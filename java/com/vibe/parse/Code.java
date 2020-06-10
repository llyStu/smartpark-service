package com.vibe.parse;

public class Code extends BaseBean {
	private String parent;
	private String name;
	private String unit;
	private String catalog;
	
	
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "insert into t_code(catalog, id, parent, name, unit)values (" + getCatalog() +"," 
				+ getId() + "," + parent + ",\"" + name + "\" , " + (unit == null?unit:("\""+unit+"\"")) + ");";
	}
}
