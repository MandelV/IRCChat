// Our grammar is called C3PO.
grammar IrcGrammar;
options{
    language = Java;
}

//EXEMPLE @tag1=value1 :Test!Unit@Hostfr CMD arg #arg2 :trailling


/*
STRING : [a-zA-Z0-9]+ ;
TAG : STRING'='?STRING? ';'?;

PREFIX : ':'STRING'!'STRING'@'[a-zA-Z0-9.]+;

TAGS : '@' TAG+ ;

ARGS : [a-zA-Z0-9 #]+' '+;

TRALLING : ':' [a-zA-Z0-9 !?:;$£*µ%ù¨^&é"'\-èà_ç]+;

SPACE : ' ' -> skip;


ircMessage : TAGS? PREFIX? ARGS TRALLING?;

*/


//Tag Lexer rules
TagSeparator : ';' -> skip;
Tag : [a-zA-Z0-9]+'='[a-zA-Z0-9]+';'?;

SPACE : ' ' -> skip;

//Prefix lexer rules

STRING : [a-zA-Z0-9#.-]+;



ircMessage : first? cmd  args?  trailling? ;

first : TAGS? PREFIX?;

TAGS : '@'Tag+;

PREFIX : ':'STRING'!'STRING'@'STRING;

cmd : ' '?STRING' '?;

args : (STRING)+ ;

trailling : ':'.*;




