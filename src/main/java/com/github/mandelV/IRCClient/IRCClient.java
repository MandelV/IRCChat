package com.github.mandelV.IRCClient;

import com.github.mandelV.IRCClient.Parser.IRCMessage;
import com.github.mandelV.IRCClient.Parser.IRCParser;


import java.io.*;
import java.net.Socket;
import java.util.List;


public class IRCClient implements Runnable  {
    private String address;
    private int port;
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
        this.send("NICK Shinzou");
        this.send("USER Shinzou test localhost :realname");
    }

    private void disconnect(){

    }





    synchronized public void send(String msg){
            this.writer.write(msg + "\r\n");
            this.writer.flush();
    }

    private String receive(){
        String line = "";
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



    private Commands processingMessage(final String str){

        return  null;
    }




    public void run() {

       boolean stop = false;

       //this.connect();

        String raw = "PING :DDSQDQSDQS";
        IRCMessage parsed = IRCParser.parse(raw);


        System.out.println(parsed.getCommand()); //QUIT
        System.out.println(parsed.getTrailing()); //Gone to have lunch


       while(true){

       }
    }
}
