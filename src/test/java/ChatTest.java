import com.github.mandelV.IRCClient.Chat.Chat;
import com.github.mandelV.IRCClient.Parser.IRCMessage;
import com.github.mandelV.IRCClient.Parser.IRCParser;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ChatTest {


    @BeforeClass
    public static void initChat(){
        Chat.getInstance();
    }

    @Test
    public void messageListTest(){

        IRCMessage message = IRCParser.parse(":Test!Unit@Host.fr QUIT :Bye people !");
        Chat.getInstance().pushMessage(message);

        if(Chat.getInstance().getMessages().size() != 1) fail("Error messageList");

        Chat.displayMsg(Chat.getInstance().getMessages().get(0).getOriginalRaw());


    }
}
