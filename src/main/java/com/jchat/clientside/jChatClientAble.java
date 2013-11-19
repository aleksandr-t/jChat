package com.jchat.clientside;

interface jChatClientAble {
    public void connect() throws Exception;

    public void disconnect() throws Exception;

    public void sendMessageToAll(String message) throws Exception;

    public String getNickName();
}
