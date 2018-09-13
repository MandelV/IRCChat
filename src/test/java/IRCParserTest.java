import com.github.mandelV.IRCClient.Parser.CommandTypes;
import com.github.mandelV.IRCClient.Parser.IRCMessage;
import com.github.mandelV.IRCClient.Parser.IRCParser;
import com.github.mandelV.IRCClient.Parser.PrefixPosition;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.fail;

/**
 * IRCParserTest
 * @author VAUBOURG Mandel
 */
public class IRCParserTest {

    /**
     * Enable to generate random String.
     * @param size Size of the String that will be generated. (-1 for random size between 1 and 512)
     * @return Return random String.
     */
    private static String randomStringGenerator(int size){
        int randSize = size;
        Random rand = new Random();
        if(randSize == -1) randSize = rand.nextInt(512) + 1;

        char[] c = new char[randSize];
        for(int i = 0; i < c.length; i++) c[i] = (char)(rand.nextInt(128-32)+32);

        return new String(c);
    }
    /**
     * Test Parser with empty String.
     */
    @Test
    public void parseEmptyStringTest(){

        Assert.assertNull("parseEmptyStringTest : message is not  null", IRCParser.parse(""));
    }

    /**
     * Test Parser with wrong and random String.
     */
    @Test
    public void parseWrongStringTest(){

        //TEST whith Wrong String.
        Assert.assertNull("parseEmptyStringTest : message is not  null", IRCParser.parse("/"));
        Assert.assertNull("parseEmptyStringTest : message is not  null", IRCParser.parse("   dddd ddd ddd ddqdqsdqsd "));
        Assert.assertNull("parseEmptyStringTest : message is not  null", IRCParser.parse("                                                        "));
        Assert.assertNull("parseEmptyStringTest : message is not null", IRCParser.parse(":d@a d d d d :c"));
        Assert.assertNull("parseEmptyStringTest : message is not null", IRCParser.parse("@:@!"));

        //TEST with random String.
        Assert.assertNull("parseEmptyStringTest : message is not null", IRCParser.parse(":" + randomStringGenerator(-1)));
        Assert.assertNull("parseEmptyStringTest : message is not null", IRCParser.parse("@" + randomStringGenerator(-1)));
        Assert.assertNull("parseEmptyStringTest : message is not null", IRCParser.parse("  " + randomStringGenerator(-1)));
        Assert.assertNull("parseEmptyStringTest : message is not null", IRCParser.parse(":"
                + randomStringGenerator(64)
                + "!"
                + randomStringGenerator(64)
                + "@"
                + randomStringGenerator(64)
                + " "
                + randomStringGenerator(64)
                + "#"
                + randomStringGenerator(64)
        ));
    }

    /**
     * Test Parser with Right String and test if IRCMessage contain the right values.
     * @see IRCMessage
     */
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

        if(!message.getOriginalRaw().equals("@tag1=value1 :Test!Unit@Host.fr JOIN #channel")) fail("message don't matches");
        if(!message.getTags().get("tag1").equals("value1")) fail("tag does not matches");
        if(message.getParsedPrefix().size() != 3) fail("Wrong size prefix");
        if(!message.getPrefix().equals("Test!Unit@Host.fr")) fail("prefix does not matches");
        if(message.getCommand() != CommandTypes.JOIN) fail("command does not matches");
        if(!message.getArguments().get(0).equals("#channel")) fail("argument does not matches");
    }
}
