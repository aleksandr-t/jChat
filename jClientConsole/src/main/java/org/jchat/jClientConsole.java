package org.jchat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

public class jClientConsole {


    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Your nick: ");
            jClient client = new jClient(reader.readLine());
            String key;
            do {
                key = reader.readLine();
                switch (key) {
                    case "connect":
                        client.addObserver(new ConsoleOutputNotifyServer());
                        client.connect();
                        break;
                    case "disconnect":
                        client.disconnect();
                        break;
                    case "send":
                        client.sendMessageToAll(reader.readLine());
                        break;
                }
            } while (!key.equals("exit"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
