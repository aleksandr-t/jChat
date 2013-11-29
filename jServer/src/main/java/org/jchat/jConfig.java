package org.jchat;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

class jConfig {

    static Properties loadConfig(Class<?> loader, String resourceName) {
        try (InputStream inputStream = loader.getClassLoader().getResourceAsStream(resourceName)) {
            Properties config = new Properties();
            config.load(inputStream);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean checkConfig(Properties config) {
        if (config == null)
            return false;
        return (config.containsKey("host") && config.containsKey("port"));
    }

    static Socket initSocketFromConfig(Properties config) throws Exception {
        if (!checkConfig(config))
            throw new IllegalArgumentException();

        String host = config.getProperty("host");
        int port = Integer.parseInt(config.getProperty("port"));
        return new Socket(host, port);

    }

    static ServerSocket initServerSocketFromConfig(Properties config) throws Exception {
        if (!checkConfig(config))
            throw new IllegalArgumentException();
        int port = Integer.parseInt(config.getProperty("port"));
        return new ServerSocket(port);
    }
}