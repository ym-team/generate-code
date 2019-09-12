package com.evergrande.generator.ui.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author LXH
 * @date 2018-01-30-20:12
 */
@Mapper
public interface GeneratorMapper {
	
	List<Map<String, Object>> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	Map<String, String> queryTable(@Param("dbName") String dbName, @Param("tableName") String tableName);
	
	List<Map<String, String>> queryColumns(@Param("dbName") String dbName, @Param("tableName") String tableName);

    List<String> listDatabase();
}
