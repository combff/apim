package com.encore.rest.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.encore.rest.service.ApimServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
@RequestMapping(value = "/BSIFREST/service", produces="application/json;charset=UTF-8")
public class ApimController {

	private Gson gson = new GsonBuilder().create();
	
	@Autowired
	ApimServiceImpl apimServiceImpl;
	
	@Autowired
	HmacAuth hmacAuth;
	
	@RequestMapping(value = "/bsif/list", method = RequestMethod.GET )
	public @ResponseBody String getStndListviw(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, String>> stndList = apimServiceImpl.getStndListservice();
		
		try {
			hmacAuth.hmanAuth(request);
			return "{ \"total\" : 1, \"count\": " + stndList.size() + ", \"results\" : " + gson.toJson(stndList) + "}";
		}catch(Exception e){
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			hmacAuth.buildErr(e);
			return "{ \"total\" : 1, \"count\": " + stndList.size() + ", \"results\" : " + gson.toJson(stndList) + "}";
		}
	}
	
	@RequestMapping(value = "/subjectarea/list", method = RequestMethod.GET )
	public @ResponseBody String getSubjectAreaListviw(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, String>> subjectAreaList = apimServiceImpl.getSubjectAreaListservice();
		
		try {
			hmacAuth.hmanAuth(request);
			return "{ \"total\" : 1, \"count\": " + subjectAreaList.size() + ", \"results\" : " + gson.toJson(subjectAreaList) + "}";
		}catch(Exception e){
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			hmacAuth.buildErr(e);
			return "{ \"total\" : 1, \"count\": " + subjectAreaList.size() + ", \"results\" : " + gson.toJson(subjectAreaList) + "}";
		}
	}
	
}
