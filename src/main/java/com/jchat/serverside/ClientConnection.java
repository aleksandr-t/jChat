package com.jchat.serverside;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

class ClientConnection extends Observable implements Runnable {

    final Socket _socket;
    final Thread thread;
    private final jChatServer _server;
    DataOutputStream _out;
    DataInputStream _in;
    private String _nickName;

    public ClientConnection(final jChatServer myServer, final Socket socket) {
        this._server = myServer;
        this._socket = socket;
        thread = new Thread(this);
    }

    public String getNickName() {
        return this._nickName;
    }

    void setNickName(final String nickName) {
        this._nickName = nickName;
    }

    @Override
    public void run() {

        try {
            String message;
            while (true) {
                message = this._in.readUTF();
                this._server.sendMessageToAll(this._nickName, message);
            }
        } catch (SocketException | EOFException se) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this._server.clientConnections.remove(this);
                this._server.sendMessageToAll(String.format("%s is offline", this._nickName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    void sendMessage(String message) throws Exception {
        if (this._out == null)
            throw new NullPointerException("OUT is null!\n");
        if (message == null || message.isEmpty())
            return;
        this._out.writeUTF(message);
    }

    public void stopConnection() {
        try {
            if (this._out != null)
                this._out.close();
            if (this._in != null)
                this._in.close();
            if (this._socket != null)
                this._socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
