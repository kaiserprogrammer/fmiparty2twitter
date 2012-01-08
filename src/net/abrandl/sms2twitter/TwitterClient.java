package net.abrandl.sms2twitter;

import org.apache.log4j.Logger;

import winterwell.jtwitter.*;

public class TwitterClient {

    final private static Logger log = Logger.getLogger(TwitterClient.class);

    protected Twitter twitter;
    private static final String TWITTER_OAUTH_KEY = "jzl7xHs9svVflj8GG8pyFg";
    private static final String TWITTER_OAUTH_SECRET = "n98PLQlc8TGdzaJF2IG0DM308UJhq6A08YciZEnUjU";
    private static final String ACCESS_TOKEN = "88661852-PVkKrSCc0l0U6i3gIyRcLuzcD0vEt6zOLAnIebQqQ";
    private static final String ACCESS_TOKEN_SECRET = "ylP6OPKi25eHyCHmeltP6wClOn2wS2qdr3tLVdH0LF8";

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
}
