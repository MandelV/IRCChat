package com.github.mandelV.IRCClient.Chat;

import com.github.mandelV.IRCClient.Parser.IRCMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class ChatDisplay extends Observable {

    private int nbrMessageDisplayed;
    List<String> messages;
    private File logFile;
    private PrintWriter writer;
    private FileReader reader;

    private static ChatDisplay instance = null;


    private ChatDisplay(final int nbrMessageDisplayed){
        this.nbrMessageDisplayed = (nbrMessageDisplayed == 0) ? 1 : nbrMessageDisplayed;
        this.messages = new ArrayList<>();
        this.logFile = new File("chat");
    }

    synchronized static public ChatDisplay getInstance(final int nbrMessageDisplayed){
        instance = (instance == null) ? new ChatDisplay(nbrMessageDisplayed) : instance;
        return instance;
    }
    synchronized static public ChatDisplay getInstance(){
        instance = (instance == null) ? new ChatDisplay(10) : instance;
        return instance;
    }
    public void logMessage(final String msg){

    }

    public void logMessage(){
    }

    public void pushMessage(final String msg){
        this.messages.add(msg);
        this.notifyObservers(msg);
    }

    public void displayChat(){
        this.messages.forEach(msg -> System.out.println(msg));
    }
}
