package org.jchat.messages;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class jMessageConnectTest {

    private static jMessage message;
    private static jMessage messageNull;

    @BeforeClass
    public static void testInitMessages() {
        message = new jMessageConnect("message");
        messageNull = new jMessageConnect(null);
    }

    @Test
    public void testGetTypeMessage() {
        assertEquals(message.getMessage(), "message");
        assertEquals(messageNull.getMessage(), "");
    }

    @Test
    public void testGetMessage() {
        assertTrue(message.equalsTypeMessage(jMsgFlags.CONNECT));
        assertTrue(messageNull.equalsTypeMessage(jMsgFlags.CONNECT));
    }
}
