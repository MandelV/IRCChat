package com.github.mandelV.IRCClient.Client;

import com.github.mandelV.IRCClient.Chat.Chat;
import com.github.mandelV.IRCClient.Parser.CommandTypes;
import com.github.mandelV.IRCClient.Parser.IRCMessage;
import com.github.mandelV.IRCClient.Parser.IRCParser;
import com.github.mandelV.IRCClient.Parser.PrefixPosition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * IRCClient
 * @author Mandel VAUBOURG
 */
public class IRCClient implements Runnable  {
    private String address;
    private int port;
    private String channel = "";
    private String nickname;
    private String name;
    private String  domain;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean stop = false;
    private static IRCClient instance;
    private boolean v = false;


    /**
     *
     * @param address
     * @param port
     */
    private IRCClient(final String address, final int port, final String channel, final String nickname, final String name, final String domain){

        this.channel = channel;
        this.nickname = nickname;
        this.name = name;
        this.domain = domain;

        try {
            this.socket = new Socket(address, port);
            Chat.displayMsg("initial connection success !");
            this.writer = new PrintWriter(socket.getOutputStream());
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("initial connection error : " + e.toString());

        }
    }

    synchronized static public IRCClient getInstance(final String address, final int port, final String channel, final String nickname, final String name, final String domain){
        instance = (instance == null) ? new IRCClient(address, port, channel, nickname, name, domain) : instance;
        return instance;
    }
    synchronized static public IRCClient getInstance() throws InstanciateException{

        if(instance == null) throw new InstanciateException("Instance is null");

        return instance;
    }

    /**
     *
     * @return
     */
    synchronized public boolean isStop() {
        return stop;
    }

    /**
     *
     */
    private void connect(){
        this.send("NICK " + nickname);
        this.send("USER " + nickname + " " + name + " " + domain + " :realname");
    }

    /**
     *
     * @param channel
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     *
     * @return
     */
    public String getChannel() {
        return channel;
    }

    /**
     *
     * @return
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @param msg
     */
    synchronized public void send(String msg){

        if(msg == null) return;
        this.processingMessage(IRCParser.parse(msg));
        this.writer.write(msg + "\r\n");
        this.writer.flush();


    }

    /**
     *
     * @return
     */
    private String receive(){
        String line = " ";
        try {
            if(reader.ready()) line = reader.readLine();
            return line;
        } catch (IOException e) {
            return e.toString();
        }
    }

    /**
     *
     * @return
     */
    private boolean receiveIsReady() {
        try {
            return reader.ready();
        } catch (IOException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    /**
     *
     * @param message
     */
    synchronized private void processingMessage(IRCMessage message){
        if(message == null) return;
        CommandTypes commandTypes = null;

        //determines what kind of cmd in message
        for(CommandTypes cmd : CommandTypes.values()){
            if(message.getCommand().toString().equals(cmd.toString())){
                commandTypes = cmd;
                break;
            }
        }
        Chat.getInstance().pushMessage(message);
        if(commandTypes == null) return;

        switch (commandTypes){

            case PING:
                this.send("PONG :" + message.getTrailing());
                break;
            case PRIVMSG:

                if(!message.getPrefix(PrefixPosition.FIRST).equals(this.nickname) && !message.getPrefix().isEmpty()){
                    String mp = (message.getArguments().size() > 0 && message.getArguments().get(0).equals(this.nickname)) ? " (whisper) " : " ";
                    Chat.displayMsg(message.getPrefix(PrefixPosition.FIRST) + mp + ": " + message.getTrailing());
                }
                break;
            case JOIN:

                if(message.getPrefix(PrefixPosition.FIRST).equals(this.nickname)){
                    this.channel = message.getTrailing();
                    Chat.displayMsg("You have joined the channel : " + this.channel);

                }else if(!message.getPrefix().isEmpty()) {
                    Chat.displayMsg(message.getPrefix(PrefixPosition.FIRST) + " has joined the channel : " + message.getTrailing());
                }

                break;
            case PART:

                break;
            case LIST:
                break;
            case NICK:
                break;
            case QUIT:
                Chat.displayMsg("You have leaved the server");
                this.stop = true;

                break;
            case USER:
                break;
            case NAMES:
                break;
        }
    }
    /**
     *
     */
    public void run() {

      this.connect();
       while(!this.stop){
           if(this.receiveIsReady()){

               IRCMessage message = IRCParser.parse(this.receive());
               this.processingMessage(message);
           }
       }
       try{
           this.reader.close();
           this.writer.close();
           this.socket.close();
       }catch (Exception e){
           System.out.println(e);
       }


    }
}
