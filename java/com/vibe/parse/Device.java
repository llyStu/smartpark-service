package com.vibe.parse;

public abstract class Device extends BaseBean {
	private String parent;
	private String name;
	private String caption;
	private String catalog;
	
	

	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public abstract String getType();
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "insert into t_device(id, parent, catalog, type, name, caption)values (" 
				+ getId() + "," + parent + "," + catalog + ", \"" + getType() + "\", \"" + name + "\" , \"" + caption + "\");\n";
	}
}
