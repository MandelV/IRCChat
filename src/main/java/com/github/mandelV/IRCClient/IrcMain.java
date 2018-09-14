package com.github.mandelV.IRCClient;

import com.github.mandelV.IRCClient.Chat.Chat;
import com.github.mandelV.IRCClient.Client.IRCClient;
import com.github.mandelV.IRCClient.Parser.IRCParser;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author VAUBOURG Mandel
 */
public class IrcMain {

    public static void main(String[] args) throws IOException {
        if(args.length < 4) return;

        /*Scanner scanner = new Scanner(System.in);
        String message;
        //Instanciate Chat.
        Chat.getInstance();


        //Client initialization final String channel, final String nickname, final String name, final String domain
        IRCClient client =  IRCClient.getInstance(args[0], 6667, args[4], args[2], args[1], args[3]);

        Thread thread = new Thread(client);
        thread.start();

        while(client.isStop()){
            message = scanner.nextLine();

            if(message.startsWith("/")){
                message = message.substring(1);
                client.send(message);
            }else{
                client.send("PRIVMSG " + client.getChannel() + " :" + message);
            }
        }*/

        //IRCParser.GrammarParse("@tag1=value1;va :test!USER@DOMAINE JOIN arg arg ar :Fff !");
        IRCParser.GrammarParse("@tag=aa;bla=d;test=test;v=5 :bla!bouch@127.0.0 OIN #arg1 arg :dkkkk");
        //IRCParser.GrammarParse("@tag=aa;bla=d;test=test;v=5 CMD #arg1 arg :dkkkk");
    }
}
