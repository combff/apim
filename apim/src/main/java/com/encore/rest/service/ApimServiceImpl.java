package com.encore.rest.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.encore.rest.dao.ApimDao;

@Service
public class ApimServiceImpl implements ApimService {
	
	@Autowired
	ApimDao apimDao;

	@Override
	public List<Map<String, String>> getStndListservice() {
		return apimDao.getStndListdao();
	}

	@Override
	public List<Map<String, String>> getSubjectAreaListservice() {
		return apimDao.getSubjectAreaListdao();
	}

	
}
