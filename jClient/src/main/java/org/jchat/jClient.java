package org.jchat;


import java.util.Observable;
import java.util.Observer;

public class jClient extends Observable implements Observer {


    private jServerConnection serverConnection;
    private String nickName;

    public jClient(String nickName) {
        if (nickName == null)
            throw new IllegalArgumentException();
        this.nickName = nickName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public boolean changeNickName(String nickName) {
        if (nickName == null || this.serverConnection.isActive())
            return false;

        this.nickName = nickName;
        return true;
    }

    public void connect() {
        this.serverConnection = new jServerConnection(this.nickName);
        new Thread(this.serverConnection).start();
        this.serverConnection.addObserver(this);
    }

    public void disconnect() {
        this.serverConnection.stopConnection();
    }

    public void sendMessageToAll(String message) throws Exception {
        this.serverConnection.sendMsg(message);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}

