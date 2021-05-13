package com.encore.apim.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApimDao {

	List<Map<String, String>> getBsifListDAO();

	List<Map<String, String>> getSubjectAreaListDAO();
	
}
