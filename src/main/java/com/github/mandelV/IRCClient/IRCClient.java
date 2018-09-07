package com.github.mandelV.IRCClient;

import com.github.mandelV.IRCClient.Parser.Parser;
import com.github.mandelV.IRCClient.Parser.ParserException;

import java.io.*;
import java.net.Socket;


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

    private void pong(){

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
        System.out.println(str);
        Parser parser = new Parser();
        Commands commands = null;
        CommandTypes commandTypes = null;
        String param = "";

        if(str.startsWith(":")){



        }else{

            for(CommandTypes c : CommandTypes.values()){
                if(str.startsWith(c.toString())){
                    commandTypes = c;
                    parser.setRegex(":(.*?)$");
                    try {
                        param = parser.find(str);
                    } catch (ParserException e) {
                        e.printStackTrace();
                    }

                }
            }
            commands = new Commands("", "", "", commandTypes, param);

        }






        return commands;

    }




    public void run() {


       boolean stop = false;


       this.connect();

       while(true){
           if(!this.receiveIsReady()) continue;

           Commands commands = this.processingMessage(this.receive());

           if(commands == null) continue;
           if(commands.getCommandTypes() == CommandTypes.PING){
               this.send("PONG :" + commands.getParam());

           }


       }







    }
}
