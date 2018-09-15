import com.github.mandelV.IRCClient.Parser.CommandTypes;
import com.github.mandelV.IRCClient.Parser.IRCMessage;
import com.github.mandelV.IRCClient.Parser.IRCParser;
import com.github.mandelV.IRCClient.Parser.PrefixPosition;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;


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
        Assert.assertFalse("Test Wrong entry : @randomEntry", IRCParser.parseV2("@" + randomStringGenerator(100)).isPresent());

        for(int i = 0; i < 10; i ++)
            Assert.assertFalse("Test Wrong entry : :randomEntry!randomEntry@randomEntry randomEntry #randomEntry :randomEntry", IRCParser.parseV2(":"
                    + randomStringGenerator(64)
                    + "!"
                    + randomStringGenerator(65)
                    + "@"
                    + randomStringGenerator(64)
                    + " "
                    + randomStringGenerator(50)
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

        Assert.assertTrue(IRCParser.parseV2(":Test!Unit@Host.fr JOIN #channel").isPresent());
        Assert.assertTrue(IRCParser.parseV2("@tag=1;tag2=2 :Test!Unit@Host.fr join #channel arg2 :Ceci est un Trailling !").isPresent());





        IRCParser.parseV2(":Test!Unit@Host.fr JOIN #channel").ifPresent(message -> {

            Assert.assertEquals("Test prefix of : Test!Unit@Host.fr JOIN #channel", ":Test!Unit@Host.fr", message.getPrefix());
            Assert.assertEquals("Test Command of : Test!Unit@Host.fr JOIN #channel", CommandTypes.JOIN, message.getCommand());
            Assert.assertEquals("Test Arguments of : Test!Unit@Host.fr JOIN #channel","#channel", message.getArguments().get(0));


            List<String> testPrefix = new ArrayList<>();
            testPrefix.add("Test");
            testPrefix.add("Unit");
            testPrefix.add("Host.fr");

            Assert.assertThat("Test Parsedprefix of", testPrefix,is(message.getParsedPrefix()));
        });

        IRCParser.parseV2("@tag=1;tag2=2 :Test!Unit@Host.fr join #channel arg2 :Ceci est un Trailling !").ifPresent(message -> {

            String testMessage = "@tag=1;tag2=2 :Test!Unit@Host.fr join #channel arg2 :Ceci est un Trailling !";
            //prefix
            Assert.assertEquals("Test prefix : " + testMessage, ":Test!Unit@Host.fr", message.getPrefix());



            Assert.assertEquals("Test parsedPrefix : " + testMessage,"Test", message.getPrefix(PrefixPosition.FIRST));
            Assert.assertEquals("Test parsedPrefix : " + testMessage,"Unit", message.getPrefix(PrefixPosition.SECOND));
            Assert.assertEquals("Test parsedPrefix : " + testMessage,"Host.fr", message.getPrefix(PrefixPosition.THIRD));


            //command
            Assert.assertEquals("Test command : " +testMessage, CommandTypes.JOIN, message.getCommand());

            //Argument
            Assert.assertEquals("Test Argument : " +testMessage,"#channel", message.getArguments().get(0));
            Assert.assertEquals("Test Argument : " +testMessage,"arg2", message.getArguments().get(1));

            //TAGS
            Assert.assertEquals("Test TAGS : " +testMessage, message.getTags().get("tag"), "1");
            Assert.assertEquals("Test TAGS : " +testMessage, message.getTags().get("tag2"), "2");

            //TRAILLING
            Assert.assertEquals("Test TRAILLING : " +testMessage, "Ceci est un Trailling !", message.getTrailing());
        });
    }
}
