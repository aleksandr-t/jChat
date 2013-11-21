package com.jchat.serverside;


import com.jchat.ConstantVariables;
import com.jchat.jMessage;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

class ClientConnection extends Observable implements Runnable {

    final Socket _socket;
    final Thread thread;
    private final jServer _server;
    boolean isActive;
    ObjectOutputStream _out;
    ObjectInputStream _in;
    private String _nickName;

    public ClientConnection(final jServer myServer, final Socket socket) {
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
            this.isActive = true;
            jMessage msg;
            while (this.isActive) {
                msg = (jMessage) this._in.readObject();
                msg.check();
                switch (msg.getMessage()) {
                    case DISCONNECT:
                        this.isActive = false;
                        break;
                    case CORRUPTED:
                        this.sendMessage(new jMessage(ConstantVariables.jMsgFlag.WARNING, "Message was corrupted while getting by server"));
                        break;
                    default:
                        this._server.sendMessageToAll(this._nickName, msg.toString());
                        break;
                }
            }
            this.stopConnection();
        } catch (SocketException | EOFException se) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this._server.clientConnections.remove(this);
                this._server.sendMessageToAll(new jMessage(ConstantVariables.jMsgFlag.INFO, String.format("%s is offline", this._nickName)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    void sendMessage(final jMessage msg) throws Exception {

        if (!this.isActive || this._out == null || msg == null) {
            setChanged();
            notifyObservers("Sending is unavailable");
        }

        this._out.writeObject(msg);
    }

    public void stopConnection() {
        try {
            this.isActive = false;
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
