package com.github.mandelV.IRCClient.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private Pattern regex;
    private Matcher matcher;


    public Parser(String regex){

        this.regex = Pattern.compile(regex);

    }
    public  Parser(){}

    public void setRegex(String regex){
        this.regex = Pattern.compile(regex);
    }

    /**
     *
     * @param str string
     * @return the groupe number 1
     * @throws ParserException
     */
    public String find(final String str) throws ParserException {

        String findstr = "NONE";
        try{
            this.matcher = regex.matcher(str);
        }catch (Exception e){
            throw new ParserException("Error at Parser fin() (no regex set) : " + e);
        }

        if(this.matcher.find()) {

            if(this.matcher.groupCount() == 0) return "false";
            try {
                return findstr = this.matcher.group(1);
            } catch (Exception e) {
                throw new ParserException("Error at Parser find() : " + e);
            }
        }else {
            findstr = "false";
        }

        return findstr;
    }

    public String[] parser(final String str) throws ParserException {

        String[] findstr = null;
        try{
            this.matcher = regex.matcher(str);
        }catch (Exception e){
            throw new ParserException("Error at Parser fin() (no regex set) : " + e);
        }

        if(this.matcher.find()) {


            try {
                findstr = new String[this.matcher.groupCount()];
                for(int i = 0; i < this.matcher.groupCount(); i++) findstr[i] = this.matcher.group(i);



            } catch (Exception e) {
                throw new ParserException("Error at Parser find() : " + e);

            }
        }

        return findstr;
    }

}
