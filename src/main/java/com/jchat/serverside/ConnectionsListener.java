package com.jchat.serverside;

import com.jchat.ConstantVariables;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

class ConnectionsListener extends Observable implements Runnable {

    private ServerSocket _serverSocket;
    private final jChatServer _server;

    ConnectionsListener(final jChatServer server) {

        this._server = server;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(ConstantVariables.SERVER_PORT)) {
            this._serverSocket = serverSocket;
            setChanged();
            notifyObservers("Waiting connections...");
            while (true) {
                Socket socket = serverSocket.accept();
                ClientConnection clientConnection = new ClientConnection(this._server, socket);

                if (validateNickName(clientConnection)) {
                    clientConnection.addObserver(this._server);
                    clientConnection.thread.start();
                    this._server.clientConnections.add(clientConnection);
                }
            }
        } catch (SocketException se) {

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    int validateNickName(final String nickName) {
        if (nickName == null || nickName.isEmpty())
            return 1;
        else if (this._server.existsNickName(nickName))
            return 2;
        return 0;
    }

    private boolean validateNickName(ClientConnection clientConnection) throws Exception {

        clientConnection._in = new DataInputStream(clientConnection._socket.getInputStream());
        clientConnection._out = new DataOutputStream(clientConnection._socket.getOutputStream());


        String message = clientConnection._in.readUTF();
        int res = validateNickName(message);
        switch (res) {
            case 0:
                this._server.sendMessageToAll(String.format("%s is online!", message));
                clientConnection.setNickName(message);
                clientConnection.sendMessage(String.format("Welcome to jChat, %s!", message));
                return true;
            case 2:
                clientConnection.sendMessage(ConstantVariables.THIS_NICK_IS_ALREADY_USED);
                clientConnection.stopConnection();
                break;
            default:
                clientConnection.sendMessage(ConstantVariables.INCORRECT_NICK);
                clientConnection.stopConnection();
                break;
        }
        return false;
    }

    public void stopListener() throws Exception {
        if (this._serverSocket != null)
            if (!this._serverSocket.isClosed())
                this._serverSocket.close();
        setChanged();
        notifyObservers("Server was stopped...");
    }

}
