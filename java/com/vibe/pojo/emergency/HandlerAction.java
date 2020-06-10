package com.vibe.pojo.emergency;

import com.vibe.service.emergency.IBMSClient;

public class HandlerAction extends Action implements Handler {
    public static final Action[] actions = {
            Action.of("DO_SEND_LIGHTS_ON", "开灯"),
            Action.of("DO_OPEN_DOORS", "开门"),
            HandlerAction.of("DO_SEND_EMAIL", "发邮件", (operation, event, src) -> {
                System.out.println("发邮件。。。。");
            }),
            HandlerAction.of("DO_BROADCAST", "广播", null),
    };

    public static HandlerAction of(String name, String desc, Handler handler) {
        HandlerAction action = new HandlerAction();
        action.setName(name);
        action.setDesc(desc);
        action.setDefaultTarget(Action.TARGET_SELF);
        action.handler = handler;
        return action;
    }


    public boolean isHandler() {
        return this.handler != null;
    }

    private Handler handler;

    public void handle(Operation operation, EmergencyEvent event, IBMSClient src) {
        this.handler.handle(operation, event, src);
    }
}

interface Handler {
    void handle(Operation operation, EmergencyEvent event, IBMSClient src);
}
