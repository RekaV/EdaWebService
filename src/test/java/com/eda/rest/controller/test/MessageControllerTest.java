package com.eda.rest.controller.test;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eda.rest.config.AppConfig;
import com.eda.rest.config.AppInitializer;
import com.eda.rest.controller.MessageController;
import com.eda.rest.model.Message1;
import com.eda.rest.model.dao.IMessageDao;
import com.eda.rest.model.dao.SequenceDao;
import com.eda.rest.model.mapper.MessageMapper;
import com.eda.rest.service.IThreadService;
import com.eda.rest.service.MessageService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, AppInitializer.class })
@WebAppConfiguration
public class MessageControllerTest {
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Spy
	MessageService messageService;
	@Mock
	IThreadService threadService;
	@Mock
	IMessageDao messageDao;
	@Spy
	SequenceDao sequenceDao;
	@InjectMocks
	MessageController messageController;

	
	
	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
	}

	//@Test
	public void testMessageCreate() throws Exception {

		JSONObject jsonData = MessageMapper.jsonMessage();
		JSONObject jsonOut = MessageMapper.jsonOut();

		jsonOut.put("event_id", jsonData.getString("event_id"));
		jsonOut.put("client_id",jsonData.getString("client_id"));
		jsonOut.put("conversation_id",jsonData.getString("conversation_id"));
		jsonOut.put("msg_id", jsonData.getLong("msg_id"));
		jsonOut.put("thread_ref_id", jsonData.getLong("thread_ref_id"));
		
		//Mockito.when(sequenceDao.getNextSequence("eda", "msg_id")).thenReturn(100000L);
		Mockito.when(messageService.createMessage(jsonData)).thenReturn(jsonOut);
		MvcResult result = mockMvc
				.perform(post("/message/create/").content(jsonData.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		System.out.println("RESULT::" + result);
	}

	@Test
	public void testHome() throws Exception {
		String output = "<html><body><h1>Eda Web Service version 2.2 running successfully.......</h1></body></html>";
		Mockito.when(messageService.getCollectionName()).thenReturn(
				"[eda_action, eda_agent, eda_chatbot, eda_gcm_reg, eda_insurance, eda_location,"
				+ " eda_message, eda_thread, errorBundle, error_log, objectlabs-system, "
						+ "objectlabs-system.admin.collections," + " sequence, songs, system.indexes]");
		MvcResult result = mockMvc.perform(get("/").content(output).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		System.out.println("RESULT::" + result);

	}

	@Test
	public void listMessage() throws Exception {
		List<Message1> msgList = new ArrayList<Message1>();
		Message1 msg = new Message1();
		msgList.add(msg);
		JSONObject threadJson = new JSONObject();
		threadJson.put("thread_id", 1L);
		Long thread_id = 1L;
		Mockito.when(messageService.listMessage(thread_id)).thenReturn(msgList);
		MvcResult result = mockMvc.perform(
				post("/message/listmessage/").content(threadJson.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		System.out.println("RESULT::" + result);

	}
	@Test
	public void messageCount() throws Exception{
		JSONObject jsonMsg=new JSONObject();
		jsonMsg.put("thread_id", 1);
		Long thread_id=1L;
		
		Mockito.when(messageService.messageCount(thread_id)).thenReturn(thread_id);
		MvcResult result = mockMvc.perform(
				post("/message/count/").content(jsonMsg.toString()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		System.out.println("RESULT::" + result);

	}
}
