/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess.dbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.scify.democracit.demoutils.DataAccess.DBUtils.SqlQueryBlockIterable;
import org.scify.democracit.demoutils.DataAccess.DBUtils.SqlQueryBlockIterator;
import org.scify.democracit.model.Comment;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class AbstractDBCAccess {

    protected Connection dbConnection;

    public static final String DB_CONN_ERROR = "Provide a valid database connection";
    
    public AbstractDBCAccess(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Iterates {@link Comnment} on Top of a result set iterator
     *
     * @author George K.<gkiom@iit.demokritos.gr>
     */
    public class CommentIterator implements Iterator<Comment> {

        Connection dbConnection;
        SqlQueryBlockIterable iResultSet;
        SqlQueryBlockIterator iRSetIter;

        public CommentIterator(Connection dbCon, String sQuery) {
            this.iResultSet = new SqlQueryBlockIterable(dbCon, sQuery);
            this.iRSetIter = (SqlQueryBlockIterator) iResultSet.iterator();
        }

        @Override
        public boolean hasNext() {
            return iRSetIter.hasNext();
        }

        @Override
        public Comment next() {
            try {
                // Read next item
                ResultSet next = iRSetIter.next();
                // Create comment item
                long id = next.getLong(1);
                String url_source = next.getString(2);
                long article_id = next.getLong(3);
                long parent_id = next.getLong(4);
                String comment = next.getString(5);
                long source_type_id = next.getLong(6);
                long discussion_thread_id = next.getLong(7);
                long user_id = next.getLong(8);
                Timestamp date_added = next.getTimestamp(9);
                // if content is OK
                if (comment != null
                        && !comment.trim().isEmpty()) {
                    // return it    
                    return new Comment(id, url_source, article_id, parent_id, comment, source_type_id, discussion_thread_id, user_id, date_added);
                }
            } catch (SQLException ex) {
                Logger.getLogger(CommentIterator.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex) {
                Logger.getLogger(CommentIterator.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(getClass().getName() + ""
                    + " does not support removing items.");
        }
    }

}