package org.jchat;


import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

class jClientConnection extends Observable implements Runnable {

    private boolean active;
    private Socket socket;
    private ObjectOutputStream outputStreamSocket;
    private ObjectInputStream inputStreamSocket;
    private String nickName;

    jClientConnection(Socket socket) {
        this.socket = socket;
    }

    String getNickName() {
        return this.nickName;
    }

    void initConnection() {
        try {
            this.inputStreamSocket = new ObjectInputStream(this.socket.getInputStream());
            this.outputStreamSocket = new ObjectOutputStream(this.socket.getOutputStream());
            Object rawMessage = this.inputStreamSocket.readObject();
            if (this.isConnectMessage(rawMessage))
                this.nickName = ((jMessage) rawMessage).getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        this.active = true;
        this.sendMsg(new jMessage(jConstants.jMsgFlag.CONNECT, String.format("Welcome to jChat, %s!", this.nickName)));
        Object msg;
        try {
            while (this.active) {
                msg = this.inputStreamSocket.readObject();
                this.checkingTypeMessage(msg);
            }
        } catch (SocketException | EOFException se) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeOpenedObjects();
            setChanged();
            notifyObservers(new jMessage(jConstants.jMsgFlag.DISCONNECT));
        }
    }

    void sendMsg(jMessage msg) {
        try {
            this.outputStreamSocket.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isMessage(Object message) {
        return (message instanceof jMessage);
    }

    private boolean isConnectMessage(Object connectMessage) {
        if (!this.isMessage(connectMessage))
            return false;
        return ((jMessage) connectMessage).getTypeMessage() == jConstants.jMsgFlag.CONNECT;
    }

    private void checkingTypeMessage(Object message) {
        if (!this.isMessage(message))
            return;
        jMessage msg = (jMessage) message;
        this.active = (msg.getTypeMessage() != jConstants.jMsgFlag.DISCONNECT);
        setChanged();
        notifyObservers(msg);
    }

    void stopConnection(String reason) {
        try {
            this.sendMsg(new jMessage(jConstants.jMsgFlag.DISCONNECT, reason));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.closeOpenedObjects();
    }

    private void closeOpenedObjects() {
        this.closeInputStream();
        this.closeOutputStream();
        this.closeSocket();
        this.active = false;
    }

    private void closeOutputStream() {
        if (this.outputStreamSocket == null)
            return;
        try {
            this.outputStreamSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeInputStream() {
        if (this.inputStreamSocket == null)
            return;
        try {
            this.inputStreamSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeSocket() {
        if (this.socket == null)
            return;
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

