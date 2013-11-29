package org.jchat;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;

class jListenerConnections extends Observable implements Runnable {

    private ServerSocket serverSocket;

    @Override
    public void run() {
        try (ServerSocket serverSocket = jConfig.initServerSocketFromConfig(jConfig.loadConfig(this.getClass(), "properties.config"))) {
            this.serverSocket = serverSocket;
            notifyServer("Waiting for connections...");
            while (true) {
                Socket socket = serverSocket.accept();
                jClientConnection clientConnection = new jClientConnection(socket);
                notifyServer(clientConnection);
            }
        } catch (SocketException se) {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            notifyServer("Server was stopped...");
        }
    }

    void stopListener() {
        if (this.serverSocket == null)
            return;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void notifyServer(Object object) {
        setChanged();
        notifyObservers(object);
    }

}
