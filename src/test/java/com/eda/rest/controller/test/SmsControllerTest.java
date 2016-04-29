package com.eda.rest.controller.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eda.rest.config.AppConfig;
import com.eda.rest.config.AppInitializer;
import com.eda.rest.controller.SmsController;
import com.eda.rest.model.Action;
import com.eda.rest.model.mapper.ActionMapper;
import com.eda.rest.service.ISmsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, AppInitializer.class })
@WebAppConfiguration
public class SmsControllerTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	
	@Mock
	ISmsService smsService;
	@InjectMocks
	SmsController smsController;
	private MockMvc mockMvc;
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(smsController).build();
	}
	@Test
	public void testSmsSend() throws Exception {

		JSONObject actionId=new JSONObject();
		actionId.put("actionid", 1);
		List<Action> list = new ArrayList<Action>();
		list.add(ActionMapper.buildSms());
		String msgContent=list.get(0).getContent();
		when(smsService.listContent(actionId)).thenReturn(msgContent);
		mockMvc.perform(post("/sms/send/").content("{'actionid':1}"))
				.andExpect(status().isOk()).andDo(print()).andReturn();
	}

	@Test
	public void testSmsCreate() throws Exception {

		JSONObject jsonData = ActionMapper.jsonSms();
		Action action = ActionMapper.buildSms();
		List<Action> list = new ArrayList<Action>();
		list.add(action);
		when(smsService.createSmsAction(jsonData)).thenReturn(true);
		MvcResult result = mockMvc
				.perform(
						post("/sms/create/").content(jsonData.toString())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		System.out.println("RESULT::" + result);
	}

	@Test
	public void testUpdateActionStatus() throws Exception {
		
		JSONObject jsonData=new JSONObject();
		jsonData.put("action_id",1L);
		jsonData.put("remarks", "IC");
		jsonData.put("status", "IC");
		
		when(smsService.updateActionStatus(jsonData)).thenReturn(
				true);
		MvcResult result = mockMvc
				.perform(
						post("/sms/updatestatus/").content(
								jsonData.toString()).contentType(
								MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();
		System.out.println("RESULT::" + result);
	}

}
