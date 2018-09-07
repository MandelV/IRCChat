package com.github.mandelV.main;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IRCClient implements Runnable  {
    private String address;
    private int port;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private String message;
    private Pattern regex;
    private Matcher matcher;


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

    public void run() {

        this.connect();

        while (true) {
            if (receiveIsReady()) {//Le message est entièrement reçu
                this.message = this.receive();//reception
                System.out.println(this.message);


            }
        }
    }
}
