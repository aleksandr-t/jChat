package org.jchat.messages;

public class jMsgControl {

    public static boolean isMessage(Object object) {
        return (object instanceof jMessage);
    }

    public static boolean isMessageConnect(Object object) {
        return isMessage(object) && ((jMessage) object).equalsTypeMessage(jMsgFlags.CONNECT);
    }

    public static boolean isMessageDisconnect(Object object) {
        return isMessage(object) && ((jMessage) object).equalsTypeMessage(jMsgFlags.DISCONNECT);
    }

    public static boolean isMessageError(Object object) {
        return isMessage(object) && ((jMessage) object).equalsTypeMessage(jMsgFlags.ERROR);
    }

    public static boolean isMessageWarning(Object object) {
        return isMessage(object) && ((jMessage) object).equalsTypeMessage(jMsgFlags.WARNING);
    }

    public static boolean isMessageInfo(Object object) {
        return isMessage(object) && ((jMessage) object).equalsTypeMessage(jMsgFlags.INFO);
    }
}
