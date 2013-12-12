package org.jchat.messages;

public class jMessageConnect extends jMessage {

    public jMessageConnect(String message) {
        super(jMsgFlags.CONNECT, message);
    }
}
