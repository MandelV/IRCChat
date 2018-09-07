package com.github.mandelV.IRCClient;

public class Commands {
    private String sendername;
    private String nickname;
    private String domain;
    private CommandTypes commandTypes;
    private String param;


    public Commands(final String sendername, final String nickname, final String domain, CommandTypes commandTypes, String param){
        this.sendername = sendername;
        this.nickname = nickname;
        this.domain = domain;
        this.commandTypes = commandTypes;
        this.param = param;
    }

    public CommandTypes getCommandTypes() {
        return commandTypes;
    }

    public String getDomain() {
        return domain;
    }

    public String getNickname() {
        return nickname;
    }

    public String getParam() {
        return param;
    }

    public String getSendername() {
        return sendername;
    }

}
