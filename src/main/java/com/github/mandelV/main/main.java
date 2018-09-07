package com.github.mandelV.main;



import java.util.Scanner;

public class main {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String message = "0";
        IRCClient server;

        String serverAddress = (args.length > 0) ? args[0] : "localhost";

        server = new IRCClient("localhost", 6667);
        Thread thread = new Thread(server);
        thread.start();

        while(true){

            message = scanner.nextLine();
            server.send("PRIVMSG #help :" + message);


        }

    }

}
