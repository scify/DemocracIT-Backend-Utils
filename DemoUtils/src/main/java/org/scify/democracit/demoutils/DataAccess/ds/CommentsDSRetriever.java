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
import org.scify.democracit.demoutils.DataAccess.DBUtils.SQLQueryGenerator;
import org.scify.democracit.demoutils.DataAccess.DBUtils.SQLUtils;
import org.scify.democracit.model.Comment;

/**
 * Fetches required comments from the DB. Can be instantiated with a DataSource
 * resource
 *
 * @author George K.<gkiom@scify.org>
 */
public class CommentsDSRetriever extends AbstractDSAccess implements ICommentsDSRetriever {

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
    public List<Comment> getCommentsPerConsultationID(int iConsultationID) throws SQLException {
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
     * @return the {@link Content} that is relevant to the parameters
     * @throws SQLException
     */
    @Override
    public List<Comment> getCommentsPerArticleID(int iArticleID) throws SQLException {
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
     * @return a list of {@link Comment} returned from the DB. If no results
     * found, return an empty list
     * @throws SQLException
     */
    private List<Comment> getCommentsFromDB(String sql, int iConOrArtID) throws SQLException {
        List<Comment> lsRes = new ArrayList<>();
        Connection dbConn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // tell the driver to fetch one row at a time
//            preparedStatement = dbConnection.prepareStatement(sql,
//                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//            preparedStatement.setFetchSize(Integer.MIN_VALUE);

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
                    // return it    
                    lsRes.add(new Comment(id, url_source, article_id, parent_id, comment, source_type_id, discussion_thread_id, user_id, date_added));
                }
            }
        } finally {
            SQLUtils.release(dbConn, preparedStatement, resultSet);
        }
        return lsRes;
    }

//    public static void main(String[] args) {
//
//        Connection dbConnection = null;
////        String databaseHost = "mysql://localhost:3306/nomad";
//        String databaseHost = "mysql://localhost:23306/nomad";
//        String databaseUsername = "nomad";
//        String databasePassword = "POI098asd";
//        try {
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            dbConnection = DriverManager.getConnection("jdbc:"
//                    + databaseHost, databaseUsername, databasePassword);
//
//            CommentsDSRetriever getme = new CommentsDSRetriever(dbConnection);
//
//            System.out.println(getme.getPolicySegments(6, 2));
//
////            List<Content> lstest = getme
////                .getContent(0, 4, null, null, 100, null);
////            System.out.println(lstest.size());
////            List<Segment> lsseg = getme
////                .getSegments(0, 4, null, "5", null);
////            System.out.println(lsseg.size());
////            int iCount = 0;
////            for (Segment each : lsseg) {
////                iCount++;
////                System.out.println(each.getID());
////                System.out.println(each.getSegmentText());
////                System.out.println(each.getSegmentLanguage());
////                System.out.println(each.getParentContentID());
////                System.out.println("=========================================");
////                if (iCount == 10) {
////                    break;
////                }
////            }
////            getme.getSegmentTermFreqMap(0, 6, "5", null, 1, 1, null);
////            String[] sp = new String[]{"74, 75"};
////            getme.getContentCount(0, 6, "18", null, "el");
//            int iCount = 0;
//            Iterator<Content> iContent = getme.getCommentIterator(0, 5, "4", null, "en");
//            while (iContent.hasNext()) {
//                iCount++;
//                Content tmp = iContent.next();
//                System.out.println(tmp.getID());
//                System.out.println(tmp.getLanguageID());
//                System.out.println("");
//            }
//            System.out.println(iCount);
//
//        } catch (ClassNotFoundException ex) {
//            Logger.getAnonymousLogger().log(Level.SEVERE, ex.getMessage(), ex);
//        } catch (InstantiationException ex) {
//            Logger.getAnonymousLogger().log(Level.SEVERE, ex.getMessage(), ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getAnonymousLogger().log(Level.SEVERE, ex.getMessage(), ex);
//        } catch (SQLException ex) {
//            Logger.getAnonymousLogger().log(Level.SEVERE, ex.getMessage(), ex);
//        }
//
//    }
}
