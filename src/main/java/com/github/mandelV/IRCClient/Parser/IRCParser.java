package com.github.mandelV.IRCClient.Parser;
import com.github.mandelV.IRCClient.CommandTypes;

import java.util.*;

/**
 * IRC Parser
 *
 * @author xdevnull
 * Jan 18, 2017
 */
public final class IRCParser {

    /**
     * Parse raw input from server to IRCMessage
     *
     * @param input
     * @return
     */


    public static final IRCMessage parse(final String input){
        if(input == null || input.equals("") || input.length() > 512) return null;

        int cursor = 0;

        String prefix = "";
        List<String> parsedPrefix = new ArrayList<>();
        CommandTypes command = null;
        List<String> arguments = new ArrayList<>();
        String traling = "";
        HashMap<String, String> messageTags = new HashMap<>();//IRCv3

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
            System.out.println(prefix);
            parsedPrefix.forEach((v -> System.out.print(v + " ")));

            if(cursor == input.length()-1) return null;
            while(cursor < input.length() && input.charAt(cursor) == ' ') cursor++;
        }



        //COMMAND
        int startCursor = cursor;
        cursor = input.indexOf(" ", cursor);
        if(cursor == -1) cursor = input.length();
        String cmdStr = input.substring(startCursor, cursor);

        for(CommandTypes cmd : CommandTypes.values()){
            if(cmd.toString().toUpperCase().equals(cmdStr.toUpperCase())) command = cmd;
        }



        if(cursor == input.length()-1 && command == null) return null;
        while(cursor < input.length() && input.charAt(cursor) == ' ') cursor++;


        //PARAMETERS AND TRALING

        startCursor = cursor;
        if(cursor == -1) cursor = input.length();
        String str = input.substring(cursor, input.length()-1);
        List<String> paramAndTralling = new ArrayList<>();

        StringTokenizer token = new StringTokenizer(str," :");

        while(token.hasMoreTokens()) paramAndTralling.add(token.nextToken());


        return null;
    }

    @Deprecated
    public static final IRCMessage parser(String input) {
        //Invalid message
        if(input == null || input.equals("")) return null;
        HashMap<String, String> message_tags = new HashMap<String, String>();
        String prefix = "";
        String command = "";
        ArrayList<String> middle = new ArrayList<String>();
        String trailing = "";

        //point to the next part that needs to be processed
        int cursor = 0;

        //Determine whether input start with @ (0x40)
        //Indicates that the message contains IRCv3.2 tags
        //Tags joined by ';' semicolon and ends at the first space
        if(input.charAt(0) == '@') {

            //The end of tags
            cursor = input.indexOf(" ");

            //Extract tags part excluding the @
            String tags = input.substring(1, cursor);

            //Break tags into tokens
            //Where Key=Value or Key without any value
            StringTokenizer token = new StringTokenizer(tags, ";");
            while(token.hasMoreTokens()) {
                //Extract Key and value
                String[] kv = token.nextToken().split("=");
                if(kv.length == 2) {
                    String val = kv[1];
                    //Escape value
                    while(val.contains("\\r"))
                        val = val.replace("\\r", "\r");
                    while(val.contains("\\n") )
                        val = val.replace("\\n", "\n");
                    while(val.contains("\\\\"))
                        val = val.replace("\\\\", "\\");
                    while(val.contains("\\s"))
                        val = val.replace("\\s", " ");
                    while(val.contains("\\:"))
                        val = val.replace("\\:", ";");
                    message_tags.put(kv[0], val);

                }
                else if(kv.length == 1)
                    message_tags.put(kv[0], null);
            }

        }

        //Ignore any whitespace
        while(input.charAt(cursor) == ' ') cursor++;



        //Determine whether message contains a prefix component
        //Prefix components starts with :
        if(cursor < input.length() && input.charAt(cursor) == ':') {

            //Prefix beginning
            int prefix_beginning = cursor + 1;

            //Point at the next component
            cursor = input.indexOf(" ", cursor);

            //Get prefix
            prefix = input.substring(prefix_beginning, cursor);
        }

        //Ignore any whitespace
        while(input.charAt(cursor) == ' ')
            cursor++;

        //Extract the message command component
        //Command 1 letter or three digits
        if(cursor < input.length()) {
            int command_beginning = cursor;
            cursor = input.indexOf(" ", cursor);
            if(cursor > -1) command = input.substring(command_beginning, cursor);
        }
        if(cursor < 0) cursor = 0;

        //Ignore any whitespace
        while(input.charAt(cursor) == ' ')
            cursor++;

        //Extract message parameters component

        while(cursor < input.length()) {
            int next = input.indexOf(" ", cursor);

            //Trailing part
            //End of message
            if(input.charAt(cursor) == ':') {
                trailing = input.substring(++cursor, input.length());
                break;
            }

            //Message contain trailing
            if(next != -1) {
                middle.add(input.substring(cursor, next));
                cursor = next + 1;

                while(cursor < input.length() && input.charAt(cursor) == ' ')
                    cursor++;
                continue;
            }

            //If message has no trailing
            //End of message
            if(next == -1) {
                middle.addAll(Arrays.asList(input.substring(cursor, input.length()).split(" ")));
                break;
            }
        }

        return new IRCMessage(input, message_tags, prefix, command, middle.toArray(new String[middle.size()]), trailing);
    }

}