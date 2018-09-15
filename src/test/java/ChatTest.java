import com.github.mandelV.IRCClient.Chat.Chat;
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

        IRCParser.parseV2(":Test!Unit@Host.fr QUIT :Bye people !").ifPresent(Chat.getInstance()::pushMessage);

        if(Chat.getInstance().getMessages().size()  == 0) fail("Error messageList size : " + Chat.getInstance().getMessages().size());

        Chat.displayMsg(Chat.getInstance().getMessages().get(0).getOriginalRaw());


    }
}
