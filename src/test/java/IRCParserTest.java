import com.github.mandelV.IRCClient.Parser.CommandTypes;
import com.github.mandelV.IRCClient.Parser.IRCMessage;
import com.github.mandelV.IRCClient.Parser.IRCParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

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

       Assert.assertFalse("parseEmptyStringTest : message is not  null", IRCParser.parseV2("").isPresent());
    }
    /**
     * Test Parser with wrong and random String.
     */
    @Test
    public void parseWrongStringTest(){

        Assert.assertFalse("Test Wrong entry : /", IRCParser.parseV2("/").isPresent());
        Assert.assertFalse("Test Wrong entry :    dddd ddd ddd ddqdqsdqsd ", IRCParser.parseV2("   dddd ddd ddd ddqdqsdqsd ").isPresent());
        Assert.assertFalse("Test Wrong entry : many spaces", IRCParser.parseV2("                       ").isPresent());
        Assert.assertFalse("Test Wrong entry : :d@a d d d d :c", IRCParser.parseV2(":d@a d d d d :c").isPresent());
        Assert.assertFalse("Test Wrong entry : @:@!", IRCParser.parseV2("@:@!").isPresent());

        for(int i = 0; i < 10; i ++)
        Assert.assertFalse("Test Wrong entry : :randomEntry", IRCParser.parseV2(":" + randomStringGenerator(64)).isPresent());

        for(int i = 0; i < 10; i ++)
        Assert.assertFalse("Test Wrong entry : @randomEntry", IRCParser.parseV2("@" + randomStringGenerator(64)).isPresent());

        for(int i = 0; i < 10; i ++)
            Assert.assertFalse("Test Wrong entry : :randomEntry!randomEntry@randomEntry randomEntry #randomEntry :randomEntry", IRCParser.parseV2(":"
                    + randomStringGenerator(64)
                    + "!"
                    + randomStringGenerator(64)
                    + "@"
                    + randomStringGenerator(64)
                    + " "
                    + randomStringGenerator(64)
                    + "#"
                    + randomStringGenerator(64)
            ).isPresent());
    }
    /**
     * Test Parser with Right String and test if IRCMessage contain the right values.
     * @see IRCMessage
     */
    @Test
    public void parseRightStringTest(){
        Assert.assertTrue("Test Right entry : :Test!Unit@Host.fr JOIN #channel ", IRCParser.parseV2(":Test!Unit@Host.fr JOIN #channel").isPresent());
        Assert.assertTrue("Test Right entry : :Test!Unit@Host.fr QUIT :Bye people !", IRCParser.parseV2(":Test!Unit@Host.fr QUIT :Bye people !").isPresent());
        Assert.assertTrue("Test Right entry : :Test!Unit@Host.fr JOIN #channel", IRCParser.parseV2(":Test!Unit@Host.fr JOIN #channel").isPresent());
        Assert.assertTrue("Test Right entry : @tag1=value1 :Test!Unit@Host.fr JOIN #channel", IRCParser.parseV2("@tag1=value1 :Test!Unit@Host.fr JOIN #channel").isPresent());

    }

    @Test
    public void parseTestContentMessage(){

        IRCParser.parseV2(":Test!Unit@Host.fr JOIN #channel").ifPresent(message -> {

            Assert.assertEquals("Test prefix of : Test!Unit@Host.fr JOIN #channel", ":Test!Unit@Host.fr", message.getPrefix());
            Assert.assertEquals("Test Command of : Test!Unit@Host.fr JOIN #channel", CommandTypes.JOIN, message.getCommand());

            Assert.assertEquals("Test Arguments of : Test!Unit@Host.fr JOIN #channel","#channel", message.getArguments().get(0));


        });


    }



}
