package com.evergrande.generator.ui.service;

import com.evergrande.generator.ui.bean.GeneratorInfo;

import java.util.List;
import java.util.Map;

/**
 * @author LXH
 * @date 2018-01-30-20:12
 */
public interface GeneratorService {

	List<Map<String, Object>> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	Map<String, String> queryTable(String dbName, String tableName);

	List<Map<String, String>> queryColumns(String dbName, String tableName);

	byte[] generatorCode(GeneratorInfo generatorInfo);

    List<String> listDatabase();
}
