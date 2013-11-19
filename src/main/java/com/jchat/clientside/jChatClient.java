package com.jchat.clientside;

import java.util.Observable;
import java.util.Observer;

public class jChatClient implements jChatClientAble, Observer {


    private ServerConnection _serverConnection;
    private final String _nickName;

    public String getNickName() {
        return this._nickName;
    }

    public jChatClient(final String nickName) {
        this._nickName = nickName;
    }

    @Override
    public void connect() {
        this._serverConnection = new ServerConnection(this);
        this._serverConnection.addObserver(this);
    }

    @Override
    public void disconnect() {
        this._serverConnection.stopConnection();
    }

    public void sendMessageToAll(String message) throws Exception {
        this._serverConnection.sendMessage(message);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg);
    }
}
