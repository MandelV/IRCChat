package com.github.mandelV.IRCClient.client;

class ConnectionException extends Exception{

    ConnectionException(final String err){
        super(err);
    }
}
