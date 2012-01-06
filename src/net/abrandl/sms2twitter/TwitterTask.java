package net.abrandl.sms2twitter;

import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author abrandl
 */
public class TwitterTask implements Runnable {

    final private static Logger log = Logger.getLogger(TwitterTask.class);

    final private TwitterClient twitter;
    final private SmsProvider provider;

    private String append = null;

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

    private void handle(String msg) {

        log.debug("Handling message " + msg);

        int max = (append != null) ? TWEET_LENGTH - append.length() : TWEET_LENGTH;

        msg = msg.substring(0, Math.min(msg.length(), max));


        if (append != null) {
            msg += append;
        }

        twitter.tweet(msg);
    }

    @Override
    public void run() {
        try {
            List<String> inbox = provider.poll();

            for (String msg : inbox) {
                handle(msg);
            }

        } catch (Exception ex) {
            log.error(ex, ex);
        }
    }
}
