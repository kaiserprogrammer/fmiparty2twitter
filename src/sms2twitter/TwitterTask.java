package sms2twitter;

import java.util.*;
import org.apache.log4j.Logger;

public class TwitterTask implements Runnable {

    final private static Logger log = Logger.getLogger(TwitterTask.class);

    final private TwitterClient twitter;
    final private SmsProvider provider;

    private String append = "";
    private HashMap<String, Boolean> seen = new HashMap<String, Boolean>();

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

    public void sendMessage(String msg) {
        log.debug("Handling message " + msg);
        msg = normalizeMessage(msg);
        twitter.tweet(msg);
    }

    private String normalizeMessage(String msg) {
        int max = TWEET_LENGTH - append.length();
        msg = msg.substring(0, Math.min(msg.length(), max));
        msg += append;
        msg = obfuscateMessage(msg);
        seen.put(msg, true);
        return msg;
    }

    private String obfuscateMessage(String orig) {
        Boolean alreadySeen = seen.get(orig);
        if (alreadySeen != null) {
            String msg = orig.substring(0, orig.length() - append.length());
            if (orig.length() == TWEET_LENGTH)
                msg = randomizeCase(msg);
            else
                msg = addPoints(msg);
            msg += append;
            log.info("duplicate converted to" + msg);
            return obfuscateMessage(msg);
        } else {
            return orig;
        }
    }

    private String addPoints(String msg) {
      return msg + ".";
    }

    private String randomizeCase(String msg) {
        int chars = msg.length();
        int rand = (int) Math.floor(chars * Math.random());
        String pre = msg.substring(0, rand);
        String middle = msg.substring(rand, rand + 1);
        String post = msg.substring(rand + 1, chars);
        return pre + middle.toUpperCase() + post;
    }

    @Override
    public void run() {
        List<String> inbox = new ArrayList<String>();
        log.info("RateLimitLeft: " + twitter.getRateLimitLeft());
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
                    log.error("Waiting for 1 seconds now");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                log.error(ex, ex);
            }
        }
    }

    public boolean seenMsg(String msg) {
        return seen.get(msg);
    }
}
