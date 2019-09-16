package com.evergrande.generator.ui.util;

import com.evergrande.generator.ui.bean.GeneratorInfo;
import com.evergrande.generator.ui.model.ColumnEntity;
import com.evergrande.generator.ui.model.TableEntity;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author LXH
 * @date 2018-01-30-20:12
 */
public class GeneratorUtils {

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("template/mapper.xml.vm");
        templates.add("template/model.java.vm");
        templates.add("template/mapper.java.vm");
        templates.add("template/controller.java.vm");
        templates.add("template/service.java.vm");
        templates.add("template/serviceImpl.java.vm");
       // templates.add("template/FacadeImpl.vm");
        //templates.add("template/Facade.vm");
       // templates.add("template/dubbo.vm");
      //  templates.add("template/dubboImpl.vm");
      //  templates.add("template/feignClient.vm");
      //  templates.add("template/fallbackFactory.vm");
        
      //  templates.add("template/server/application.properties.vm");
       // templates.add("template/server/bootstrap.properties.vm");
      //  templates.add("template/server/logback-spring.vm");
      //  templates.add("template/server/pom.xml.vm");
        
     //   templates.add("template/api/pom.xml.api.vm");
//        templates.add("template/vodelete.java.vm");
    //    templates.add("template/voquery.java.vm");
    //    templates.add("template/vosave.java.vm");
     //   templates.add("template/voupdate.java.vm");
        
        templates.add("template/modelD.java.vm");
        templates.add("template/modelQ.java.vm");
        templates.add("template/modelQP.java.vm");
        templates.add("template/modelS.java.vm");
        templates.add("template/modelU.java.vm");
        templates.add("template/modelVO.java.vm");
        
        templates.add("template/pagelist.html.vm");
        templates.add("template/pageadd.html.vm");
        templates.add("template/pageupdate.html.vm");
        
        templates.add("template/menu-permision.vm");
        
     //   templates.add("template/test/application.properties.test.vm");
    //    templates.add("template/test/bootstrap.properties.test.vm");
     //   templates.add("template/test/logback-spring.test.vm");
     //   templates.add("template/test/pom.xml.test.vm");
        
        
        
        
        return templates;
    }

    /**
     * 生成代码
     */
    public static void generatorCode(GeneratorInfo generatorInfo, Map<String, String> table,
                                     List<Map<String, String>> columns, ZipOutputStream zip) {
        //配置信息
        Configuration config = getConfig();

        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComment(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), generatorInfo.getTablePrefix());
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<ColumnEntity>();
        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComment(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);

            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }
            

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comment", tableEntity.getComment());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("package", config.getString("package"));
        map.put("author", generatorInfo.getAuthor());
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        map.put("moduleName", generatorInfo.getModuleName());
        map.put("secondModuleName", toLowerCaseFirstOne(className));
        map.put("baseResultMap", true);
        map.put("projectName", generatorInfo.getProjectName());
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            try (StringWriter sw = new StringWriter()) {
                Template tpl = Velocity.getTemplate(template, "UTF-8");
                tpl.merge(context, sw);
                //添加到zip
                String fileNameForOneFolder = getFileNameForOneFolder(template, tableEntity.getClassName(), config.getString("package"), config.getString("mainModule"));
                zip.putNextEntry(new ZipEntry(fileNameForOneFolder));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                zip.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String projectName = "order";
    
    public static String getFileNameForOneFolder(String template, String className, String packageName, String moduleName) {
    	String packagePath = "sc-backend-single" + File.separator + "main" + File.separator + "java" + File.separator; 
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }
        
        if (template.contains("controller.java.vm")) {
            return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+File.separator +toLowerCaseFirstOne(className) + File.separator+ className + "Controller.java";
        }
        if (template.contains("service.java.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+File.separator +toLowerCaseFirstOne(className) + File.separator+ "I"+className + "Service.java";
        }
        
        if (template.contains("serviceImpl.java.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+File.separator +toLowerCaseFirstOne(className) + File.separator+ className + "ServiceImpl.java";
        }

        if (template.contains("mapper.java.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+File.separator + toLowerCaseFirstOne(className) + File.separator+ className + "Mapper.java";
        }
        if (template.contains("mapper.xml.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+File.separator + toLowerCaseFirstOne(className) + File.separator+ className + "Mapper.xml";
        }
        if (template.contains("model.java.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+ File.separator +toLowerCaseFirstOne(className) + File.separator+ className + "Model.java";
        }
        if (template.contains("modelD.java.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+ File.separator +toLowerCaseFirstOne(className) + File.separator+ className + "BOD.java";
        }
        if (template.contains("modelQ.java.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+ File.separator +toLowerCaseFirstOne(className) + File.separator+ className + "BOQ.java";
        }
        if (template.contains("modelQP.java.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+ File.separator +toLowerCaseFirstOne(className) + File.separator+ className + "BOQP.java";
        }
        if (template.contains("modelS.java.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+ File.separator +toLowerCaseFirstOne(className) + File.separator+ className + "BOS.java";
        }
        if (template.contains("modelU.java.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+ File.separator +toLowerCaseFirstOne(className) + File.separator+ className + "BOU.java";
        }
        if (template.contains("modelVO.java.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+ File.separator +toLowerCaseFirstOne(className) + File.separator+ className + "VO.java";
        }
        if (template.contains("pageadd.html.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+ File.separator +toLowerCaseFirstOne(className) + File.separator + "add"+className+".html";
        }
        if (template.contains("pagelist.html.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+ File.separator +toLowerCaseFirstOne(className) + File.separator+ toLowerCaseFirstOne(className) + "List.html";
        }
        if (template.contains("pageupdate.html.vm")) {
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+ File.separator +toLowerCaseFirstOne(className) + File.separator+"update" +className + ".html";
        }
        
        if(template.contains("menu-permision.vm")){
        	return packagePath +"com"+ File.separator+"cloud"+File.separator +"business"+ File.separator +toLowerCaseFirstOne(className) + File.separator+"update" +className + ".sql";
        }
        return null;

    }

    public static String getFileNameForPro(String template, String className, String packageName, String moduleName) {
        String packagePath = projectName+"-server" + File.separator + "main" + File.separator + "java" + File.separator;
        //String frontPath = "ui" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("pom.xml.vm")) {
            return projectName+"-server"  + File.separator  + "pom.xml";
        }
        
        if (template.contains("logback-spring.vm")) {
        	return projectName+"-server" + File.separator + "main" + File.separator + "resources" + File.separator + " logback-spring.xml";
        //	return projectName + File.separator + "main" + File.separator + "resources" + File.separator + "logback-spring.xml";
        }
        if (template.contains("bootstrap.properties.vm")) {
        	return projectName+"-server" + File.separator + "main" + File.separator + "resources" + File.separator + " bootstrap.properties";
        //	return projectName + File.separator + "main" + File.separator + "resources" + File.separator + "bootstrap.properties";
        }
        if (template.contains("application.properties.vm")) {
        	return projectName+"-server" + File.separator + "main" + File.separator + "resources" + File.separator + " application.properties";
        //	return projectName + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties";
        }

        if (template.contains("controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }
        
        if (template.contains("service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }
        if (template.contains("mapper.java.vm")) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }
        if (template.contains("model.java.vm")) {
            return packagePath + "model" + File.separator + className + "Model.java";
        }

        if (template.contains("serviceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }
        if (template.contains("FacadeImpl.vm")) {
            return packagePath + "facade" + File.separator + "impl" + File.separator + className + "FacadeImpl.java";
        }
        if (template.contains("Facade.vm")) {
            return packagePath + "facade" + File.separator  + className + "Facade.java";
        }
        if (template.contains("dubboImpl.vm")) {
            return packagePath + "dubbo" + File.separator + "impl" + File.separator + className + "DubboImpl.java";
        }
        if (template.contains("mapper.xml.vm")) {
            return projectName+"-server" + File.separator  + "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + className + "Mapper.xml";
        }

        //TODO vo
        //TODO xml
        String packagePathapi = projectName+"-api" + File.separator + "main" + File.separator + "java" + File.separator;
        if (template.contains("dubbo.vm")) {
            return packagePathapi + "dubbo" + File.separator  + className + "dubbo.java";
        }
        
        if (template.contains("feignClient.vm")) {
            return packagePathapi + "client" + File.separator + "HtCloud"+ className + "Client.java";
        }
        if (template.contains("fallbackFactory.vm")) {
            return packagePathapi + "hystrix" + File.separator +"HtCloud" + className + "ClientHystrix.java";
        }
        
        if (template.contains("pom.xml.api.vm")) {
            return projectName+"-api"  + File.separator  + "pom.xml";
        }
        if (template.contains("vodelete.java.vm")) {
            return packagePathapi + "vo" + File.separator + className + "VoD.java";
        }
        if (template.contains("voquery.java.vm")) {
            return packagePathapi + "vo" + File.separator + className + "VoQ.java";
        }
        if (template.contains("vosave.java.vm")) {
            return packagePathapi + "vo" + File.separator + className + "VoS.java";
        }
        if (template.contains("voupdate.java.vm")) {
            return packagePathapi + "vo" + File.separator + className + "VoU.java";
        }
        
        
       
        
        String packagePathtest = projectName+"-test" + File.separator + "main" + File.separator + "java" + File.separator;

        if (template.contains("pom.xml.test.vm")) {
            return projectName+"-test"  + File.separator  + "pom.xml";
        }
        if (template.contains("logback-spring.test.vm")) {
        	return projectName+"-test" + File.separator + "main" + File.separator + "resources" + File.separator + " logback-spring.xml";
        }
        if (template.contains("bootstrap.properties.test.vm")) {
        	return projectName +"-test" +File.separator + "main" + File.separator + "resources" + File.separator + " bootstrap.properties";
        }
        if (template.contains("application.properties.test.vm")) {
        	return projectName +"-test"+ File.separator + "main" + File.separator + "resources" + File.separator + " application.properties";
        }
 
        

        return null;
    }
    

    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
    
    //首字母转大写
    public static String toUpCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
