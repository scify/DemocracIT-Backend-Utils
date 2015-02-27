/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess.dbc;

import java.util.Iterator;
import org.scify.democracit.model.Comment;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public interface ICommentsDBCRetriever {

    public Iterator<Comment> getCommentIteratorPerConsultationID(long iConsultationID);

    public Iterator<Comment> getCommentIteratorPerArticleID(long iArticleID);
}
