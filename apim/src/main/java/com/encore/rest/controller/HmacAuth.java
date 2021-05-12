package com.encore.rest.controller;

import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class HmacAuth {

	protected final String SIGNATURE_ALGORITHM = "HmacSHA512";
	
	@Value("${key}")
	protected String SECRET;
	
	protected long baseTimeStamp = 10 * 60 * 1000; //msec
	protected final String ERR_MESSAGE_INVALID_SIGNATURE = "hmac signature is not valid";
	protected final String ERR_MESSAGE_TIMEOUT = "Request is not valid(timeout)";
	protected final String ERR_SERVER_NAME = "EDIS";
	
	@SuppressWarnings("unchecked")
	protected void hmanAuth(HttpServletRequest request) throws Exception {
		String xAuthTime = StringUtils.defaultString(request.getHeader("X-AuthorizationTime"), "");
		String xAppName = StringUtils.defaultString(request.getHeader("X-APP-NAME"), "");
		String xGTId = StringUtils.defaultString(request.getHeader("X-Global-Transaction-ID"), "");
		String signature = StringUtils.defaultString(request.getHeader("X-Header-Authorization"), "");
		String message = xAuthTime + "@" + xAppName + "@" + xGTId;
		String checkHash = getHmacSignature(message.getBytes());
		
		if (!checkHash.equals(signature)) {
			System.out.println("xAuthorizationTime: " + xAuthTime);
			System.out.println("xAppName: " + xAppName);
			System.out.println("X-Global-Transaction-ID: " + xGTId);
			System.out.println("signature: " + signature);
			System.out.println("check Hmac Hash: " + checkHash);
			throw new RuntimeException(ERR_MESSAGE_INVALID_SIGNATURE);
		}
		
		if(!isValidTime(xAuthTime)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmdd'T'HHmmssZ");
			String nowDateStr = dateFormat.format(new Date());
			
			System.out.println("xAuthorizationTime: " + xAuthTime );
			System.out.println("current Time: " + nowDateStr );
			throw new RuntimeException(ERR_MESSAGE_TIMEOUT);
		}
	}
	
	protected String getHmacSignature(byte[] message) {
		byte[] key = SECRET.getBytes();
		final SecretKeySpec secretKey = new SecretKeySpec(key, SIGNATURE_ALGORITHM);
		
		try {
			Mac mac = Mac.getInstance(SIGNATURE_ALGORITHM);
			mac.init(secretKey);
			return Base64.encodeBase64String(mac.doFinal(message));
		}catch(Exception ignored){
			return "";
		}
	}
	protected boolean isValidTime(String inboundTimestamp) throws ParseException{
		boolean isValid = true;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmdd'T'HHmmssZ");
		
		Date inboundDate = dateFormat.parse(inboundTimestamp);
		long nowTimeStamp = (new Date()).getTime();
		long inboundTimeStamp = inboundDate.getTime();
		if (nowTimeStamp > (inboundTimeStamp + baseTimeStamp)) {
			isValid = false;
		}
		
		return isValid;
	}
	
	protected HashMap<String,Object> buildErr(Exception e){
		HashMap<String,Object> errResult = new HashMap<String,Object>();
		
		if(e.getMessage().equals(ERR_MESSAGE_INVALID_SIGNATURE)) {
			errResult.put("ErrorServer", ERR_SERVER_NAME);
			errResult.put("ErrorCode", "01");
			errResult.put("ErrorMsg", "hmacError");
		}else if(e.getMessage().equals(ERR_MESSAGE_TIMEOUT)) {
			errResult.put("ErrorServer", ERR_SERVER_NAME);
			errResult.put("ErrorCode", "02");
			errResult.put("ErrorMsg", "hmacError");
		}else{
			errResult.put("ErrorServer", ERR_SERVER_NAME);
			errResult.put("ErrorCode", "");
			errResult.put("ErrorMsg", e.getMessage());
		}
		
		return errResult;
	}
	
}
