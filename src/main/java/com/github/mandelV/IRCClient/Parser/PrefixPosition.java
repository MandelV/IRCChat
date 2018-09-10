package com.github.mandelV.IRCClient.Parser;

public enum PrefixPosition {
    FIRST(0), SECOND(1), THIRD(2);

    private final int value;

    private PrefixPosition(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
