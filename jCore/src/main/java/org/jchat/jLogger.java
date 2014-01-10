package org.jchat;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

class jLogger {

    static Logger initFileLogger(String name) throws IOException {

        Logger logger = Logger.getLogger(name);
        FileHandler fileHandler = new FileHandler("log.log", true);
        logger.addHandler(fileHandler);

        return logger;
    }

    static Logger initConsoleLogger(String name) {
        Logger logger = Logger.getLogger(name);
        ConsoleHandler consoleHandler = new ConsoleHandler();

        logger.addHandler(consoleHandler);

        return logger;
    }

}
