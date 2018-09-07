package com.github.mandelV.IRCClient;

import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        String serverAddress = (args.length > 0) ? args[0] : "localhost";

        Scanner scanner = new Scanner(System.in);
        String message = "0";

        //Client initialization
        IRCClient client = new IRCClient(serverAddress, 6667);
        Thread thread = new Thread(client);
        thread.start();


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
