package org.jchat;

import org.jchat.messages.jMessage;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class jMessageTest {

    private static jMessage message;
    private static jMessage messageNull;

    @BeforeClass
    public static void testInitMessages() {

        message = new jMessage("message");
        messageNull = new jMessage(null);
    }

    @Test
    public void testGetMessage() {

        assertEquals(message.getMessage(), "message");
        assertEquals(messageNull.getMessage(), "");
    }
}
