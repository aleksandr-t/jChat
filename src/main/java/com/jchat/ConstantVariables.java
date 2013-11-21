package com.jchat;

public class ConstantVariables {
    public static final int SERVER_PORT = 12345;
    public static final String HOST = "localhost";

    public static enum jMsgFlag {
        CONNECT,
        DISCONNECT,
        CORRUPTED,
        MESSAGE,
        INFO,
        WARNING
    }
}
