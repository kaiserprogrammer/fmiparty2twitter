package net.abrandl.sms2twitter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author abrandl
 */
public class SmsProvider {

    final private static Logger log = Logger.getLogger(SmsProvider.class);

    final private Connection conn;
    private PreparedStatement select;
    private PreparedStatement update;

    public SmsProvider(Connection conn) {
        this.conn = conn;
    }

    private void prepare() throws SQLException {
        select = conn.prepareStatement("SELECT id, sendernumber, textdecoded, receivingdatetime FROM inbox WHERE tweeted = FALSE");
        update = conn.prepareStatement("UPDATE inbox SET tweeted = TRUE WHERE id=?");
    }

    synchronized public List<String> poll() {
        if (select == null || update == null) {
            try {
                prepare();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        log.info("Pooling for new short messages");

        List<String> messages = new LinkedList<String>();

        try {
            ResultSet result = select.executeQuery();

            while (result.next()) {

                String msg = result.getString("textdecoded");

                messages.add(msg);

                update.setInt(1, result.getInt("id"));
                update.executeUpdate();
            }

            conn.commit();

        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                log.error(ex1, ex1);
            }

            throw new RuntimeException(ex);
        }

        log.info(String.format("Got %d new short messages", messages.size()));

        return messages;

    }



}
