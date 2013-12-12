package org.jchat.messages;

public class jMessageError extends jMessage {

    public jMessageError(String message) {
        super(jMsgFlags.ERROR, message);
    }
}
