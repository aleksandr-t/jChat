package com.jchat.serverside;

import com.jchat.ConstantVariables;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class jChatServer implements jChatServerAble, Observer {

    protected List<ClientConnection> clientConnections;
    protected ConnectionsListener connectionsListener;

    public jChatServer() throws Exception {
        this.clientConnections = new LinkedList<>();
    }


    @Override
    public void startServer() {
        this.connectionsListener = new ConnectionsListener(this);
        this.connectionsListener.addObserver(this);
    }

    @Override
    public void stopServer() throws Exception {
        ConnectionsListener cl = this.connectionsListener;

        if (cl == null)
            return;
        disconnectAll();
        cl.stopListener();
    }

    @Override
    public void disconnectClient(int idClient) throws Exception {

        this.disconnectClient(this.clientConnections.get(idClient - 1));
    }

    protected void disconnectClient(final ClientConnection clientConnection) throws Exception {

        clientConnection.sendMessage(ConstantVariables.KICK);
        clientConnection.stopConnection();
        clientConnection.thread.join();
    }

    @Override
    public void disconnectAll() throws Exception {
        int size = this.clientConnections.size();
        this.sendMessageToAll(ConstantVariables.KICK_ALL);
        for (int i = 0; i < size; i++) {
            this.clientConnections.get(0).stopConnection();
        }
    }

    @Override
    public void sendMessageToClient(int idClient, final String message) throws Exception {
        this.clientConnections.get(idClient - 1).sendMessage(message);
    }

    @Override
    public void sendMessageToAll(final String message) throws Exception {
        for (ClientConnection c : this.clientConnections) {
            this.sendMessageToClient(c, message);
        }
    }

    protected void sendMessageToClient(ClientConnection clientConnection, final String message) throws Exception {
        clientConnection.sendMessage(message);
    }

    protected void sendMessageToAll(final String From, final String message) throws Exception {
        for (ClientConnection c : this.clientConnections) {
            if (!From.equals(c.getNickName()))
                this.sendMessageToClient(c, String.format("%s >> %s", From, message));
        }
    }

    @Override
    public boolean existsNickName(String nickName) {
        for (ClientConnection c : this.clientConnections) {
            if (nickName.equals(c.getNickName()))
                return true;
        }
        return false;
    }

    @Override
    public int getTotalConnections() {
        return this.clientConnections.size();
    }

    @Override
    public LinkedList<String> getConnectedClients() {
        LinkedList<String> lst = new LinkedList<>();

        for (ClientConnection c : this.clientConnections)
            lst.add(c.getNickName());

        return lst;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg);
    }
}
