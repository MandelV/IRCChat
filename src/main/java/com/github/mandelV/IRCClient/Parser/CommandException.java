package com.github.mandelV.IRCClient.Parser;

public class CommandException extends Exception {
    CommandException(final String err){
        super(err);
    }
}
