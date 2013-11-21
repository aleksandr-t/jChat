package com.jchat.consolechat;

import com.jchat.ConstantVariables;
import com.jchat.jMessage;
import com.jchat.serverside.jServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class ConsoleServer {

    public static void main(String[] args) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            jServer server = new jServer();
            String key;
            do {
                key = reader.readLine();
                switch (key) {
                    case "start":
                        server.startServer();
                        break;
                    case "stop":
                        server.stopServer();
                        break;
                    case "sendClient":
                        server.sendMessageToClient(Integer.parseInt(reader.readLine()), new jMessage(ConstantVariables.jMsgFlag.INFO, reader.readLine()));
                        break;
                    case "sendAll":
                        server.sendMessageToAll(new jMessage(ConstantVariables.jMsgFlag.INFO, reader.readLine()));
                        break;
                    case "count":
                        System.out.printf("Total connections: %d\n", server.getTotalConnections());
                        break;
                    case "disClient":
                        server.disconnectClient(Integer.parseInt(reader.readLine()));
                        break;
                    case "disAll":
                        server.disconnectAll();
                        break;
                    case "online":
                        LinkedList<String> lst = server.getConnectedClients();
                        for (String s : lst)
                            System.out.printf("%s; ", s);
                        System.out.println();
                        break;
                }
            } while (!key.equals("exit"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
