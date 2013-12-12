package org.jchat;

import org.jchat.messages.jMessage;
import org.jchat.messages.jMessageInfo;
import org.jchat.messages.jMsgControl;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

class jServer extends Observable implements Observer {

    private List<jClientConnection> clientConnections;
    private jListenerConnections listenerConnections;
    private Thread threadListenerConnections;

    jServer() {
        this.clientConnections = new LinkedList<>();
        this.listenerConnections = new jListenerConnections();
        this.listenerConnections.addObserver(this);
        this.threadListenerConnections = new Thread(this.listenerConnections);
    }

    void startServer() {
        if (!this.threadListenerConnections.isAlive())
            this.threadListenerConnections.start();
    }

    void stopServer() {
        if (this.threadListenerConnections.isAlive())
            this.listenerConnections.stopListener();
        this.disconnectAll();
    }

    synchronized int getTotalConnections() {
        return this.clientConnections.size();
    }

    synchronized LinkedList<String> getConnectedClients() {
        LinkedList<String> lst = new LinkedList<>();

        for (jClientConnection c : this.clientConnections)
            lst.add(c.getNickName());

        return lst;
    }

    synchronized void disconnectClient(int idClient) {
        this.disconnectClientConnection(this.clientConnections.get(idClient - 1));
    }

    synchronized void disconnectAll() {
        for (jClientConnection clientConnection : this.clientConnections)
            this.disconnectClientConnection(clientConnection);
        this.clientConnections.clear();
    }

    private void disconnectClientConnection(jClientConnection clientConnection) {
        clientConnection.stopConnection("Disconnect by admin");
    }

    synchronized void sendMessageToClient(int idClient, String message) {
        this.clientConnections.get(idClient - 1).sendMsg(new jMessageInfo(message));
    }

    synchronized void sendMessageToAll(String message) {
        for (jClientConnection clientConnection : this.clientConnections)
            clientConnection.sendMsg(new jMessageInfo(message));
    }

    synchronized void sendMessageToAll(String From, String message) {
        for (jClientConnection c : this.clientConnections) {
            if (!From.equals(c.getNickName()))
                this.sendMessageToClientConnection(c, new jMessage(String.format("%s >> %s", From, message)));
        }
    }

    private void sendMessageToClientConnection(jClientConnection clientConnection, jMessage msg) {
        clientConnection.sendMsg(msg);
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        if (o instanceof jClientConnection) {
            jClientConnection clientConnection = (jClientConnection) o;

            if (jMsgControl.isMessageDisconnect(arg)) {
                this.clientConnections.remove(clientConnection);
                this.sendMessageToAll(String.format("%s is offline.", clientConnection.getNickName()));
            } else
                this.sendMessageToAll(clientConnection.getNickName(), arg.toString());
        } else {
            if (arg instanceof jClientConnection)
                this.initClientConnection((jClientConnection) arg);
            else {
                setChanged();
                notifyObservers(arg);
            }
        }
    }

    private void initClientConnection(jClientConnection clientConnection) {
        clientConnection.initConnection();
        if (this.existsNickName(clientConnection.getNickName()))
            clientConnection.stopConnection("Incorrect nickname or already exists");
        else {
            this.addNewClientConnection(clientConnection);
        }
    }

    private synchronized boolean existsNickName(String nickName) {
        if (nickName == null || nickName.isEmpty())
            return true;
        for (jClientConnection c : this.clientConnections)
            if (nickName.equals(c.getNickName()))
                return true;
        return false;
    }

    private synchronized void addNewClientConnection(jClientConnection clientConnection) {
        this.sendMessageToAll(String.format("%s is online.", clientConnection.getNickName()));
        clientConnection.addObserver(this);
        this.clientConnections.add(clientConnection);
        new Thread(clientConnection).start();
    }
}
