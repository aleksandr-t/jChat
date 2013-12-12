package org.jchat;

import org.jchat.messages.jMessage;
import org.jchat.messages.jMessageConnect;
import org.jchat.messages.jMessageDisconnect;
import org.jchat.messages.jMsgControl;

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
            if (jMsgControl.isMessageConnect(rawMessage))
                this.nickName = ((jMessage) rawMessage).getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        this.active = true;
        this.sendMsg(new jMessageConnect(String.format("Welcome to jChat, %s!", this.nickName)));

        while (this.active) {
            this.active = this.canReadFromSocket();
        }

        this.closeOpenedObjects();
        this.notifyServer(new jMessageDisconnect());

    }

    void sendMsg(jMessage msg) {
        try {
            this.outputStreamSocket.writeObject(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean canReadFromSocket() {
        boolean canRead = false;
        try {
            Object msg = this.inputStreamSocket.readObject();
            if (jMsgControl.isMessage(msg))
                notifyServer(msg);
            canRead = !jMsgControl.isMessageDisconnect(msg);
        } catch (SocketException | EOFException se) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return canRead;
    }

    void stopConnection(String reason) {
        try {
            this.sendMsg(new jMessageDisconnect(reason));
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

    private void notifyServer(Object object) {
        setChanged();
        notifyObservers(object);
    }
}

