package com.eda.rest.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.eda.rest.model.Message1;
import com.eda.rest.model.MsgThread;
import com.eda.rest.service.IMessageService;
import com.eda.rest.service.MessageService;
import com.google.android.gcm.server.Result;
import com.ibm.watson.developer_cloud.dialog.v1.DialogService;
import com.ibm.watson.developer_cloud.dialog.v1.model.Conversation;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;

@ServerEndpoint("/websocket")
public class WebSocketController /* extends AbstractController */{

	int counter = 1;
	//@Autowired
	
	MessageService messageService =  new MessageService();
	
	static DialogService dialogService = new DialogService();
	static Conversation conversation;
	static String dialogId = "bb17d959-c338-4acb-b841-a6e1279bb3ec";
	boolean taxDialog = false;
	boolean insuranceDialog = false;
	private Logger logger = Logger.getLogger(WebSocketController.class);
	
	@OnMessage
	public void receiveMessage(String message, Session session) throws IOException{
	    System.out.println("From websocket: " + message);

	    //Sending the messages/data to client synchronously.
	    //JSONObject jsonObject=new JSONObject(message);
	   
	    JSONObject returnToApp = new JSONObject();
		JSONObject jsonMessage = new JSONObject(message);
		logger.info("JSON MESSAGE::::::::"+jsonMessage);
		JSONObject out = messageService.createMessage(jsonMessage);

		logger.info("OUT SIZE:::::::::::::::create message::::"+out.length());
		if(out.length()!=1){
		String direction = out.getString("direction");
		String msg_content = out.getString("msg_content");
		Long thread_ref_id = out.getLong("thread_ref_id");
		String device_id = out.getString("device_id");
		String classification=out.optString("classification");
		String event_id=out.optString("event_id");
		Integer conversation_id=Integer.valueOf(out.optString("conversation_id"));
		Integer client_id = Integer.valueOf(out.optString("client_id"));
		
		logger.info("EVENT ID FRO MOBILE:::::::::::::::::::"+event_id);
		/*(msg_content.equals("i need to file tax") || msg_content.equals("i want to file tax")
		|| msg_content.equals("i want to file income tax") || msg_content.equals("i would like to file income tax")
		|| msg_content.equals("like to file tax") || msg_content.equals("i need to file income tax")
		|| msg_content.equals("file tax") || msg_content.equals("like to file income tax")))*/
		
		
		if ((direction.equals("I") && classification.equals("tax")) || ((direction.equals("I") && classification.equals("travelinsurance"))) || ((direction.equals("I") && classification.equals("floral")))) 
		{
			taxDialog = true;
			event_id="true";
			logger.info("EVENT ID IN TAX:::::::::::::::::::"+event_id);
			returnToApp.put("event_id", event_id);
		}
		
		if (event_id.equals("true")) {
			if((conversation_id == 1 && client_id == 0)){
				dialogService.setUsernameAndPassword("3b1c2fce-016c-4c21-b8e1-1387965bcfc5", "PEGLOaNItcFU");
				try {
					conversation = dialogService.createConversation(dialogId);
					logger.info("CONVERSATION:::::::::::::::::" + conversation);
					client_id = conversation.getClientId();
					conversation_id = conversation.getId();
					logger.info("CONVERSATION CLIENT ID:::::::::::::::::" + client_id);
					logger.info("CONVERSATION CONVERSATION ID:::::::::::::::::" + conversation_id);
					returnToApp.put("client_id", String.valueOf(client_id));
					returnToApp.put("conversation_id",String.valueOf(conversation_id));
					
				} catch (Exception e) {
					logger.error("MESSAGE CONTROLLER CONSTRUCTOR:::::" + e);
				}
				
			}
			
			
			logger.info("CONVERSATION ID::::::::::::::::::::" + conversation);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(DialogService.DIALOG_ID, dialogId);
			params.put(DialogService.CLIENT_ID, client_id);//conversation.getClientId());
			params.put(DialogService.CONVERSATION_ID, conversation_id);//conversation.getId());
			params.put(DialogService.INPUT, msg_content.toLowerCase());
			conversation = dialogService.converse(params);

			// conversation.setInput(msg_content);
			String response_message = conversation.getResponse().get(0);
			String response_message1 = "";
			if(conversation.getResponse().size()>1){
				response_message1 = conversation.getResponse().get(2);
			}
			
			System.out.println("CONVERSATION1:::" + response_message);

			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("device_id", device_id);
			jsonResponse.put("msg_content", response_message);
			jsonResponse.put("direction", "O");
			jsonResponse.put("thread_ref_id", thread_ref_id);
			jsonResponse.put("origin_ip", "ip");

			
			
			JSONObject outResponse = messageService.createMessage(jsonResponse);
			direction = outResponse.getString("direction");
			msg_content = outResponse.getString("msg_content");
			thread_ref_id = outResponse.getLong("thread_ref_id");

			/*if (msg_content.equals("Your request will now be forwarded to one of our agents to guide you through the filing process. Are you sure to continue?")
					|| msg_content.equals("Your request will now be forwarded to one of our agents. Are you sure to continue?")
					|| msg_content.equals("Ok")) {*/
			if(checkResponseMessage(msg_content)){
				taxDialog = false;
				event_id="false";
				conversation_id = 1;
				client_id = 0;
				logger.info("EVENT ID IN END:::::::::::::::::::"+event_id);
				returnToApp.put("event_id", event_id);
				returnToApp.put("client_id",String.valueOf(client_id));
				returnToApp.put("conversation_id",String.valueOf(conversation_id));
			}
		}
		if (direction.equals("O")) {
			Result result = null;
			//result = sendGcmPushNotification(thread_ref_id, msg_content);
			logger.info("SEND PUSH NOTIFICATION.............");
			//request.setAttribute("pushStatus", result.toString());
		}
		logger.info("EVENT ID IN LAST:::::::::::::::::::"+event_id);
		returnToApp.put("event_id", event_id);
		returnToApp.put("client_id",String.valueOf(client_id));
		returnToApp.put("conversation_id",String.valueOf(conversation_id));
		returnToApp.put("msg_id", out.getLong("msg_id"));
		returnToApp.put("thread_ref_id", out.getLong("thread_ref_id"));
		logger.info("RETURN TO APP::::::::::::::"+returnToApp.toString());
		}
		
	    
	    for(int i=0;i<=2;i++){
	    	 session.getBasicRemote().sendText("Sending message: "+  returnToApp.toString() +" from server");
	    }
	   
	  }
	
	static boolean checkResponseMessage(String message)
	{
		List<String> responseList = listResponse();
		boolean result=false;
		for(int i=0;i<responseList.size();i++)
		{
			if(message.equals(responseList.get(i).toString())){
				result = true;
			}
		}
		return result;
	}
	static List<String> listResponse()
	{
		
		List<String> responseList = new ArrayList<>();
		responseList.add("Your request will now be forwarded to one of our agents to guide you through the filing process. Are you sure to continue?");
		responseList.add("Your request will now be forwarded to one of our agents. Are you sure to continue?");
		responseList.add("I have collected all the necessary information. Shall I complete the order");
		responseList.add("Ok");
		return responseList;
	}
}


