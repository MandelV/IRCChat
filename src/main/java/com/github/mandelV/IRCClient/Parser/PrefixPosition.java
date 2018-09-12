package com.github.mandelV.IRCClient.Parser;

/***
 * PrefixPosition
 * @author VAUBOURG Mandel
 */
public enum PrefixPosition {
    FIRST(0), SECOND(1), THIRD(2);

    private final int value;

     PrefixPosition(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
