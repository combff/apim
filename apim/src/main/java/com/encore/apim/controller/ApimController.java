package com.encore.apim.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.encore.apim.service.ApimServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(value = "/service", method = { RequestMethod.GET, RequestMethod.POST }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class ApimController {

	private final static Logger logger = LoggerFactory.getLogger(ApimController.class);

	@Value("${hmac.use.yn}")
	protected String hmacUseYn;

	@Autowired
	protected ApimServiceImpl apim;

	@Autowired
	protected HmacAuth hmacAuth;

	@RequestMapping(value = "/bsif/list")
	public ResponseEntity<HashMap<String, Object>> getStndList(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> result = new HashMap<>();
		List<Map<String, String>> bsifList = apim.getBsifListDAO();

		if ("Y".equals(hmacUseYn)) {
			try {
				hmacAuth.hmacAuth(request);
			} catch (Exception e) {
				logger.error("{}", ExceptionUtils.getRootCauseMessage(e));
				
				response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
				result = hmacAuth.buildErr(e);
				
				return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.EXPECTATION_FAILED);
			}
		}

		if (bsifList.size() > 0) {
			result.put("total", bsifList.get(0).get("TOTAL_ROW_COUNT"));
			result.put("count", bsifList.size());
		} else {
			result.put("total", 0);
			result.put("count", 0);
		}

		result.put("results", bsifList);
		
		return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/subjectarea/list")
	public ResponseEntity<HashMap<String, Object>> getSubjectAreaList(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> result = new HashMap<>();
		List<Map<String, String>> subjectAreaList = apim.getSubjectAreaListDAO();

		if ("Y".equals(hmacUseYn)) {
			try {
				hmacAuth.hmacAuth(request);
			} catch (Exception e) {
				logger.error("{}", ExceptionUtils.getRootCauseMessage(e));
				
				response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
				result = hmacAuth.buildErr(e);
				
				return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.EXPECTATION_FAILED);
			}
		}

		if (subjectAreaList.size() > 0) {
			result.put("total", subjectAreaList.size());
			result.put("count", subjectAreaList.size());
		} else {
			result.put("total", 0);
			result.put("count", 0);
		}

		result.put("results", subjectAreaList);
		
		return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
	}

}
