package com.github.mandelV.IRCClient;

import com.github.mandelV.IRCClient.Chat.Chat;
import com.github.mandelV.IRCClient.Client.IRCClient;

import java.util.Scanner;

/**
 * @author VAUBOURG Mandel
 */
public class IrcMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String message;
        //Instanciate Chat.
        Chat.getInstance();


        String serverAddress = (args.length > 0) ? args[0] : "localhost";
        String name = (args.length > 1) ? args[1] : "";
        String nickname = (args.length > 2) ? args[2] : "";
        String domain = (args.length > 3) ? args[3] : "";
        String channel = (args.length > 4) ? args[4] : "";






        //Client initialization final String channel, final String nickname, final String name, final String domain
        IRCClient client =  IRCClient.getInstance(serverAddress, 6667, channel, nickname, name, domain);

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
