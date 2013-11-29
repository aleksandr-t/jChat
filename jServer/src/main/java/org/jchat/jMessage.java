package org.jchat;

import java.io.Serializable;

public class jMessage implements Serializable {

    private jConstants.jMsgFlag typeMessage;
    private String message;

    public jMessage(jConstants.jMsgFlag typeMessage, String message) {
        if (typeMessage == null || message == null)
            throw new IllegalArgumentException();
        this.typeMessage = typeMessage;
        this.message = message;
    }

    public jMessage(jConstants.jMsgFlag typeMessage) {
        this(typeMessage, "");
    }

    public jConstants.jMsgFlag getTypeMessage() {
        return this.typeMessage;
    }

    public String getMessage() {
       return  this.message;
    }
}
