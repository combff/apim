package com.encore.apim.service;

import java.util.List;
import java.util.Map;

public interface ApimService {

	List<Map<String, String>> getBsifListDAO();

	List<Map<String, String>> getSubjectAreaListDAO();
	
}
