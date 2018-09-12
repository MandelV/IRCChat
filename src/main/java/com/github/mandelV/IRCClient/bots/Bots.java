package com.github.mandelV.IRCClient.bots;

import java.util.Observable;
import java.util.Observer;

/**
 * bots
 * @author VAUBOURG Mandel
 */
public interface Bots extends Observer {

    @Override
    void update(Observable o, Object arg);
}
