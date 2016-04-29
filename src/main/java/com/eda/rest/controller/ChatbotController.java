package com.eda.rest.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eda.rest.service.IChatbotService;

@RestController
public class ChatbotController extends AbstractController{

	@Autowired
	IChatbotService chatbotService;
	@RequestMapping(value="/event/create/",method=RequestMethod.POST)
	public boolean createEvent(HttpServletRequest request,HttpServletResponse response)
	{
		JSONObject jsonEvent=readJSONData(request);

		return chatbotService.createEvent(jsonEvent);
		
	}
	@RequestMapping(value="/event/list/questions/",method=RequestMethod.POST)
	public String listQuestion(HttpServletResponse response,HttpServletRequest request) throws JSONException, IOException
	{
		JSONObject jsonList=readJSONData(request);
		//System.out.println(jsonList.toString());
		//String output=chatbotService.listQuestion(jsonList);
		return chatbotService.listQuestion(jsonList);
		
	}
}
