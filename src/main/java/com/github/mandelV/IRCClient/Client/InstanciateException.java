package com.github.mandelV.IRCClient.Client;

/**
 * InstanciateExeption
 * @author VAUBOURG Mandel
 */
public class InstanciateException extends Exception {
    public InstanciateException(){
        super();
    }
    public InstanciateException(final String err){
        super(err);
    }
}
