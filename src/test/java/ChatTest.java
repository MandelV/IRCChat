import com.github.mandelV.ircclient.chat.Chat;
import com.github.mandelV.ircclient.parser.IRCMessage;
import com.github.mandelV.ircclient.parser.IRCParser;
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

        if(Chat.getInstance().getMessages().size()  == 0) fail("Error messageList size : " + Chat.getInstance().getMessages().size());

        Chat.displayMsg(Chat.getInstance().getMessages().get(0).getOriginalRaw());


    }
}
