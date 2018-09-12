import com.github.mandelV.IRCClient.Parser.CommandTypes;
import com.github.mandelV.IRCClient.Parser.IRCMessage;
import com.github.mandelV.IRCClient.Parser.IRCParser;
import com.github.mandelV.IRCClient.Parser.PrefixPosition;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;


public class IRCParserTest {

    @Test
    public void parseEmptyStringTest(){

        Assert.assertNull("parseEmptyStringTest : message is not  null", IRCParser.parse(""));
    }
    @Test
    public void parseWrongStringTest(){

        Assert.assertNull("parseEmptyStringTest : message is not  null", IRCParser.parse("/"));


        Assert.assertNull("parseEmptyStringTest : message is not  null", IRCParser.parse("   dddd ddd ddd ddqdqsdqsd "));


        Assert.assertNull("parseEmptyStringTest : message is not  null", IRCParser.parse("                                                        "));


        Assert.assertNull("parseEmptyStringTest : message is not null", IRCParser.parse(":d@a d d d d :c"));


        Assert.assertNull("parseEmptyStringTest : message is not null", IRCParser.parse("@:@!"));
    }

    @Test
    public void parseRightStringTest(){
        IRCMessage message;

        message = IRCParser.parse("/ping :A5dc8");
        Assert.assertNotNull("parseEmptyStringTest : message is not null", message);
        if(message.getCommand() != CommandTypes.PING) fail("/ping :A5dc8 - Command not matches");
        if(!message.getTrailing().equals("A5dc8")) fail("/ping :A5dc8 - Trailling not equals");

        message = IRCParser.parse(":Test!Unit@Host.fr QUIT :Bye people !");
        Assert.assertNotNull("parseEmptyStringTest : message is not null", message);

        if(message.getPrefix(PrefixPosition.FIRST).equals("Test")
        && message.getPrefix(PrefixPosition.SECOND).equals("Unit")
        && message.getPrefix(PrefixPosition.THIRD).equals("Host.fr"))
            fail("Prefix does not matches");

        if(message.getCommand() != CommandTypes.QUIT) fail("command does not matches");
        if(!message.getTrailing().equals("Bye people !")) fail("trailling does not matches");


        message = IRCParser.parse(":Test!Unit@Host.fr JOIN #channel");
        Assert.assertNotNull("parseEmptyStringTest : message is not null", message);

        if(message.getArguments().size() == 0 || !message.getArguments().get(0).equals("#channel")) fail("no arguments or not matches");

        message = IRCParser.parse("@tag1=value1 :Test!Unit@Host.fr JOIN #channel");
        Assert.assertNotNull("parseEmptyStringTest : message is not null", message);

        if(!message.getTags().get("tag1").equals("value1")) fail("tag does not matches");
        if(!message.getPrefix().equals("Test!Unit@Host.fr")) fail("prefix does not matches");
        if(message.getCommand() != CommandTypes.JOIN) fail("command does not matches");
        if(!message.getArguments().get(0).equals("#channel")) fail("argument does not matches");
    }
}
