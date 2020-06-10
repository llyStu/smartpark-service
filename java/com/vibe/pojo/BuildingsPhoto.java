package com.vibe.pojo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BuildingsPhoto {
    private Integer id;
    
    private int parentId;

    private String photo;//用于拼接字符串保存到数据库
    private List<String>photos;//用于回显

    private LocalDate date;
    //private Date date;
    private String bDate;

    private String evaluation;

    private String protectStatus;

    private String statusDescription;

    private int type;
    
    
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    public String getDate() {
        return this.getBDate();
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.bDate = date.toString();
    }
	
    public String getBDate() {
		return bDate;
	}




    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation == null ? null : evaluation.trim();
    }

    public String getProtectStatus() {
        return protectStatus;
    }

    public void setProtectStatus(String protectStatus) {
        this.protectStatus = protectStatus == null ? null : protectStatus.trim();
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription == null ? null : statusDescription.trim();
    }

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}



}