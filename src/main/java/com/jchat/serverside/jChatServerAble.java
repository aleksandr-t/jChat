package com.jchat.serverside;

import java.util.List;

interface jChatServerAble {
    public void startServer();

    public void stopServer() throws Exception;

    public void disconnectClient(int idClient) throws Exception;

    public void disconnectAll() throws Exception;

    public void sendMessageToClient(int idClient, final String message) throws Exception;

    public void sendMessageToAll(final String message) throws Exception;

    public boolean existsNickName(final String nick);

    public int getTotalConnections();

    public List<String> getConnectedClients();
}
