package org.jchat;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class jClientTest {

    private static jClient client;

    @BeforeClass
    public static void testCreateClient() {
        client = new jClient("Tester");
        assertEquals("Tester", client.getNickName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullClient() {
        jClient client = new jClient(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyClient() {
        jClient client1 = new jClient("");
    }

    @Test
    public void testChangeNickName() {
        String nickName = "tester 2";

        assertTrue(client.changeNickName(nickName));
        assertEquals(nickName, client.getNickName());
    }

    @Test
    public void testChangeNullNickName() {
        String nickName = client.getNickName();

        assertFalse(client.changeNickName(null));
        assertEquals(nickName, client.getNickName());
    }

    @Test
    public void testChangeEmptyNickName() {
        String nickName = client.getNickName();

        assertFalse(client.changeNickName(""));
        assertEquals(nickName, client.getNickName());
    }


    @Test
    public void testConnect() {
        client.connect();
        client.sendMessageToAll("");
    }

    @Test
    public void testDisconnect() {

    }

    @Test
    public void testSendMessageToAll() {

    }

    @Test
    public void testUpdate() {

    }
}
