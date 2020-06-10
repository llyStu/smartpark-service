package com.vibe.util.constant;

import lombok.Data;

import java.util.List;

/**
 * @Auther: zhousili
 * @Date: 2019/07/26
 * @Description:  处理响应列表类的接口，泛型指定需要响应的实体
 */
@Data
public class ResponseListModel<T> {
    // 接口是否执行成功
    private Boolean successful;

    // 接口返回码
    private String code;

    // 错误时的错误信息
    private String message;

    // 调用成功时的具体数据
    private List<T> data;


    public ResponseListModel() {
    }

    public ResponseListModel(Boolean successful, String code, String message, List<T> data) {
        this.successful = successful;
        this.message = message;
        this.code = code;
        this.data = data;

    }
    public ResponseListModel<T> check() throws Exception {
        if (!this.getSuccessful()) {
            throw new Exception(this.getMessage());
        }
        return this;

    }

}
