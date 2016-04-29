package com.eda.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IncomeTaxController {

	
	@RequestMapping(value="/incometax/get/questions/",method=RequestMethod.POST)
	public String getTaxQuestion(HttpServletRequest request,HttpServletResponse response)
	{
		
		return null;
		
	}
}
