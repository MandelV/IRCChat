package com.github.mandelV.IRCClient.Bots;

import com.github.mandelV.IRCClient.Client.IRCClient;
import com.github.mandelV.IRCClient.Client.InstanciateException;

import java.util.Base64;
import java.util.Observable;
import java.util.Observer;

public class Bots implements Observer {

    private String name;
    private String nickname;
    private String decode;
    private boolean send = false;


    @Override
    public void update(Observable o, Object arg) {


    }
}
