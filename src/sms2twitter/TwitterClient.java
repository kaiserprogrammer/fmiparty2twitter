package sms2twitter;

import org.apache.log4j.Logger;

import winterwell.jtwitter.*;

public class TwitterClient {

    final private static Logger log = Logger.getLogger(TwitterClient.class);

    public Twitter twitter;
    private static final String TWITTER_OAUTH_KEY = "";
    private static final String TWITTER_OAUTH_SECRET = "";
    private static final String ACCESS_TOKEN = "";
    private static final String ACCESS_TOKEN_SECRET = "";

    public static OAuthSignpostClient makeDefaultOAuthClient() {
        return new OAuthSignpostClient(TWITTER_OAUTH_KEY, TWITTER_OAUTH_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
    }

    public TwitterClient(String username, OAuthSignpostClient oauthClient) {
        twitter = new Twitter(username, oauthClient);
    }

    public void tweet(String text) {
        log.info("tweet: " + text);
        twitter.setStatus(text);
    }

    public int getRateLimitLeft() {
      return twitter.getRateLimitStatus();
    }
}
