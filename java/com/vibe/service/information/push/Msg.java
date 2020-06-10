package com.vibe.service.information.push;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Msg {
    public static final ObjectMapper JSON = new ObjectMapper();
    private String type, desc, data;

    public static <T> Msg of(T data) throws JsonProcessingException {
        return Msg.of("", data);
    }

    public static <T> Msg of(String desc, T data) throws JsonProcessingException {
        String clazz = data.getClass().getSimpleName();
        return Msg.of(clazz, desc, data);
    }

    public static <T> Msg of(String type, String desc, T data) throws JsonProcessingException {
        String json = JSON.writeValueAsString(data);
        return new Msg(type, desc, json);
    }


    public static Msg from(String json) throws JsonParseException, JsonMappingException, IOException {
        return JSON.readValue(json, Msg.class);
    }

    public <T> T toData(Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        return JSON.readValue(this.data, clazz);
    }

    @Override
    public String toString() {
        try {
            return JSON.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String typeAndDesc() {
        return type + (StringUtils.isBlank(desc) ? "" : "=" + desc);
    }


    public Msg() {
    }

    public Msg(String type, String desc, String data) {
        super();
        this.type = type;
        this.desc = desc;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
