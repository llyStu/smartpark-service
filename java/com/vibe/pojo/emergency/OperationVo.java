package com.vibe.pojo.emergency;

public class OperationVo extends Operation {
	private int[] oids;
	private Integer pageNum = 1, pageSize = 10;

	public int[] getOids() {
		return oids;
	}

	public void setOids(int[] oids) {
		this.oids = oids;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
