package com.vibe.util.constant;

import lombok.Data;

/**
 * @Auther: zhousili
 * @Date: 2019/07/26
 * @Description: 处理响应单一实体的请求
 */
@Data
public class ResponseModel<T> {

    // 接口是否执行成功
    private Boolean successful;

    // 接口返回码
    private String code;

    // 错误时的错误信息
    private String message;

    // 调用成功时的具体数据
    private T data;


    public ResponseModel() {

    }

    public ResponseModel(Boolean successful, String code, String message, T data) {
        this.successful = successful;
        this.message = message;
        this.code = code;
        this.data = data;

    }
    public ResponseModel(boolean successful) {
        this.successful = successful;
    }

    public ResponseModel<T> data(T data) {
        this.data = data;
        return this;
    }
    public ResponseModel<T> errorMessage(String message) {
        this.message = message;
        return this;
    }


    public static <T> ResponseModel<T> success(T data) {
        return new ResponseModel<T>(true).data(data);
    }

    public static ResponseModel failure(String errorMessage) {
        return new ResponseModel<>(false).errorMessage(errorMessage);
    }

    public ResponseModel<T> code(String code) {
        this.code = code;
        return this;
    }

    public ResponseModel<T> check() throws Exception {
        if (!this.getSuccessful()) {
            throw new Exception(this.getMessage());
        }
        return this;

    }

}

