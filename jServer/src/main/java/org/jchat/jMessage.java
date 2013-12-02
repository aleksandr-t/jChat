package org.jchat;

import java.io.Serializable;

public class jMessage implements Serializable {

    private jMsgFlags typeMessage;
    private String message;

    public jMessage(jMsgFlags typeMessage, String message) {
        if (typeMessage == null || message == null)
            throw new IllegalArgumentException();
        this.typeMessage = typeMessage;
        this.message = message;
    }

    public jMessage(jMsgFlags typeMessage) {
        this(typeMessage, "");
    }

    public jMsgFlags getTypeMessage() {
        return this.typeMessage;
    }

    public String getMessage() {
        return this.message;
    }
}
