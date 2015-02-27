/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess.dbc;

import java.sql.Connection;
import java.util.Iterator;
import org.scify.democracit.demoutils.DataAccess.DBUtils.SQLQueryGenerator;
import org.scify.democracit.model.Comment;

/**
 * Fetches required comments from the DB. Can be instantiated with a raw SQL
 * Connection resource
 *
 * @author George K. <gkiom@scify.org>
 */
public class CommentsDBCRetriever extends AbstractDBCAccess implements ICommentsDBCRetriever {

    public CommentsDBCRetriever(Connection dbConnection) {
        super(dbConnection);
    }

    /**
     *
     * @param iConsultationID the consultation ID that the comments belong to
     * @return a list of {@link Comment} returned from the nomad DB. If no
     * results found, return an empty list
     */
    @Override
    public Iterator<Comment> getCommentIteratorPerConsultationID(long iConsultationID) {
        System.out.println("\tAcquiring comments for consultation...");
        // get the appropriate sql
        String SQL = new SQLQueryGenerator().generateCommentsSQLPerConsultation(iConsultationID);
        // return an iterator on top of the result set
        if (dbConnection == null) {
            throw new IllegalArgumentException(DB_CONN_ERROR);
        }
        return new CommentIterator(dbConnection, SQL);
    }

    /**
     *
     * @param iArticleID the article ID that the comments belong to
     * @return a list of {@link Comment} returned from the nomad DB. If no
     * results found, return an empty list
     */
    @Override
    public Iterator<Comment> getCommentIteratorPerArticleID(long iArticleID) {
        System.out.println("\tAcquiring comments for article...");
        // get the appropriate sql
        String SQL = new SQLQueryGenerator().generateCommentsSQLPerArticle(iArticleID);
        // return an iterator on top of the result set
        if (dbConnection == null) {
            throw new IllegalArgumentException(DB_CONN_ERROR);
        }
        return new CommentIterator(dbConnection, SQL);
    }
}
