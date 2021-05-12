package com.encore.rest.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApimDao {

	List<Map<String,String>> getStndListdao();
	List<Map<String,String>> getSubjectAreaListdao();
}
