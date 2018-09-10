package com.github.mandelV.IRCClient.Parser;

import java.util.*;

/**
 * IRC Parser
 *
 * @author Vaubourg Mandel
 * Jan 18, 2017
 */
public final class IRCParser {

    /**
     * Parse raw input from server to IRCMessage
     *
     * @param input
     * @return
     */


    public static final IRCMessage parse(String input){

        //System.out.println(input);
        if(input == null || input.equals("")) return null;
        //System.out.print("test1 ");

        int cursor = 0;

        String prefix = "";
        List<String> parsedPrefix = new ArrayList<>();
        CommandTypes command = null;
        List<String> arguments = new ArrayList<>();
        String traling = "";
        HashMap<String, String> messageTags = new HashMap<>();//IRCv3

        while(cursor < input.length() && input.charAt(cursor) == ' ') cursor++;

        if(input.charAt(cursor) == '/') input = input.substring(1);

        //System.out.print("test2 ");




        //MESSAGE TAG
        if(input.charAt(0) == '@'){

            cursor = input.indexOf(" ");
            String tags = input.substring(1, cursor);
            StringTokenizer token = new StringTokenizer(tags,";");
            while (token.hasMoreTokens()){
                String[] splitValue = token.nextToken().split("=");
                if(splitValue.length == 2) messageTags.put(splitValue[0], splitValue[1]);
            }
            if(cursor == input.length()-1) return null;
            while(cursor < input.length() && input.charAt(cursor) == ' ') cursor++;
        }
        //System.out.print("test3 ");

        //messageTags.forEach((k, v) -> System.out.println(k + " : " + v));



        //PREFIX
        if(input.charAt(cursor) == ':'){

            int startCursor = cursor;

            cursor = input.indexOf(" ", cursor);
            String prefix_ = input.substring(startCursor, cursor);
            prefix = input.substring(startCursor+1, cursor);

            int testChar = 0;
            for(int i = 0; i < prefix_.length() && testChar <= 3; i++){
                if(prefix_.charAt(i) == ':' || prefix_.charAt(i) == '!' || prefix_.charAt(i) == '@') testChar++;
            }
            if(testChar < 3 || testChar > 3) return null;


            StringTokenizer token = new StringTokenizer(prefix_, ":!@");

            while (token.hasMoreTokens()){
                parsedPrefix.add(token.nextToken());
            }


            if(cursor == input.length()-1) return null;
            while(cursor < input.length() && input.charAt(cursor) == ' ') cursor++;
        }
        //System.out.print("test4 ");



        //COMMAND
        String cmdStr = "";
        int startCursor = cursor;
        if(cursor == 0){
            startCursor = 0;
        }

        cursor = input.indexOf(' ', cursor);
       // System.out.println(cursor);
        if(cursor == -1) cursor = input.length();
        cmdStr = input.substring(startCursor, cursor);


        for(CommandTypes cmd : CommandTypes.values()){
            if(cmd.toString().toUpperCase().equals(cmdStr.toUpperCase())) command = cmd;
        }


        if(command == null) return null;

       //System.out.print("test5 ");

        if(cursor == input.length()-1 && command == null) return null;
        while(cursor < input.length() && input.charAt(cursor) == ' ') cursor++;

       // System.out.print("test6 ");
        //PARAMETERS AND TRALING

        startCursor = cursor;
        if(cursor == -1) cursor = input.length();
        String str = input.substring(cursor);

        List<String> paramAndTralling = new ArrayList<>();

        StringTokenizer token = new StringTokenizer(str," ");

        while(token.hasMoreTokens()){
            paramAndTralling.add(token.nextToken());
        }
        //System.out.print("test7 ");




        if(paramAndTralling.size() != 0 && paramAndTralling.get(paramAndTralling.size()-1).charAt(0) == ':'){
            traling =  paramAndTralling.get(paramAndTralling.size()-1).substring(1);
            paramAndTralling.remove(paramAndTralling.size()-1);
        }

        paramAndTralling.forEach(v -> arguments.add(v));

        //System.out.print("test9 ");
        return new IRCMessage(input, prefix, parsedPrefix, command, arguments, messageTags, traling);
    }
}