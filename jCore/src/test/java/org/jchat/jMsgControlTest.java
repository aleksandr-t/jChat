package org.jchat;

import org.jchat.messages.*;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class jMsgControlTest {

    @Test
    public void testNullObject() {
        Object object = null;

        assertFalse(jMsgControl.isMessage(object));
        assertFalse(jMsgControl.isMessageConnect(object));
        assertFalse(jMsgControl.isMessageDisconnect(object));
        assertFalse(jMsgControl.isMessageError(object));
        assertFalse(jMsgControl.isMessageWarning(object));
        assertFalse(jMsgControl.isMessageInfo(object));
    }

    @Test
    public void testIsMessage() {
        Object object = new jMessage("message");

        assertTrue(jMsgControl.isMessage(object));

        assertFalse(jMsgControl.isMessageConnect(object));
        assertFalse(jMsgControl.isMessageDisconnect(object));
        assertFalse(jMsgControl.isMessageError(object));
        assertFalse(jMsgControl.isMessageWarning(object));
        assertFalse(jMsgControl.isMessageInfo(object));
    }

    @Test
    public void testIsMessageDisconnect() {
        Object object = new jMessageDisconnect("disconnect");

        assertTrue(jMsgControl.isMessage(object));
        assertTrue(jMsgControl.isMessageDisconnect(object));

        assertFalse(jMsgControl.isMessageConnect(object));
        assertFalse(jMsgControl.isMessageError(object));
        assertFalse(jMsgControl.isMessageWarning(object));
        assertFalse(jMsgControl.isMessageInfo(object));
    }

    @Test
    public void testIsMessageConnect() {
        Object object = new jMessageConnect("connect");

        assertTrue(jMsgControl.isMessage(object));
        assertTrue(jMsgControl.isMessageConnect(object));

        assertFalse(jMsgControl.isMessageDisconnect(object));
        assertFalse(jMsgControl.isMessageError(object));
        assertFalse(jMsgControl.isMessageWarning(object));
        assertFalse(jMsgControl.isMessageInfo(object));
    }

    @Test
    public void testIsMessageWarning() {
        Object object = new jMessageWarning("warning");

        assertTrue(jMsgControl.isMessage(object));
        assertTrue(jMsgControl.isMessageWarning(object));

        assertFalse(jMsgControl.isMessageDisconnect(object));
        assertFalse(jMsgControl.isMessageConnect(object));
        assertFalse(jMsgControl.isMessageError(object));
        assertFalse(jMsgControl.isMessageInfo(object));
    }

    @Test
    public void testIsMessageError() {
        Object object = new jMessageError("error");

        assertTrue(jMsgControl.isMessage(object));
        assertTrue(jMsgControl.isMessageError(object));

        assertFalse(jMsgControl.isMessageDisconnect(object));
        assertFalse(jMsgControl.isMessageConnect(object));
        assertFalse(jMsgControl.isMessageWarning(object));
        assertFalse(jMsgControl.isMessageInfo(object));
    }

    @Test
    public void testIsMessageInfo() {
        Object object = new jMessageInfo("info");

        assertTrue(jMsgControl.isMessage(object));
        assertTrue(jMsgControl.isMessageInfo(object));

        assertFalse(jMsgControl.isMessageDisconnect(object));
        assertFalse(jMsgControl.isMessageConnect(object));
        assertFalse(jMsgControl.isMessageError(object));
        assertFalse(jMsgControl.isMessageWarning(object));
    }

}
