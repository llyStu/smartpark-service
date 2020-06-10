package com.vibe.client;

import java.io.Serializable;

import com.vibe.pojo.user.User;

public class ClientLoginUser implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -715746559084734038L;
    private User user;
    private boolean result;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    private String setCookie;

    public String getSetCookie() {
        return setCookie;
    }

    public void setSetCookie(String setCookie) {
        this.setCookie = setCookie;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
