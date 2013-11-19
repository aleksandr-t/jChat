package com.jchat.clientside;

interface jChatClientAble {
    public void connect();

    public void disconnect();

    public void sendMessageToAll(String message) throws Exception;

    public String getNickName();
}
