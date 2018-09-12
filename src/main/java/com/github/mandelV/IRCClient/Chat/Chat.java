package com.github.mandelV.IRCClient.Chat;

import com.github.mandelV.IRCClient.Parser.IRCMessage;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Chat
 * @author VAUBOURG Mandel
 */
public class Chat extends Observable {

    private int nbrMessageDisplayed;
    List<IRCMessage> messages;
    private File logFile;
    private PrintWriter writer;
    private FileReader reader;

    private static Chat instance = null;



    private Chat(final int nbrMessageDisplayed){
        this.nbrMessageDisplayed = (nbrMessageDisplayed == 0) ? 1 : nbrMessageDisplayed;
        this.messages = new ArrayList<>();
        this.logFile = new File("chat");
    }


    synchronized static public Chat getInstance(final int nbrMessageDisplayed){
        instance = (instance == null) ? new Chat(nbrMessageDisplayed) : instance;
        return instance;
    }
    synchronized static public Chat getInstance(){
        instance = (instance == null) ? new Chat(10) : instance;
        return instance;
    }
    public void logMessage(final String msg){

    }

    public void logMessage(){
    }

   synchronized public void pushMessage(final IRCMessage msg){
        this.messages.add(msg);
        this.setChanged();
        this.notifyObservers(msg);

    }

    public List<IRCMessage> getMessages() {
        return messages;
    }

    public static void displayMsg(final String msg){
        System.out.println(msg);
    }
}
