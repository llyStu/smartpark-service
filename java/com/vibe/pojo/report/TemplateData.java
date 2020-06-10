package com.vibe.pojo.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class TemplateData {
	private Integer tid;
	private String name;
	private String clazz;
	private String method;
	private byte[] data;
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<String, Serializable> getMap() throws IOException, ClassNotFoundException {
		if (data == null) return Collections.EMPTY_MAP;
		
		try(ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
			Map<String, Serializable> map = (Map<String, Serializable>)ois.readObject();
			return map;
		}
	}
	public void setMap(Map<String, Serializable> data) throws IOException {
		try(
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos); ) {
			oos.writeObject(data);
			oos.flush();
			this.data = baos.toByteArray();
		}
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "TemplateData [tid=" + tid + ", name=" + name + ", clazz=" + clazz + ", method=" + method + "]";
	}
}
