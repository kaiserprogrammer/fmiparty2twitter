package sms2twitter.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.BasicConfigurator;

import sms2twitter.*;

import winterwell.jtwitter.OAuthSignpostClient;

public class Main {

    public static void main(String[] args) throws Exception {

        BasicConfigurator.configure();

        String username = "fmipartysms";

        final String TWITTER_OAUTH_KEY = "";
        final String TWITTER_OAUTH_SECRET = "";
        final String ACCESS_TOKEN = "";
        final String ACCESS_TOKEN_SECRET = "";

        OAuthSignpostClient oauth = new OAuthSignpostClient(TWITTER_OAUTH_KEY, TWITTER_OAUTH_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
        TwitterClient twitter = new TwitterClient(username, oauth);

        Connection conn = DriverManager.getConnection("jdbc:postgresql://juergenbickert.de:5432/smsd?user=smsd");
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        SmsProvider smsProvider = new SmsProvider(conn);

        TwitterTask task = new TwitterTask(twitter, smsProvider);
        task.setAppend(" #tmn12");

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

        pool.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);

    }
}
