package org.jchat;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;
import java.util.logging.Logger;

class jListenerConnections extends Observable implements Runnable {

    private ServerSocket serverSocket;
    private Logger logger;

    @Override
    public void run() {
        try (ServerSocket serverSocket = jConfig.initServerSocketFromConfig("properties.config")) {
            logger = jLogger.initFileLogger(jServer.class.getName());
            this.serverSocket = serverSocket;
            notifyServer("Waiting for connections...");
            while (true) {
                Socket socket = serverSocket.accept();
                jClientConnection clientConnection = new jClientConnection(socket, logger);
                notifyServer(clientConnection);
            }
        } catch (SocketException se) {
            notifyServer("Waiting for connection was reset.");
        } catch (Exception e) {
            if (logger != null)
                logger.severe(e.toString());
            else
                e.printStackTrace();
        } finally {
            notifyServer("Server was stopped.");
        }
    }

    void stopListener() {
        if (this.serverSocket == null)
            return;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            logger.severe(e.toString());
        }
    }

    private void notifyServer(Object object) {
        setChanged();
        notifyObservers(object);
    }

}
