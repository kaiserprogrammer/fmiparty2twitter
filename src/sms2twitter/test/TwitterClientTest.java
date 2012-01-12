package sms2twitter.test;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.junit.*;

import sms2twitter.TwitterClient;

public class TwitterClientTest {

  private TwitterClient twitter;
  @BeforeClass
  public static void loggerSetup() {
    BasicConfigurator.configure();
  }

  @Before
  public void setUp() {
    twitter = new TwitterClient("kosjb@web.de", TwitterClient.makeDefaultOAuthClient());
  }

  @Test
  public void testRateLimitRetrieval() throws Exception {
    assertTrue(twitter.twitter.getRateLimitStatus() > 0);
  }

  @Ignore
  @Test
  public void testPosting() {
    TwitterClient twitter = new TwitterClient("kosjb@web.de", TwitterClient.makeDefaultOAuthClient());
    twitter.tweet("blub");
  }

}
