package org.jchat.messages;

public class jMessageWarning extends jMessage {

    public jMessageWarning(String message) {
        super(jMsgFlags.WARNING, message);
    }
}
