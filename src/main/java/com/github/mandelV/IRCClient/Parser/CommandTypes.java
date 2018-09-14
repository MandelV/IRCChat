package com.github.mandelV.IRCClient.Parser;

/**
 * CommandTypes
 * @author VAUBOURG Mandel
 */
public enum CommandTypes {
    USER,
    PART,
    NICK,
    PONG,
    PING,
    QUIT,
    SQUIT,
    JOIN,
    NAMES,
    LIST,
    PRIVMSG,
    NOTICE;



    CommandTypes() {
    }

    public static CommandTypes getValue(final String value){

        for(CommandTypes cmd : CommandTypes.values()){
            if(cmd.toString().toUpperCase().equals(value.toUpperCase())) return cmd;
        }

        return null;
    }
}
