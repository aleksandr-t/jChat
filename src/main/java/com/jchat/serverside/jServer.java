package com.jchat.serverside;

import com.jchat.ConstantVariables;
import com.jchat.jMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class jServer implements Observer {

    final List<ClientConnection> clientConnections;
    private ConnectionsListener connectionsListener;

    public jServer() {
        this.clientConnections = new LinkedList<>();
    }

    public void startServer() {
        this.connectionsListener = new ConnectionsListener(this);
        this.connectionsListener.addObserver(this);
    }


    public void stopServer() throws Exception {
        ConnectionsListener cl = this.connectionsListener;

        if (cl == null)
            return;
        disconnectAll();
        cl.stopListener();
    }

    public void disconnectClient(int idClient) throws Exception {

        this.disconnectClient(this.clientConnections.get(idClient - 1));
    }

    void disconnectClient(final ClientConnection clientConnection) throws Exception {

        clientConnection.sendMessage(new jMessage(ConstantVariables.jMsgFlag.DISCONNECT, "Disconnect"));
        clientConnection.thread.join(10000);
        if (clientConnection.thread.isAlive())
            clientConnection.stopConnection();
    }

    public void disconnectAll() throws Exception {
        int size = this.clientConnections.size();
        if (size == 0)
            return;
        for (int i = 0; i < size; i++)
            this.disconnectClient(this.clientConnections.get(0));
    }

    public void sendMessageToClient(int idClient, final jMessage msg) throws Exception {
        this.clientConnections.get(idClient - 1).sendMessage(msg);
    }

    public void sendMessageToAll(final jMessage msg) throws Exception {
        for (ClientConnection c : this.clientConnections)
            c.sendMessage(msg);
    }

    void sendMessageToClient(ClientConnection clientConnection, final jMessage msg) throws Exception {
        clientConnection.sendMessage(msg);
    }

    void sendMessageToAll(final String From, final String message) throws Exception {
        for (ClientConnection c : this.clientConnections) {
            if (!From.equals(c.getNickName()))
                this.sendMessageToClient(c, new jMessage(ConstantVariables.jMsgFlag.MESSAGE, String.format("%s >> %s", From, message)));
        }
    }

    public boolean existsNickName(String nickName) {
        for (ClientConnection c : this.clientConnections) {
            if (nickName.equals(c.getNickName()))
                return true;
        }
        return false;
    }

    public int getTotalConnections() {
        return this.clientConnections.size();
    }

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
