package com.github.mandelV.IRCClient.Client;

import com.github.mandelV.IRCClient.Chat.Chat;
import com.github.mandelV.IRCClient.Parser.IRCMessage;
import com.github.mandelV.IRCClient.Parser.IRCParser;
import com.github.mandelV.IRCClient.Parser.PrefixPosition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

/**
 * IRCClient
 * @author Mandel VAUBOURG
 */
public class IRCClient implements Runnable  {
    private String address;
    private int port;
    private String channel;
    private String nickname;
    private String name;
    private String  domain;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean stop = false;
    private static IRCClient instance;


    /**
     *
     * @param address address ip
     * @param port port
     * @param channel channel irc
     * @param nickname nickname
     * @param name real name
     * @param domain domain of user
     * @throws ConnectionException when connection fails
     */
    private IRCClient(final String address, final int port, final String channel, final String nickname, final String name, final String domain) throws ConnectionException{

        this.address = address;
        this.port = port;
        this.channel = channel;
        this.nickname = nickname;
        this.name = name;
        this.domain = domain;

        try {
            this.socket = new Socket(this.address, this.port);
            Chat.displayMsg("initial connection success !");
            this.writer = new PrintWriter(socket.getOutputStream());
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new ConnectionException("Error connection : " + e.toString());
        }
    }

    /**
     *
     * @param address address ip
     * @param port port
     * @param channel channel irc
     * @param nickname nickname
     * @param name real name
     * @param domain domain of user
     * @return IRCClient instance if connection is success.
     */
    synchronized static public IRCClient getInstance(final String address, final int port, final String channel, final String nickname, final String name, final String domain){
        try {
            instance = (instance == null) ? new IRCClient(address, port, channel, nickname, name, domain) : instance;
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     *
     * @return IRCClient instance is not null.
     * @throws InstanciateException when instance of IRCChannel is null
     */
    synchronized static public IRCClient getInstance() throws InstanciateException{
        if(instance == null) throw new InstanciateException("Instance is null");
        return instance;
    }
    /**
     *
     * @return the domain of user
     */
    public String getDomain() {
        return domain;
    }
    /**
     *
     * @return port
     */
    public int getPort() {
        return port;
    }
    /**
     *
     * @return address
     */
    public String getAddress() {
        return address;
    }
    /**
     *
     * @return real name
     */
    public String getName() {
        return name;
    }


    /**
     *
     * @return if the loop is stopped
     */
    synchronized public boolean isStop() {
        return (!stop);
    }

    /**
     * allow to connect to irc server
     */
     private void connect(){
        this.send("NICK " + nickname);
        this.send("USER " + nickname + " " + name + " " + domain + " :realname");
    }

    /**
     *
     * @param channel set channel
     */
    private void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     *
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @param msg msg send to server
     */
    synchronized public void send(String msg){

        if(msg == null) return;

        IRCParser.parseV2(msg).ifPresent(this::processingMessage);

        this.writer.write(msg + "\r\n");
        this.writer.flush();


    }

    /**
     *
     * @return msg send by server
     */
    private Optional<String> receive(){

        try {

            if(!reader.ready()) return Optional.empty();

            return Optional.of(reader.readLine());
        } catch (IOException e) {
            System.out.println(e.toString());
            return Optional.empty();
        }
    }

    /**
     *
     * @return if the received message is ready to read
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
     * @param message proccess the message
     */
    synchronized private void processingMessage(IRCMessage message){
        if(message == null) return;

        Chat.getInstance().pushMessage(message);
        switch (message.getCommand()){

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
                    this.setChannel(message.getTrailing());
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
                default:
                    break;
        }
    }


    public void run() {

      this.connect();
       while(this.isStop()){
           if(this.receiveIsReady()){

               //IRCMessage message = IRCParser.parse(this.receive());
               //this.processingMessage(message);

               this.receive().ifPresent(v -> IRCParser.parseV2(v).ifPresent(this::processingMessage));


           }
       }
       try{
           this.reader.close();
           this.writer.close();
           this.socket.close();
       }catch (Exception e){
           System.out.println(e.toString());
       }


    }
}
