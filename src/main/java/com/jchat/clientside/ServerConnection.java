package com.jchat.clientside;

import com.jchat.ConstantVariables;
import com.jchat.jMessage;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;


class ServerConnection extends Observable implements Runnable {

    private final jClient _client;
    public boolean isActive;
    private Socket _socket;
    private ObjectOutputStream _out;

    public ServerConnection(final jClient myClient) {
        this._client = myClient;
        this.isActive = false;
        new Thread(this).start();
    }

    @Override
    public void run() {

        try (Socket socket = new Socket(ConstantVariables.HOST, ConstantVariables.SERVER_PORT);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {

            this.isActive = true;
            this._socket = socket;
            this._out = objectOutputStream;


            sendMsg(new jMessage(ConstantVariables.jMsgFlag.CONNECT, this._client.getNickName()));
            jMessage msg;
            while (this.isActive) {
                msg = (jMessage) objectInputStream.readObject();
                msg.check();
                setChanged();
                switch (msg.getMessage()) {
                    case DISCONNECT:
                        this.isActive = false;
                        notifyObservers(String.format("Connection was aborted by server. Reason: %s", msg));
                        break;
                    case CORRUPTED:
                        notifyObservers("Message from server was corrupted while getting");
                        break;
                    default:
                        notifyObservers(msg);
                        break;
                }
            }
        } catch (SocketException | EOFException se) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            setChanged();
            notifyObservers("Server connection was loss");
        }
    }

    public void stopConnection() {
        try {
            this.sendMsg(new jMessage(null));
            this.isActive = false;
            if (this._socket != null && !this._socket.isClosed()) {
                this._socket.shutdownOutput();
                this._socket.shutdownInput();
                this._socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(final jMessage msg) throws Exception {

        if (!this.isActive || this._out == null || msg == null) {
            setChanged();
            notifyObservers("Unavailable to send");
        }

        this._out.writeObject(msg);
    }

    public void sendMsg(final String msg) throws Exception {
        this.sendMsg(new jMessage(ConstantVariables.jMsgFlag.MESSAGE, msg));
    }
}
