package com.vibe.util;

import javax.servlet.http.HttpServletRequest;

import com.vibe.pojo.user.User;

public class UserUtil {

    public static User getCurrentUser(HttpServletRequest request) {
        Object queryUser = request.getSession().getAttribute("loginUser");
        if (queryUser != null) {
            return (User) queryUser;
        }
        return null;
    }

}
