import com.github.mandelV.ircclient.chat.Chat;
import com.github.mandelV.ircclient.client.IRCClient;
import com.github.mandelV.ircclient.client.InstanciateException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * IRCClientTest
 * @author VAUBOURG Mandel
 */
public class IRCClientTest {

    private static IRCClient client;
    private static Thread threadClient;
    @BeforeClass
    public static void instanciateTest() throws InstanciateException {
        client = IRCClient.getInstance("localhost", 6667, "#help", "test", "unit", "localhost");

        if(IRCClient.getInstance() == null) fail("Error connect");

        threadClient = new Thread(client);
        threadClient.start();
    }
    @Test
    public void userInfoTest(){
        if(!client.getAddress().equals("localhost")) fail("error getAddress");

        if(client.getPort() != 6667) fail("error getPort");

        if(!client.getChannel().equals("#help")) fail("error getChannel");

        if(!client.getNickname().equals("test")) fail("error getNickname");

        if(!client.getName().equals("unit")) fail("error getName");

        if(!client.getDomain().equals("localhost")) fail("error getDomain");

    }
    @Test
    public void connectionTest(){

        client.send("ping :00UNITTESTPING00");
        if(!Chat.getInstance().getMessages().get(Chat.getInstance().getMessages().size()-1).getTrailing().equals("00UNITTESTPING00"))
            fail("Test connection by Ping fail");

    }

    @Test
    public void setChannelTest() throws InterruptedException {

        Thread.sleep(1000);
        client.send("join #help");
        if(!client.getChannel().equals("#help")) fail("set Channel failed");
        client.send("PRIVMSG " + client.getChannel() + " :" + "UNIT TESTS");

    }



    @AfterClass
    public static void stopTest() throws InterruptedException {

        client.send("QUIT :stopTEST");//Will stop the thread.
        Thread.sleep(1000);//Wait for stop Thread
        if(client.isStop()) fail("client has not stopped");
        if(threadClient.isAlive()) fail("Error thread is alive");

    }
}
