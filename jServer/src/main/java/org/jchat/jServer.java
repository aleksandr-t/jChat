package org.jchat;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

class jServer extends Observable implements Observer {

    private List<jClientConnection> clientConnections;
    private jListenerConnections listenerConnections;

    jServer() {
        this.clientConnections = new LinkedList<>();
    }

    void startServer() {
        this.listenerConnections = new jListenerConnections();
        new Thread(this.listenerConnections).start();
        this.listenerConnections.addObserver(this);
    }

    void stopServer() throws Exception {
        if (this.listenerConnections == null)
            return;
        this.disconnectAll();
        this.listenerConnections.stopListener();
    }

    synchronized private void addClientConnection(jClientConnection clientConnection) {
        this.clientConnections.add(clientConnection);
    }

    void disconnectClient(int idClient) throws Exception {
        this.disconnectClientConnection(this.clientConnections.get(idClient - 1));
    }

    private void disconnectClientConnection(jClientConnection clientConnection) throws Exception {
        clientConnection.stopConnection("Disconnect by admin");
    }

    synchronized void disconnectAll() throws Exception {
        int size = this.clientConnections.size();
        if (size == 0)
            return;
        jClientConnection clientConnection;
        for (int i = 0; i < size; i++) {
            clientConnection = this.clientConnections.get(0);
            this.disconnectClientConnection(clientConnection);
            this.clientConnections.remove(clientConnection);
        }
    }

    void sendMessageToClient(int idClient, jMessage msg) throws Exception {
        this.clientConnections.get(idClient - 1).sendMsg(msg);
    }

    public void sendMessageToAll(final jMessage msg) {
        for (jClientConnection clientConnection : this.clientConnections)
            clientConnection.sendMsg(msg);
    }

    private void sendMessageToClientConnection(jClientConnection clientConnection, jMessage msg) {
        clientConnection.sendMsg(msg);
    }

    synchronized void sendMessageToAll(String From, String message) {
        for (jClientConnection c : this.clientConnections) {
            if (!From.equals(c.getNickName()))
                this.sendMessageToClientConnection(c, new jMessage(jMsgFlags.MESSAGE, String.format("%s >> %s", From, message)));
        }
    }

    synchronized private boolean existsNickName(String nickName) {
        if (nickName == null || nickName.isEmpty())
            return true;
        for (jClientConnection c : this.clientConnections) {
            if (nickName.equals(c.getNickName()))
                return true;
        }
        return false;
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

    private void initClientConnection(jClientConnection clientConnection) {
        clientConnection.initConnection();
        String nickName = clientConnection.getNickName();
        if (this.existsNickName(nickName)) {
            clientConnection.stopConnection("Incorrect nickname or already exists");
        } else {
            this.sendMessageToAll(new jMessage(jMsgFlags.INFO, String.format("%s is online.", nickName)));
            this.addClientConnection(clientConnection);
            clientConnection.addObserver(this);
            new Thread(clientConnection).start();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        jMessage msg;
        if (o instanceof jClientConnection) {
            jClientConnection clientConnection = (jClientConnection) o;
            msg = (jMessage) arg;
            jMsgFlags jMsgFlags = msg.getTypeMessage();
            switch (jMsgFlags) {
                case DISCONNECT:
                    this.clientConnections.remove(clientConnection);
                    this.sendMessageToAll(new jMessage(jMsgFlags.INFO, String.format("%s is offline.", clientConnection.getNickName())));
                    break;
                case MESSAGE:
                    this.sendMessageToAll(clientConnection.getNickName(), msg.getMessage());
                    break;
            }
        } else {
            if (arg instanceof jClientConnection)
                this.initClientConnection((jClientConnection) arg);
            else {
                setChanged();
                notifyObservers(arg);
            }
        }
    }
}
