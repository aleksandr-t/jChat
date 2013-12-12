package org.jchat.messages;

public class jMessageDisconnect extends jMessage {

    public jMessageDisconnect(String message) {
        super(jMsgFlags.DISCONNECT, message);
    }

    public jMessageDisconnect() {
        super(jMsgFlags.DISCONNECT, "");
    }
}
