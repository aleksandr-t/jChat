package org.jchat;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;


class jServerConnection extends Observable implements Runnable {

    private boolean active;
    private Socket socket;
    private ObjectOutputStream outputStreamSocket;
    private ObjectInputStream inputStreamSocket;
    private String nickName;

    jServerConnection(String nickName) {
        this.active = false;
        this.nickName = nickName;
    }

    void initConnection() {
        try {
            this.socket = jConfig.initSocketFromConfig(jConfig.loadConfig(this.getClass(), "properties.config"));
            this.outputStreamSocket = new ObjectOutputStream(socket.getOutputStream());
            this.inputStreamSocket = new ObjectInputStream(socket.getInputStream());
            new Thread(this).start();
        } catch (ConnectException connectException) {
            notifyServer(new jMessage(jMsgFlags.ERROR, "Unavailable connect to server"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            this.active = true;
            sendMsg(new jMessage(jMsgFlags.CONNECT, this.nickName));
            Object msg;
            while (this.active) {
                msg = this.inputStreamSocket.readObject();
                this.checkTypeMessage(msg);
            }
        } catch (SocketException | EOFException se) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeOpenedObjects();
            this.notifyServer(new jMessage(jMsgFlags.WARNING, "Server connection was loss"));
        }
    }

    private void checkTypeMessage(Object obj) {
        if (obj == null || !(obj instanceof jMessage)) {
            this.notifyServer(new jMessage(jMsgFlags.ERROR, "Unknown message"));
            return;
        }
        jMessage msg = (jMessage) obj;
        this.active = (msg.getTypeMessage() != jMsgFlags.DISCONNECT);
        this.notifyServer(msg);
    }

    void stopConnection() {
        try {
            if (this.active)
                this.sendMsg(new jMessage(jMsgFlags.DISCONNECT));
            this.closeOpenedObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeOpenedObjects() {
        this.closeInputStream();
        this.closeOutputStream();
        this.closeSocket();
        this.active = false;
    }

    private void closeSocket() {
        if (this.socket == null)
            return;
        try {
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeOutputStream() {
        if (this.outputStreamSocket != null) {
            try {
                this.outputStreamSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void closeInputStream() {
        if (this.inputStreamSocket != null) {
            try {
                this.inputStreamSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    boolean isActive() {
        return this.active;
    }

    void sendMsg(jMessage msg) {
        try {
            this.outputStreamSocket.writeObject(msg);
        } catch (Exception e) {
            this.notifyServer(new jMessage(jMsgFlags.ERROR, "Unavailable to send"));
        }
    }

    void sendMsg(String message) {
        this.sendMsg(new jMessage(jMsgFlags.MESSAGE, message));
    }

    private void notifyServer(Object object) {
        setChanged();
        notifyObservers(object);
    }
}
