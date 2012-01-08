package sms2twitter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.abrandl.sms2twitter.*;
import org.apache.log4j.BasicConfigurator;

import winterwell.jtwitter.OAuthSignpostClient;

public class Main {

    public static void main(String[] args) throws Exception {

        BasicConfigurator.configure();

        String username = "fmipartysms";

        final String TWITTER_OAUTH_KEY = "jzl7xHs9svVflj8GG8pyFg";
        final String TWITTER_OAUTH_SECRET = "n98PLQlc8TGdzaJF2IG0DM308UJhq6A08YciZEnUjU";
        final String ACCESS_TOKEN = "88661852-PVkKrSCc0l0U6i3gIyRcLuzcD0vEt6zOLAnIebQqQ";
        final String ACCESS_TOKEN_SECRET = "ylP6OPKi25eHyCHmeltP6wClOn2wS2qdr3tLVdH0LF8";

        OAuthSignpostClient oauth = new OAuthSignpostClient(TWITTER_OAUTH_KEY, TWITTER_OAUTH_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
        TwitterClient twitter = new TwitterClient(username, oauth);

        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test?user=twit&password=twit");
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        SmsProvider smsProvider = new SmsProvider(conn);

        TwitterTask task = new TwitterTask(twitter, smsProvider);
        task.setAppend(" #tmn12");

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

        pool.scheduleWithFixedDelay(task, 0, 5, TimeUnit.SECONDS);

    }
}
