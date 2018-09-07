package com.github.mandelV.IRCClient;

import com.github.mandelV.IRCClient.Parser.IRCMessage;
import com.github.mandelV.IRCClient.Parser.IRCParser;
import com.github.mandelV.IRCClient.Parser.PrefixPosition;


import java.io.*;
import java.net.Socket;
import java.util.List;


public class IRCClient implements Runnable  {
    private String address;
    private int port;
    private String channel = "";
    private String nickname = "Shinzou";
    private String name = "mand";
    private String  domain = "localhost";
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private String message;


    public IRCClient(final String address, final int port){

        try {
            message = "";

            this.socket = new Socket(address, port);
            System.out.println("initial connection success !");
            this.writer = new PrintWriter(socket.getOutputStream());
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("initial connection error : " + e.toString());

        }
    }


    private void connect(){
        this.send("NICK " + nickname);
        this.send("USER " + nickname + " " + name + " " + domain + " :realname");
    }

    private void disconnect(){

    }


    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    synchronized public void send(String msg){

        this.processingMessage(IRCParser.parse(msg));
        this.writer.write(msg + "\r\n");
        this.writer.flush();


    }

    private String receive(){
        String line = " ";
        try {
            if(reader.ready()) line = reader.readLine();
            return line;
        } catch (IOException e) {
            return e.toString();
        }
    }

    private boolean receiveIsReady() {
        try {
            return reader.ready();
        } catch (IOException e) {
            System.out.println(e.toString());
            return false;
        }
    }



    synchronized private void processingMessage(IRCMessage message){


        CommandTypes commandTypes = null;

        for(CommandTypes cmd : CommandTypes.values()){
            if(message.getCommand().toUpperCase().equals(cmd.toString())){
                commandTypes = cmd;
                break;
            }
        }
        if(commandTypes == null) return;

        switch (commandTypes){

            case PING:
                this.send("PONG :" + message.getTrailing());

                break;
            case PRIVMSG:
                break;
            case JOIN:
                if( message.getPrefix(PrefixPosition.FIRST).equals(this.nickname)){
                    this.channel = message.getParameters()[0];
                    System.out.println("You have joined the channel : " + this.channel);

                }else if(!message.getPrefix().isEmpty()) {
                    System.out.println(message.getPrefix(PrefixPosition.FIRST) + " has joined the channel : " + message.getParameters()[0]);
                }

                break;
            case LIST:
                break;
            case NICK:
                break;
            case QUIT:
                break;
            case USER:
                break;
            case NAMES:
                break;
        }



    }




    public void run() {

       boolean stop = false;

       this.connect();
       while(!stop){
           if(!this.receiveIsReady()) continue;
           IRCMessage message = IRCParser.parse(this.receive());
           this.processingMessage(message);

           //System.out.println(message.getRaw());



       }
    }
}
