package com.evergrande.generator.ui.model;

/**
 * @author LXH
 * @date 2018-01-30-20:12
 */
public class ColumnEntity {
	/**
	 * 列名
	 */
    private String columnName;
	/**
	 * 列类型
	 */
    private String dataType;
	/**
	 * 列备注
	 */
    private String comment;
	/**
	 * 属性名称(第一个字母大写)，如：user_name => UserName
	 */
    private String attrName;
	/**
	 *  属性名称(第一个字母小写)，如：user_name => userName
	 */
    private String attrname;
	/**
	 * 属性类型
	 */
    private String attrType;
	/**
	 * auto_increment
	 */
    private String extra;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName == null ? null : columnName.trim();
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType == null ? null : dataType.trim();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment == null ? null : comment.trim();
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName == null ? null : attrName.trim();
	}

	public String getAttrname() {
		return attrname;
	}

	public void setAttrname(String attrname) {
		this.attrname = attrname == null ? null : attrname.trim();
	}

	public String getAttrType() {
		return attrType;
	}

	public void setAttrType(String attrType) {
		this.attrType = attrType == null ? null : attrType.trim();
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra == null ? null : extra.trim();
	}
}
