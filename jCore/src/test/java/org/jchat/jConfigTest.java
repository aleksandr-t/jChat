package org.jchat;

import org.junit.Test;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static org.junit.Assert.assertNotNull;

public class jConfigTest {

    @Test(expected = SocketException.class)
    public void testInitSocketFromConfig() throws Exception {
        Socket socket = jConfig.initSocketFromConfig("properties.config");
        assertNotNull(socket);
        socket.close();
    }

    @Test
    public void testInitServerSocketFromConfig() throws Exception {
        ServerSocket serverSocket;

        serverSocket = jConfig.initServerSocketFromConfig("properties.config");
        assertNotNull(serverSocket);
        serverSocket.close();
    }
}
