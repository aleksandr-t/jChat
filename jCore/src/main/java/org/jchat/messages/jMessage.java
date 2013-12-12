package org.jchat.messages;

import java.io.Serializable;

public class jMessage implements Serializable {

    private jMsgFlags typeMessage;
    private String message;

    protected jMessage(jMsgFlags typeMessage, String message) {
        this.typeMessage = typeMessage;
        this.message = (message == null) ? "" : message;
    }

    public jMessage(String message) {
        this(jMsgFlags.MESSAGE, message);
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return this.getMessage();
    }

    protected boolean equalsTypeMessage(jMsgFlags typeMessage) {
        return (this.typeMessage == typeMessage);
    }
}
