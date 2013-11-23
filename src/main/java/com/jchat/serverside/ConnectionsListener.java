package com.jchat.serverside;

import com.jchat.ConstantVariables;
import com.jchat.jMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

class ConnectionsListener extends Observable implements Runnable {

    private ServerSocket _serverSocket;
    private final jServer _server;

    ConnectionsListener(final jServer server) {

        this._server = server;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(ConstantVariables.SERVER_PORT)) {
            this._serverSocket = serverSocket;
            setChanged();
            notifyObservers("Waiting for connections...");
            while (true) {
                Socket socket = serverSocket.accept();
                ClientConnection clientConnection = new ClientConnection(this._server, socket);

                if (validateConnection(clientConnection)) {
                    clientConnection.addObserver(this._server);
                    clientConnection.thread.start();
                    this._server.addConnection(clientConnection);
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

    private boolean validateConnection(ClientConnection clientConnection) {

        try {

            clientConnection._in = new ObjectInputStream(clientConnection._socket.getInputStream());
            clientConnection._out = new ObjectOutputStream(clientConnection._socket.getOutputStream());


            jMessage msg = ((jMessage) clientConnection._in.readObject());
            msg.check();

            if (msg.getMessage() != ConstantVariables.jMsgFlag.CONNECT) {
                clientConnection.sendMessage(new jMessage(ConstantVariables.jMsgFlag.DISCONNECT, "Bad format connecting"));
                clientConnection.stopConnection();
                return false;
            }

            int res = validateNickName(msg.toString());
            switch (res) {
                case 0:
                    this._server.sendMessageToAll(new jMessage(ConstantVariables.jMsgFlag.INFO, String.format("%s is online!", msg)));
                    clientConnection.setNickName(msg.toString());
                    clientConnection.sendMessage(new jMessage(ConstantVariables.jMsgFlag.CONNECT, String.format("Welcome to jChat, %s!", msg)));
                    return true;
                case 2:
                    clientConnection.sendMessage(new jMessage(ConstantVariables.jMsgFlag.DISCONNECT, "This nick already used"));
                    clientConnection.stopConnection();
                    break;
                default:
                    clientConnection.sendMessage(new jMessage(ConstantVariables.jMsgFlag.DISCONNECT, "Incorrect nick"));
                    clientConnection.stopConnection();
                    break;
            }
        } catch (Exception e) {

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
