package ru.myteas.teaclient_a;

import android.support.annotation.NonNull;

public class User {
    public static String name = null;
    public static String id = null;


    @Override
    public  String toString() {
        return name + "@" + id;
    }
}
