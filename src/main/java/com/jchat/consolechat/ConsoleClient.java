package com.jchat.consolechat;

import com.jchat.clientside.jClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleClient {


    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Your nick: ");
            jClient client = new jClient(reader.readLine());
            String key;
            do {
                key = reader.readLine();
                switch (key) {
                    case "connect":
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
