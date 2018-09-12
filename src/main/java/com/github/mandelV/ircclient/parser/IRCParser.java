package com.github.mandelV.ircclient.parser;

import java.util.*;

/**
 * IRC parser
 * @author Vaubourg Mandel
 */
public final class IRCParser {

    /**
     * Parse raw input from server to IRCMessage
     *
     * @param ircMessage String that will parsed
     * @return input parsed in IRCMessage
     * @see IRCMessage
     */


    public static IRCMessage parse(String ircMessage){
        String input = ircMessage;


        if(input == null || input.equals("")) return null;

        int cursor = 0;
        String prefix = "";
        List<String> parsedPrefix = new ArrayList<>();
        CommandTypes command = null;
        List<String> arguments = new ArrayList<>();
        String trailling = "";
        HashMap<String, String> messageTags = new HashMap<>();//IRCv3

        //Place the cursor to the first character while as long as there are space.
        while(cursor < input.length() && input.charAt(cursor) == ' ') cursor++;

        //Remove the '/' (command character)
        if(cursor != input.length() && input.charAt(cursor) == '/') input = input.substring(1);

        //MESSAGE TAG IRCv3.x
        if(cursor != input.length() && input.length() > 1 && input.charAt(0) == '@'){

            cursor = input.indexOf(" ");
            if(cursor == -1) return null;

            String tags = input.substring(1, cursor);//Only gets the TAG

            StringTokenizer token = new StringTokenizer(tags,";");//Parse this TAG with StringTokenizer with ';' delimiter

            while (token.hasMoreTokens()){
                String[] splitValue = token.nextToken().split("=");
                if(splitValue.length == 2) messageTags.put(splitValue[0], splitValue[1]);
            }

            if(cursor == input.length()-1) return null;
            while(cursor < input.length() && input.charAt(cursor) == ' ') cursor++;
        }


        //PREFIX
        if(cursor != input.length() && input.length() > 1 && input.charAt(cursor) == ':'){

            int startCursor = cursor;

            cursor = input.indexOf(" ", cursor);
            if(cursor == -1) return null;

            String prefix_ = input.substring(startCursor, cursor-1);
            prefix = input.substring(startCursor+1, cursor);

            int testChar = 0;

            //Test if the PREFIX has right shape
            for(int i = 0; i < prefix_.length() && testChar <= 3; i++){
                if(prefix_.charAt(i) == ':' || prefix_.charAt(i) == '!' || prefix_.charAt(i) == '@') testChar++;
            }
            if(testChar < 3 || testChar > 3) return null;


            StringTokenizer token = new StringTokenizer(prefix_, ":!@");

            while (token.hasMoreTokens()) parsedPrefix.add(token.nextToken());


            if(cursor != input.length() && cursor == input.length()-1) return null;
            while(cursor < input.length() && input.charAt(cursor) == ' ') cursor++;
        }


        //COMMAND
        String cmdStr;
        int startCursor = cursor;


        cursor = input.indexOf(' ', cursor);
        if(cursor == -1) cursor = input.length();
        cmdStr = input.substring(startCursor, cursor).toUpperCase();

        //Test if the command matches with the CommandTypes enum.
        for(CommandTypes cmd : CommandTypes.values()){
            if(cmd.toString().equals(cmdStr)) command = cmd;
        }

        if(command == null || cursor == input.length()-1) return null;
        while(cursor < input.length() && input.charAt(cursor) == ' ') cursor++;


        //PARAMETERS
        //if(cursor == -1) cursor = input.length()-1;
        String str = input.substring(cursor);

        int strSize = str.length();
        cursor = str.indexOf(':');

        if(cursor == -1) cursor = str.length()-1;
        String param = "";

        if(cursor+1 <= str.length()) param = str.substring(0, cursor+1);

        StringTokenizer args = new StringTokenizer(param, " ");

        while (args.hasMoreTokens()) arguments.add(args.nextToken());

        //TRAILING
        if(cursor != str.length()-1) trailling = str.substring(str.indexOf(':'), strSize).replaceFirst(":","");

        return new IRCMessage(input, prefix, parsedPrefix, command, arguments, messageTags, trailling);
    }
}