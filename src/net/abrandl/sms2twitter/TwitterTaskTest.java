package net.abrandl.sms2twitter;

import static org.junit.Assert.*;

import java.util.*;

import org.apache.log4j.BasicConfigurator;
import org.junit.*;

public class TwitterTaskTest {

    MockTwitterClient twitter;
    MockSmsProvider provider;
    TwitterTask task;

    @BeforeClass
    public static void setupLogging() {
        BasicConfigurator.configure();
    }

    @Before
    public void setup() {
        twitter = new MockTwitterClient();
        provider = new MockSmsProvider();
        task = new TwitterTask(twitter, provider);
    }

    @Test
    public void testTwitterTaskNoMessage() {
        provider.messages = 0;
        task.run();
        assertEquals(0, twitter.tweetedTimes);
    }

    @Test
    public void testTwitterTaskOneMessage() {
        provider.messages = 1;
        task.run();
        assertEquals(1, twitter.tweetedTimes);
    }

    @Test
    public void testTwitterTwoMessages() throws Exception {
        provider.messages = 2;
        task.run();
        assertEquals(2, twitter.tweetedTimes);
    }

    @Test
    public void testAppendedWordIsAppendedForShortMessages() throws Exception {
        task.setAppend("appended");
        task.sendMessage("not-relevant   uitrnae truinae ");
        assertTrue(twitter.lastMsg.contains("appended"));
    }

    @Test
    public void testAppendedWordIsAppendedForLongMessages() throws Exception {
        String shortMessage = "short";
        String longMessage = "long";
        for (int i = 0; i <= 140; i++)
            longMessage += shortMessage;
        task.setAppend("appended");
        task.sendMessage(longMessage);
        assertTrue(twitter.lastMsg.contains("appended"));

    }

    @Test
    public void testMessageIsSmallEnough() throws Exception {
        String shortMessage = "short";
        String longMessage = "long";
        for (int i = 0; i <= 140; i++)
            longMessage += shortMessage;
        task.sendMessage(longMessage);
        assertEquals(TwitterTask.TWEET_LENGTH, twitter.maxCharsTweetedMessage);
    }

    @Test
    public void testMessageIsSmallEnoughWithAppend() throws Exception {
        String shortMessage = "short";
        String longMessage = "long";
        for (int i = 0; i <= 140; i++)
            longMessage += shortMessage;
        task.setAppend("appended");
        task.sendMessage(longMessage);
        assertEquals(TwitterTask.TWEET_LENGTH, twitter.maxCharsTweetedMessage);
    }

    class MockTwitterClient extends TwitterClient {

        int tweetedTimes = 0;
        int maxCharsTweetedMessage = 0;
        String lastMsg = "";

        public MockTwitterClient() {
            super(null, null);
        }

        public void tweet(String msg) {
            tweetedTimes++;
            maxCharsTweetedMessage = Math.max(msg.length(), maxCharsTweetedMessage);
            lastMsg = msg;
        }
    }

    class MockSmsProvider extends SmsProvider {
        int messages = 0;
        public MockSmsProvider() {
            super(null);
        }

        public List<String> poll() {
            ArrayList<String> msgs = new ArrayList<String>();
            for (int i = 0; i < messages; i++)
                msgs.add(""+i);
            return msgs;
        }
    }
}
