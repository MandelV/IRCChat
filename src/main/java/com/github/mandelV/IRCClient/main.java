package com.github.mandelV.IRCClient;

import com.github.mandelV.IRCClient.Chat.ChatDisplay;
import com.github.mandelV.IRCClient.Client.IRCClient;

import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        String serverAddress = (args.length > 0) ? args[0] : "localhost";

        Scanner scanner = new Scanner(System.in);
        String message = "0";

        //Client initialization
        IRCClient client =  IRCClient.getInstance(serverAddress, 6667);
        Thread thread = new Thread(client);
        thread.start();

        ChatDisplay chatDisplay =  ChatDisplay.getInstance(5);


        while(!client.isStop()){
            message = scanner.nextLine();
            if(message.startsWith("/")){
                message = message.substring(1);
                client.send(message);
            }else{
                client.send("PRIVMSG " + client.getChannel() + " :" + message);
            }
        }
    }
}
