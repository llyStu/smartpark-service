package com.vibe.util.constant;

import com.github.pagehelper.PageInfo;
import lombok.Data;

/**
 * @Auther: zhousili
 * @Date: 2019/07/26
 * @Description:
 */
@Data
public class ResponsePageModel<T> {
	
	// 接口是否执行成功
	private Boolean successful;
	
	// 接口返回码
	private String code;
	
	// 错误时的错误信息
	private String message;
	
	// 调用成功时的具体数据
	private PageInfo<T> data;
	

	public ResponsePageModel() {
	}

	public ResponsePageModel(Boolean successful, String code, String message, PageInfo<T> data) {
		this.successful = successful;
		this.message = message;
		this.code = code;
		this.data = data;

	}
	public void setParamError(String message){
		this.successful = false;
		this.code = "1";
		this.message = message;
	}
	
	public void setExceptionError(String message){
		this.successful = false;
		this.code = "2";
		this.message = message;
	}
	
	public void setSuccess(Boolean successful, String code){
		this.successful = successful;
		this.code = code;
	}
	
	public void setSuccessAndData(PageInfo<T> data){
		this.successful = true;
		this.code = "0";
		this.data = data;
	}
	public ResponsePageModel<T> check() throws Exception {
        if (!this.getSuccessful()) {
            throw new Exception(this.getMessage());
        }
        return this;

    }


}
