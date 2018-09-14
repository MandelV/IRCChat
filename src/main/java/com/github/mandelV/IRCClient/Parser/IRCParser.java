package com.github.mandelV.IRCClient.Parser;

import com.github.mandelV.IRCClient.Chat.Chat;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;


/**
 * IRC Parser
 * @author Vaubourg Mandel
 */
public final class IRCParser {


    /**
     * @param input String to be parsed
     * @return parser that will be used to get part of the input.
     * @see org.antlr.v4.parse.ANTLRParser
     * @see org.antlr.v4.parse.ANTLRLexer
     * @see CommonTokenStream
     * @see ANTLRInputStream
     */
    private static IrcGrammarParser getParser(final String input){
        if(input == null || input.isEmpty()) return null;
        IrcGrammarLexer lexer = new IrcGrammarLexer(new ANTLRInputStream(input));
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        IrcGrammarParser parser = new IrcGrammarParser(tokenStream);
        parser.reset();
        if(parser.ircMessage() == null) return null;
        parser.reset();

        return parser;
    }
    /**
     * Get prefix
     * @param ircMessage String to be parsed
     * @return Optional String
     * @see Optional
     */
    private static Optional<String> getPrefix(final String ircMessage){
        IrcGrammarParser parser = getParser(ircMessage);

        if(parser == null || parser.ircMessage().first() == null) return Optional.empty();
        parser.reset();
        if(parser.ircMessage().first().PREFIX() == null) return Optional.empty();
        parser.reset();

        return Optional.of(parser.ircMessage().first().PREFIX().toString());
    }
    /**
     *Get tags of the ircMessage
     * @param ircMessage String to be parsed
     * @return Optional HasMap<String, String> key value tag
     */
    private static Optional<HashMap<String, String>> getTags(final String ircMessage){
        IrcGrammarParser parser = getParser(ircMessage);

        HashMap<String, String> tags = new HashMap<>();

        if(parser == null || parser.ircMessage().first() == null) return Optional.empty();
        if(parser.first() == null) return Optional.empty();
        parser.reset();
        if(parser.ircMessage().first().TAGS() == null) return Optional.empty();
        parser.reset();


        StringTokenizer tokenizer = new StringTokenizer(parser.ircMessage().first().TAGS().toString(), "@;");

        while (tokenizer.hasMoreTokens()){

            String[] splitTags = tokenizer.nextToken().split("=");

            if(splitTags.length == 2) tags.put(splitTags[0], splitTags[1]);

        }
        return Optional.of(tags);
    }
    /**
     * Get Command of the ircMessage
     * @param ircMessage String to be Parsed
     * @return Type of the command
     * @see CommandTypes
     * @see Optional
     */
    private static Optional<CommandTypes> getCommand(final String ircMessage){

        IrcGrammarParser parser = getParser(ircMessage);

        if(parser == null || parser.ircMessage().cmd() == null) return Optional.empty();
        parser.reset();
        if(parser.ircMessage().cmd().isEmpty()) return Optional.empty();
        parser.reset();


        String c = parser.ircMessage().cmd().STRING().toString().toUpperCase();
        CommandTypes cmd = CommandTypes.getValue(c);

        if(cmd == null) return Optional.empty();

        return Optional.of(cmd);
    }
    /**
     * Get Arguments command
     * @param ircMessage String to be parsed
     * @return Optional List<String> list of the arguments
     * @see Optional
     */
    private static Optional<List<String>> getArguments(final String ircMessage){
        IrcGrammarParser parser = getParser(ircMessage);

        List<String> arguments = new ArrayList<>();

        if(parser == null || parser.ircMessage().args() == null) return Optional.empty();
        parser.reset();

        for(TerminalNode node : parser.ircMessage().args().STRING()) arguments.add(node.toString());

        return Optional.of(arguments);
    }

    /**
     * Get Trailling of the ircMessage
     * @param ircMessage  String to be parsed
     * @return Trailling.
     */
    private static Optional<String> getTralling(final String ircMessage){
        IrcGrammarParser parser = getParser(ircMessage);

        if(parser == null || parser.ircMessage().trailling() == null) return Optional.empty();
        parser.reset();



        return Optional.of(parser.ircMessage().trailling().getText());
    }
    private static Optional<String> getError(final String ircMessage){

        IrcGrammarParser parser = getParser(ircMessage);

        if(parser == null || parser.ircMessage().error() == null) return Optional.empty();
        parser.reset();

        return Optional.of(parser.ircMessage().error().getText());

    }
    /**
     * @param input String to be parsed.
     * @return Optional Parsed message (if parsing has failed Optional will be empty)
     * @see Optional
     */
    public static Optional<IRCMessage> parseV2(final String input)  {

        String ircMessage = input;
        if(ircMessage == null || ircMessage.isEmpty()) return Optional.empty();

        if(ircMessage.charAt(0) == '/') ircMessage = ircMessage.replaceFirst("/", "");

        Optional<String> error = getError(ircMessage);
        error.ifPresent(Chat::displayMsg);
        if(error.isPresent()) return Optional.empty();

        String prefix;

        List<String> parsedPrefix = new ArrayList<>();
        CommandTypes command;
        List<String> arguments;
        String trailing;
        HashMap<String, String> tags;

        //GET PREFIX
        prefix = getPrefix(ircMessage).orElse("");
        prefix = prefix.replaceFirst(" ", "");

        StringTokenizer tokenizer = new StringTokenizer(prefix, ":!@");
        while (tokenizer.hasMoreTokens()) parsedPrefix.add(tokenizer.nextToken());

        //System.out.println("prefix : " + prefix);

        //GET TAGS
        tags = getTags(ircMessage).orElse(new HashMap<>());

        //System.out.println("tag : " +tags.toString());


        //GET COMMAND
        try {
            command = getCommand(ircMessage).orElseThrow(() -> new CommandException("Command exception"));
        } catch (CommandException e) {
            return Optional.empty();
        }

        //System.out.println("command : "+ command.toString());


        //GET ARGUMENTS
        arguments = getArguments(ircMessage).orElse(new ArrayList<>());



        //System.out.println("arg : " +arguments);

        //GET TRAILLING
        trailing = getTralling(ircMessage).orElse("");
        trailing = trailing.replaceFirst(":", "");

        //System.out.println("trailling : " +trailing);

        return Optional.of(new IRCMessage(ircMessage, prefix, parsedPrefix, command, arguments, tags, trailing));
    }

    /**
     * @param s String to be parsed.
     * @return String parsed
     * @see IRCMessage
     */
    /*public static IRCMessage parse(String s){
        String input = s;
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
    }*/
}