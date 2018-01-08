package com.irpyte.server.controllers;

public class Constants {

    public static final String CONTAINER = "data";
    public static final String STORAGE_ACCOUNT = "irpytedata";
    public static final String USER_DATA_LOCATION = "userdata";

    public static String getUserDataLocation(String id) {
        return USER_DATA_LOCATION + "/" + id;
    }

}
