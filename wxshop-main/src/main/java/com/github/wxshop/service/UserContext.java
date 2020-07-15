package com.github.wxshop.service;

import com.github.wxshop.generate.User;

public class UserContext {
    private static ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(User user){
        currentUser.set(user);
    }


    public static User getCurrentUser(){
        return currentUser.get(); //不同地线程获取到的数据是不一样的!
    }


    public static void clearCurrentUser() {
        currentUser.remove();
    }
}
