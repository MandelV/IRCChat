package com.github.mandelV.IRCClient.Client;

public class ConnectionException extends Exception{
    public ConnectionException(){
        super();
    }
    public ConnectionException(final String err){
        super(err);
    }
}
