package org.jchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class jClientConsole {

    private static jClient client;
    private static ConsoleNotifyService notifyService;

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Your nick: ");

            String nickName = reader.readLine();
            initClient(nickName);

            processing(reader);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initClient(String nickName) {
        client = new jClient(nickName);
        notifyService = new ConsoleNotifyService();
        client.addObserver(notifyService);
    }

    private static void processing(BufferedReader reader) throws IOException {
        String key;
        do {
            key = reader.readLine();
            switch (key) {
                case "connect":
                    client.connect();
                    break;
                case "exit":
                case "disconnect":
                    client.disconnect();
                    break;
                case "send":
                    client.sendMessageToAll(reader.readLine());
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        } while (!key.equals("exit"));
    }
}
