package com.jchat.clientside;

import com.jchat.ConstantVariables;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

class ServerConnection extends Observable implements Runnable {

    private final Thread _thread;
    private Socket _socket;
    private ObjectOutputStream _out;
    private boolean _isActive;
    private jChatClient _client;

    public ServerConnection(final jChatClient myClient) throws IOException {
        this._client = myClient;
        this._thread = new Thread(this);
        this._thread.start();
    }

    @Override
    public void run() {

        ObjectInputStream ois = null;
        try (Socket socket = new Socket(ConstantVariables.HOST, ConstantVariables.SERVER_PORT)) {
            this._isActive = true;
            this._socket = socket;
            String message;
            this._out = new ObjectOutputStream(this._socket.getOutputStream());
            sendMessage(this._client.getNickName());
            ois = new ObjectInputStream(socket.getInputStream());
            while (this._isActive) {
                message = (String) ois.readObject();
                setChanged();
                switch (message) {
                    case ConstantVariables.THIS_NICK_IS_ALREADY_USED:
                    case ConstantVariables.INCORRECT_NICK:
                    case ConstantVariables.KICK_ALL:
                    case ConstantVariables.KICK:
                        this._isActive = false;
                        notifyObservers(String.format("Connection was aborted by server. Reason: %s", message));
                        break;
                    default:
                        notifyObservers(message);
                        break;
                }
            }
        } catch (SocketException se) {

        } catch (EOFException ee) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            setChanged();
            notifyObservers("Server connection was loss");
            try {
                if (ois != null)
                    ois.close();
                if (this._out != null)
                    this._out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopConnection() {
        try {
            this._isActive = false;
            if (this._socket != null) {
                if (!this._socket.isClosed()) {
                    this._socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws Exception {
        if (this._out == null)
            throw new Exception("out is null");
        if (message == null || message.isEmpty())
            return;
        this._out.writeObject(message);
    }

}
