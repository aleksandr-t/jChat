package com.jchat.serverside;


import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

class ClientConnection extends Observable implements Runnable {

    private final jChatServer _server;
    final Socket _socket;
    ObjectOutputStream _out;
    ObjectInputStream _in;
    private String _nickName;
    final Thread thread;

    public ClientConnection(final jChatServer myServer, final Socket socket) {
        this._server = myServer;
        this._socket = socket;
        thread = new Thread(this);
    }

    public String getNickName() {
        return this._nickName;
    }

    int setNickName(final String nickName) {
        if (nickName == null || nickName.isEmpty())
            return 1;
        else if (this._server.existsNickName(nickName))
            return 2;
        this._nickName = nickName;

        return 0;
    }

    @Override
    public void run() {

        try {
            String message;
            while (true) {
                message = (String) this._in.readObject();
                this._server.sendMessageToAll(this._nickName, message);
            }
        } catch (SocketException | EOFException se) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (this._out != null)
                    this._out.close();
                if (this._in != null)
                    this._in.close();
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
        this._out.writeObject(message);
    }

    public void stopConnection() {
        try {
            if (this._socket != null) {
                if (!this._socket.isClosed())
                    this._socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
