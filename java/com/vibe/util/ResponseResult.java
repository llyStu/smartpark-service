package com.vibe.util;

import com.vibe.pojo.Response;

public class ResponseResult {

    public static Response getANewResponse(Boolean result) {
        Response response = new Response();
        response.setResult(result);
        return response;
    }
}
