package com.evergrande.generator.ui.service.impl;

import com.evergrande.generator.ui.bean.GeneratorInfo;
import com.evergrande.generator.ui.mapper.GeneratorMapper;
import com.evergrande.generator.ui.service.GeneratorService;
import com.evergrande.generator.ui.util.GeneratorUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author LXH
 * @date 2018-01-30-20:12
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    private GeneratorMapper generatorMapper;

    @Override
    public List<Map<String, Object>> queryList(Map<String, Object> map) {
        int offset = Integer.parseInt(map.get("offset").toString());
        int limit = Integer.parseInt(map.get("limit").toString());
        map.put("offset", offset);
        map.put("limit", limit);

        return generatorMapper.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return generatorMapper.queryTotal(map);
    }

    @Override
    public Map<String, String> queryTable(String dbName, String tableName) {
        return generatorMapper.queryTable(dbName, tableName);
    }

    @Override
    public List<Map<String, String>> queryColumns(String dbName, String tableName) {
        return generatorMapper.queryColumns(dbName, tableName);
    }

    @Override
    public byte[] generatorCode(GeneratorInfo generatorInfo) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (String tableName : generatorInfo.getTableNameList()) {
            //查询表信息
            Map<String, String> table = queryTable(generatorInfo.getDbName(), tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(generatorInfo.getDbName(), tableName);
            //生成代码
            GeneratorUtils.generatorCode(generatorInfo, table, columns, zip);
        }
        IOUtils.closeQuietly(zip);

        return outputStream.toByteArray();
    }

    @Override
    public List<String> listDatabase() {
        return generatorMapper.listDatabase();
    }
}
