/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess.ds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import javax.swing.text.AbstractDocument.Content;
import org.scify.democracit.dao.model.Articles;
import org.scify.democracit.dao.model.Comments;
import org.scify.democracit.dao.model.DiscussionThread;
import org.scify.democracit.dao.model.SourceTypeLkp;
import org.scify.democracit.dao.model.Users;
import org.scify.democracit.demoutils.DataAccess.DBUtils.SQLQueryGenerator;
import org.scify.democracit.demoutils.DataAccess.DBUtils.SQLUtils;

/**
 * Fetches required comments from the DB. Can be instantiated with a DataSource
 * resource
 *
 * @author George K.<gkiom@scify.org>
 */
public class CommentsDSRetriever extends AbstractDSAccess implements ICommentsRetriever {

    /**
     * Preferred method
     *
     * @param dataSource the {@link DataSource} resource required for connection
     * handling
     */
    public CommentsDSRetriever(DataSource dataSource) {
        super(dataSource);
    }

    /**
     *
     * @param iConsultationID the consultation ID that the comments belong to
     * @return the {@link Content} that is relevant to the parameters
     * @throws SQLException
     */
    @Override
    public List<Comments> getCommentsPerConsultationID(int iConsultationID) throws SQLException {
        System.out.println("\tAcquiring comments...");
        List lsRes = new ArrayList<>();
        // get the appropriate sql
        String SQL_SELECT = SQLQueryGenerator.StaticQuery.GET_COMMENTS_FOR_A_CONSULTATION;
        // get content for this SQL
        lsRes.addAll(getCommentsFromDB(SQL_SELECT, iConsultationID));
        System.out.format("\t%d comments acquired%n", lsRes.size());
        return lsRes;
    }

    /**
     *
     * @param iArticleID the consultation ID that the comments belong to
     * @return the {@link Comments} that is relevant to the parameters
     * @throws SQLException
     */
    @Override
    public List<Comments> getCommentsPerArticleID(int iArticleID) throws SQLException {
        System.out.println("\tAcquiring comments...");
        List lsRes = new ArrayList<>();
        // get the appropriate sql
        String SQL_SELECT = SQLQueryGenerator.StaticQuery.GET_COMMENTS_FOR_AN_ARTICLE;
        // get content for this SQL
        lsRes.addAll(getCommentsFromDB(SQL_SELECT, iArticleID));
        System.out.format("\t%d comments acquired%n", lsRes.size());
        return lsRes;
    }

    /**
     *
     * @param sql The SQL SELECT to apply
     * @return a list of {@link Comments} returned from the DB. If no results
     * found, return an empty list
     * @throws SQLException
     */
    private List<Comments> getCommentsFromDB(String sql, int iConOrArtID) throws SQLException {
        List<Comments> lsRes = new ArrayList<>();
        Connection dbConn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            dbConn = dataSource.getConnection();

            preparedStatement = dbConn.prepareStatement(sql);
            preparedStatement.setLong(1, iConOrArtID);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                String url_source = resultSet.getString(2);
                long article_id = resultSet.getLong(3);
                long parent_id = resultSet.getLong(4);
                String comment = resultSet.getString(5);
                long source_type_id = resultSet.getLong(6);
                long discussion_thread_id = resultSet.getLong(7);
                long user_id = resultSet.getLong(8);
                Timestamp date_added = resultSet.getTimestamp(9);
                // if content is OK
                if (comment != null
                        && !comment.trim().isEmpty()) {
                    Comments tmp = new Comments(id);
                    tmp.setUrlSource(url_source);
                    tmp.setArticleId(new Articles(article_id));
                    tmp.setParentId(new Comments(parent_id));
                    tmp.setComment(comment);
                    tmp.setSourceTypeId(new SourceTypeLkp((short) source_type_id));
                    tmp.setDiscussionThreadId(new DiscussionThread(discussion_thread_id));
                    tmp.setUserId(new Users(user_id));
                    tmp.setDateAdded(date_added);
                    // add to result list
                    lsRes.add(tmp);
                }
            }
        } finally {
            SQLUtils.release(dbConn, preparedStatement, resultSet);
        }
        return lsRes;
    }
}
