package com.evergrande.generator.ui.bean;

import java.util.List;

/**
 * @author K
 * @date 2018-02-07-8:48
 */
public class GeneratorInfo {

    private String projectName;
    private String tablePrefix;
    private String author;
    /**
     * @see com.evergrande.generator.ui.enums.Switch
     */
    private String isUseLombok;
    /**
     * @see com.evergrande.generator.ui.enums.Switch
     */
    private String isSingleTableOperation;
    private String IDLFileName;
    private List<String> tableNameList;
    private String dbName;
    private String moduleName;

    public String getProjectName() {
        return "ht-cloud-" + moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix == null ? null : tablePrefix.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getIsUseLombok() {
        return isUseLombok;
    }

    public void setIsUseLombok(String isUseLombok) {
        this.isUseLombok = isUseLombok == null ? null : isUseLombok.trim();
    }

    public String getIsSingleTableOperation() {
        return isSingleTableOperation;
    }

    public void setIsSingleTableOperation(String isSingleTableOperation) {
        this.isSingleTableOperation = isSingleTableOperation == null ? null : isSingleTableOperation.trim();
    }

    public String getIDLFileName() {
        return IDLFileName;
    }

    public void setIDLFileName(String IDLFileName) {
        this.IDLFileName = IDLFileName == null ? null : IDLFileName.trim();
    }

    public List<String> getTableNameList() {
        return tableNameList;
    }

    public void setTableNameList(List<String> tableNameList) {
        this.tableNameList = tableNameList;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName == null ? null : dbName.trim();
    }
}
