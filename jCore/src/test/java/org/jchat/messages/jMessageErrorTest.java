package org.jchat.messages;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class jMessageErrorTest {
    static jMessage message;
    static jMessage messageNull;

    @BeforeClass
    public static void testInitMessages() {
        message = new jMessageError("message");
        messageNull = new jMessageError(null);
    }

    @Test
    public void testGetTypeMessage() {
        assertEquals(message.getMessage(), "message");
        assertEquals(messageNull.getMessage(), "");
    }

    @Test
    public void testGetMessage() {
        assertTrue(message.equalsTypeMessage(jMsgFlags.ERROR));
        assertTrue(messageNull.equalsTypeMessage(jMsgFlags.ERROR));
    }
}
