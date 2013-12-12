package org.jchat;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

class jConfig {

    private static Properties loadConfig(String resourceName) throws Exception {
        Properties config = new Properties();
        InputStream inputStream = jConfig.class.getClassLoader().getResourceAsStream(resourceName);
        config.load(inputStream);
        return config;
    }

    static Socket initSocketFromConfig(String resourceName) throws Exception {
        Properties config = loadConfig(resourceName);
        String host = config.getProperty("host");
        int port = Integer.parseInt(config.getProperty("port"));
        return new Socket(host, port);
    }

    static ServerSocket initServerSocketFromConfig(String resourceName) throws Exception {
        Properties config = loadConfig(resourceName);
        int port = Integer.parseInt(config.getProperty("port"));
        return new ServerSocket(port);
    }
}