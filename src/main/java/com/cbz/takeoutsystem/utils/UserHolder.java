package com.cbz.takeoutsystem.utils;

import com.cbz.takeoutsystem.entity.User;

public class UserHolder {

    private final static ThreadLocal<User> tl = new ThreadLocal<>();

    public static void saveUser(User user) {
        tl.set(user);
    }

    public static User getUser() {
        return tl.get();
    }

    public static void removeUser() {
        tl.remove();
    }
}
