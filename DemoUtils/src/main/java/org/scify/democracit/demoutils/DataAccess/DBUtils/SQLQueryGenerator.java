/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess.DBUtils;

/**
 *
 * @author George K.<gkiom@scify.org>
 */
public class SQLQueryGenerator {

    public String generateCommentsSQLPerConsultation(long iConsultationID) {
        String SQL = StaticQuery.GET_COMMENTS_FOR_A_CONSULTATION.substring(0, StaticQuery.GET_COMMENTS_FOR_A_CONSULTATION.indexOf("?"));
        String concat = SQL.concat(String.valueOf(iConsultationID)).concat(";");
        return concat;

    }

    public String generateCommentsSQLPerArticle(long iArticleID) {
        String SQL = StaticQuery.GET_COMMENTS_FOR_AN_ARTICLE.substring(0, StaticQuery.GET_COMMENTS_FOR_AN_ARTICLE.indexOf("?"));
        String concat = SQL.concat(String.valueOf(iArticleID)).concat(";");
        return concat;
    }

    public static class StaticQuery {

        public static String GET_COMMENTS_FOR_AN_ARTICLE
                = "SELECT id, url_source, article_id, parent_id, comment, source_type_id, discussion_thread_id, user_id, date_added "
                + "FROM comments "
                + "WHERE article_id = ?;";

        public static String GET_COMMENTS_FOR_A_CONSULTATION
                = "SELECT comments.id, url_source, article_id, parent_id, comment, source_type_id, discussion_thread_id, user_id, date_added "
                + "FROM comments "
                + "INNER JOIN articles ON articles.id = comments.article_id "
                + "WHERE articles.consultation_id = ?;";

    }
}
