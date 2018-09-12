package com.github.mandelV.IRCClient.Bots;

import java.util.Observable;
import java.util.Observer;

/**
 * Bots
 * @author VAUBOURG Mandel
 */
public interface Bots extends Observer {

    @Override
    void update(Observable o, Object arg);
}
