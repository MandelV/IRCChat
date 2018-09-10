package com.github.mandelV.IRCClient.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * IRCMessage
 *
 * @author VAUBOURG Mandel
 *
 */
public class IRCMessage {
    String originalRaw = "";
    String prefix = "";
    List<String> parsedPrefix = new ArrayList<>();
    CommandTypes command = null;
    List<String> arguments = new ArrayList<>();
    String trailing = "";
    HashMap<String, String> messageTags = new HashMap<>();//IRCv3


   public IRCMessage(final String originalRaw,
                     final String prefix,
                     final List<String> parsedPrefix,
                     final CommandTypes commandTypes,
                     final List<String> arguments,
                     final HashMap<String, String> messageTags,
                     final String trailing){


       this.originalRaw = originalRaw;
       this.prefix = prefix;
       this.parsedPrefix = parsedPrefix;
       this.command = commandTypes;
       this.arguments = arguments;
       this.trailing = trailing;
       this.messageTags = messageTags;
   }

    public CommandTypes getCommand() {
        return command;
    }

    public HashMap<String, String> getMessageTags() {
        return messageTags;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public List<String> getParsedPrefix() {
        return parsedPrefix;
    }


    public String getOriginalRaw() {
        return originalRaw;
    }

    public String getPrefix(PrefixPosition position){
       return (this.parsedPrefix.size() != 0) ? this.parsedPrefix.get(position.getValue()) : "";

    }

    public String getPrefix() {
        return prefix;
    }

    public String getTrailing() {
        return trailing;
    }

}
