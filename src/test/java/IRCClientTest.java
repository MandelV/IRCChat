import com.github.mandelV.IRCClient.Chat.Chat;
import com.github.mandelV.IRCClient.Client.IRCClient;
import com.github.mandelV.IRCClient.Client.InstanciateException;
import com.github.mandelV.IRCClient.Parser.CommandTypes;
import com.github.mandelV.IRCClient.Parser.IRCMessage;
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
    public void connectionTest() throws InterruptedException {

        client.send("PING :00UNITTESTPING00");
        Thread.sleep(50);

        boolean pong = false;
        for(IRCMessage message : Chat.getInstance().getMessages()){

            if(message.getCommand() == CommandTypes.PONG && message.getTrailing().equals("00UNITTESTPING00")){
                pong = true;
                break;
            }
        }


        if(!pong ) fail("Test connection by Ping fail");

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
        if(client.isStop()) fail("Client has not stopped");
        if(threadClient.isAlive()) fail("Error thread is alive");

    }
}
