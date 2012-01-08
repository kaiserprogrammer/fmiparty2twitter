package net.abrandl.sms2twitter;

import java.util.*;
import org.apache.log4j.Logger;

public class TwitterTask implements Runnable {

    final private static Logger log = Logger.getLogger(TwitterTask.class);

    final private TwitterClient twitter;
    final private SmsProvider provider;

    private String append = "";

    final public static int TWEET_LENGTH = 140;

    public TwitterTask(TwitterClient twitter, SmsProvider provider) {
        this.twitter = twitter;
        this.provider = provider;
    }

    public String getAppend() {
        return append;
    }

    public void setAppend(String append) {
        this.append = append;
    }

    protected void sendMessage(String msg) {
        log.debug("Handling message " + msg);

        int max = TWEET_LENGTH - append.length();
        msg = msg.substring(0, Math.min(msg.length(), max));
        msg += append;

        twitter.tweet(msg);
    }

    @Override
    public void run() {
        List<String> inbox = new ArrayList<String>();
        try {
            inbox = provider.poll();
        } catch (Exception ex) {
            log.error(ex, ex);
        }

        for (int i = 0; i < inbox.size(); i++) {
            try {
                String msg = inbox.get(i);
                sendMessage(msg);
            } catch (Exception ex) {
                i--;
                try {
                    log.error("Waiting for 30 seconds now");
                    Thread.sleep(30000);
                } catch (InterruptedException e) {}
                log.error(ex, ex);
            }
        }
    }
}
