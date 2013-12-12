package org.jchat;

import org.jchat.messages.jMessage;

import java.util.Observable;
import java.util.Observer;

class jClient extends Observable implements Observer {

    private jServerConnection serverConnection;
    private String nickName;

    jClient(String nickName) {
        if (!this.isValidNickName(nickName))
            throw new IllegalArgumentException();
        this.nickName = nickName;
        this.serverConnection = new jServerConnection();
        this.serverConnection.addObserver(this);
    }

    private boolean isValidNickName(String nickName) {
        return !(nickName == null || nickName.isEmpty());
    }

    String getNickName() {
        return this.nickName;
    }

    boolean changeNickName(String nickName) {
        if (!this.isValidNickName(nickName) || this.isConnected())
            return false;
        this.nickName = nickName;
        return true;
    }

    void connect() {
        this.serverConnection.initConnection(this.nickName);
    }

    void disconnect() {
        this.serverConnection.stopConnection();
    }

    boolean isConnected() {
        return this.serverConnection.isActive();
    }

    void sendMessageToAll(String message) {
        this.serverConnection.sendMsg(new jMessage(message));
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}

