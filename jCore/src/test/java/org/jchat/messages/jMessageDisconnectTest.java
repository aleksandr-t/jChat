package org.jchat.messages;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class jMessageDisconnectTest {
    private static jMessage message;
    private static jMessage messageNull;
    private static jMessage messageEmpty;

    @BeforeClass
    public static void testInitMessages() {
        message = new jMessageDisconnect("message");
        messageNull = new jMessageDisconnect(null);
        messageEmpty = new jMessageDisconnect();
    }

    @Test
    public void testGetTypeMessage() {
        assertEquals(message.getMessage(), "message");
        assertEquals(messageNull.getMessage(), "");
        assertEquals(messageEmpty.getMessage(), "");
    }

    @Test
    public void testGetMessage() {
        assertTrue(message.equalsTypeMessage(jMsgFlags.DISCONNECT));
        assertTrue(messageNull.equalsTypeMessage(jMsgFlags.DISCONNECT));
        assertTrue(messageEmpty.equalsTypeMessage(jMsgFlags.DISCONNECT));
    }
}
