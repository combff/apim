package com.encore.apim.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.encore.apim.dao.ApimDao;

@Service
public class ApimServiceImpl implements ApimService {
	
	@Autowired
	ApimDao apimDao;

	@Override
	public List<Map<String, String>> getBsifListDAO() {
		return apimDao.getBsifListDAO();
	}

	@Override
	public List<Map<String, String>> getSubjectAreaListDAO() {
		return apimDao.getSubjectAreaListDAO();
	}

	
}
