package com.github.mandelV.IRCClient.Chat;

import com.github.mandelV.IRCClient.Parser.IRCMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Chat
 * @author VAUBOURG Mandel
 */
public class Chat extends Observable {

    private List<IRCMessage> messages;
    private static Chat instance = null;


    private Chat(){
        this.messages = new ArrayList<>();
    }


    static public Chat getInstance(){
        instance = (instance == null) ? new Chat() : instance;
        return instance;
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
