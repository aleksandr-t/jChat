package com.jchat.clientside;

import com.jchat.ConstantVariables;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

class ServerConnection extends Observable implements Runnable {

    private final jChatClient _client;
    public boolean isActive;
    private Socket _socket;
    private DataOutputStream _out;

    public ServerConnection(final jChatClient myClient) {
        this._client = myClient;
        this.isActive = false;
        new Thread(this).start();
    }

    @Override
    public void run() {

        try (Socket socket = new Socket(ConstantVariables.HOST, ConstantVariables.SERVER_PORT);
             DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {

            this.isActive = true;
            this._socket = socket;
            this._out = dataOutputStream;


            sendMessage(this._client.getNickName());
            String message;
            while (this.isActive) {
                message = dataInputStream.readUTF();
                setChanged();
                switch (message) {
                    case ConstantVariables.THIS_NICK_IS_ALREADY_USED:
                    case ConstantVariables.INCORRECT_NICK:
                    case ConstantVariables.KICK_ALL:
                    case ConstantVariables.KICK:
                        this.isActive = false;
                        notifyObservers(String.format("Connection was aborted by server. Reason: %s", message));
                        break;
                    default:
                        notifyObservers(message);
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

    public void sendMessage(String message) throws Exception {

        if (!this.isActive || this._out == null)
            throw new Exception("out is null");
        if (message == null || message.isEmpty())
            return;
        this._out.writeUTF(message);
    }

}
