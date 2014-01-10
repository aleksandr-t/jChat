package org.jchat;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class jServerConsole {

    private static jServer server;
    private static ConsoleNotifyService notifyService;

    public static void main(String[] args) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            initServer();
            processing(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initServer() {
        server = new jServer();
        notifyService = new ConsoleNotifyService();
        server.addObserver(notifyService);
    }

    private static void processing(BufferedReader reader) throws Exception {
        String key;
        do {
            key = reader.readLine();
            switch (key) {
                case "start":
                    server.startServer();
                    break;
                case "exit":
                case "stop":
                    server.stopServer();
                    break;
                case "sendClient":
                    server.sendMessageToClient(Integer.parseInt(reader.readLine()), reader.readLine());
                    break;
                case "sendAll":
                    server.sendMessageToAll(reader.readLine());
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
                    System.out.println(server.getConnectedClients());
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        } while (!key.equals("exit"));
    }


}
