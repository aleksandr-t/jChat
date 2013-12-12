package org.jchat.messages;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class jMessageWarningTest {
    static jMessage message;
    static jMessage messageNull;

    @BeforeClass
    public static void testInitMessages() {
        message = new jMessageInfo("message");
        messageNull = new jMessageInfo(null);
    }

    @Test
    public void testGetTypeMessage() {
        assertEquals(message.getMessage(), "message");
        assertEquals(messageNull.getMessage(), "");
    }

    @Test
    public void testGetMessage() {
        assertTrue(message.equalsTypeMessage(jMsgFlags.INFO));
        assertTrue(messageNull.equalsTypeMessage(jMsgFlags.INFO));
    }
}
