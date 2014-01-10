package org.jchat;

import org.jchat.messages.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;
import java.util.logging.Logger;


class jServerConnection extends Observable implements Runnable {

    private boolean active;
    private Socket socket;
    private ObjectOutputStream outputStreamSocket;
    private ObjectInputStream inputStreamSocket;
    private String nickName;
    private Logger logger;

    void initConnection(String nickName) {
        if (this.active)
            return;
        this.nickName = nickName;
        try {
            logger = jLogger.initFileLogger(jClient.class.getName());
            this.socket = jConfig.initSocketFromConfig("properties.config");
            this.outputStreamSocket = new ObjectOutputStream(socket.getOutputStream());
            this.inputStreamSocket = new ObjectInputStream(socket.getInputStream());
            this.active = true;
            new Thread(this).start();
        } catch (ConnectException connectException) {
            this.closeOpenedObjects();
            notifyServer(new jMessageError("Unavailable connect to server"));
        } catch (Exception e) {
            if (logger != null)
                logger.severe(e.toString());
            else
                e.printStackTrace();
            this.closeOpenedObjects();
        }
    }

    @Override
    public void run() {

        sendMsg(new jMessageConnect(this.nickName));

        while (this.active) {
            this.active = canReadFromSocket();
        }

        this.closeOpenedObjects();
        this.notifyServer(new jMessageWarning("Server connection was loss"));
    }

    void sendMsg(jMessage msg) {
        try {
            this.outputStreamSocket.writeObject(msg);
        } catch (Exception e) {
            this.notifyServer(new jMessageError("Unavailable to send"));
        }
    }

    private boolean canReadFromSocket() {
        boolean canRead = false;
        try {
            Object msg = this.inputStreamSocket.readObject();
            if (jMsgControl.isMessage(msg))
                notifyServer(msg);
            canRead = !jMsgControl.isMessageDisconnect(msg);
        } catch (SocketException se) {
            this.notifyServer(new jMessageInfo("Disconnecting..."));
        } catch (Exception e) {
            logger.severe(e.toString());
        }
        return canRead;
    }

    void stopConnection() {
        this.closeOpenedObjects();
    }

    private void closeOpenedObjects() {
        this.closeInputStream();
        this.closeOutputStream();
        this.closeSocket();
    }

    private void closeInputStream() {
        if (this.inputStreamSocket == null)
            return;
        try {
            this.inputStreamSocket.close();
        } catch (Exception e) {
            logger.severe(e.toString());
        }
    }

    private void closeOutputStream() {
        if (this.outputStreamSocket == null)
            return;
        try {
            this.outputStreamSocket.close();
        } catch (Exception e) {
            logger.severe(e.toString());
        }
    }

    private void closeSocket() {
        if (this.socket == null)
            return;
        try {
            this.socket.close();
        } catch (Exception e) {
            logger.severe(e.toString());
        }
    }

    boolean isActive() {
        return this.active;
    }

    private void notifyServer(Object object) {
        setChanged();
        notifyObservers(object);
    }
}
