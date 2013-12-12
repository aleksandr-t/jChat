package org.jchat;

import java.util.Observable;
import java.util.Observer;

class ConsoleNotifyService implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg);
    }
}