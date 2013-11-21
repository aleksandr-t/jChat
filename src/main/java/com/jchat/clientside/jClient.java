package com.jchat.clientside;

import java.util.Observable;
import java.util.Observer;

public class jClient implements Observer {


    private ServerConnection _serverConnection;
    private final String _nickName;

    public String getNickName() {
        return this._nickName;
    }

    public jClient(final String nickName) {
        this._nickName = nickName;
    }

    public void connect() {
        this._serverConnection = new ServerConnection(this);
        this._serverConnection.addObserver(this);
    }

    public void disconnect() {
        this._serverConnection.stopConnection();
    }

    public void sendMessageToAll(String message) throws Exception {
        this._serverConnection.sendMsg(message);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg);
    }
}
