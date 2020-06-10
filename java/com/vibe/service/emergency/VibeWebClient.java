package com.vibe.service.emergency;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibe.pojo.emergency.EmergencyEvent;
import com.vibe.pojo.emergency.Operation;
import org.springframework.stereotype.Component;

@ClientEndpoint
public class VibeWebClient implements IBMSClient, AutoCloseable {
    public VibeWebClient(ERService service) {
        super();
        this.service = service;
    }

    public static final String NAME = "VibeWebClient";
    public static final String ALARM_TYPE = "alarm";
    private ERService service;
    private Session session;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void handle(List<Operation> operations, EmergencyEvent event, IBMSClient src) {
        if (operations == null || operations.isEmpty()) return;

        //TODO ....
        System.out.println("处理 " + event + ", from: " + src.getName());
        for (Operation operation : operations) {
            System.out.println("操作: " + operation);
        }
    }

    @Override
    public void close() throws IOException {
        if (session != null) session.close();
    }

    @Override
    public void init() {
        try {
            this.connect("ws://localhost:8008/websocket");
            this.write(ALARM_TYPE);
        } catch (Exception e) {
            Exception exception = new Exception("不能连接到服务器： " + this.getName(), e);
            System.out.println(exception.getMessage() + ", Caused by" + exception.getCause().getMessage());
        }
    }

    private void write(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }

    private void connect(String uri) throws Exception {
        WebSocketContainer sc = ContainerProvider.getWebSocketContainer();
        this.session = sc.connectToServer(this, URI.create(uri));
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("VibeWebClient connected " + session);
        service.conn(this);
    }

    @OnMessage
    public void onMessage(String message) throws JsonParseException, JsonMappingException, IOException {
        if (message != null) return;

        System.out.println("get message: " + message);
        com.vibe.monitor.alarm.AlarmData data = new ObjectMapper().readValue(message, com.vibe.monitor.alarm.AlarmData.class);

        EmergencyEvent ret = new EmergencyEvent();
        ret.withLevel(data.getState(), 2);
        ret.setOrigin(this.getName());
        try {
            ret.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getTime()));
        } catch (Exception e) {
            ret.setTime(new Date());
        }
        String[] caption = data.getCaption().split("->");
        ret.setType(caption[caption.length - 1]);
        ret.setDataFields(message);
        if (caption.length > 1) {
            ret.setPosition(data.getCaption());
        }
        ret.setDesc(data.getAssetId() + ":" + data.getErrorMessage());
        service.handleEvent(ret, this);
    }

    @OnClose
    public void onClose() {
        service.disconn(this);
    }
}