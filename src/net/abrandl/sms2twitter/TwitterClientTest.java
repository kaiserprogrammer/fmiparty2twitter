package net.abrandl.sms2twitter;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.junit.*;

public class TwitterClientTest {

  @BeforeClass
  public static void loggerSetup() {
    BasicConfigurator.configure();
  }

  @Test
  public void testRateLimitRetrieval() throws Exception {
    TwitterClient twitter = new TwitterClient("kosjb@web.de", TwitterClient.makeDefaultOAuthClient());
    assertTrue(twitter.twitter.getRateLimitStatus() > 0);
  }

  @Ignore
  @Test
  public void testPosting() {
    TwitterClient twitter = new TwitterClient("kosjb@web.de", TwitterClient.makeDefaultOAuthClient());
    twitter.tweet("blub");
  }

}
