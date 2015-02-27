/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess.ds;

import java.sql.SQLException;
import java.util.List;
import org.scify.democracit.model.Comment;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public interface ICommentsDSRetriever {

    public List<Comment> getCommentsPerConsultationID(int iConsultationID) throws SQLException;

    public List<Comment> getCommentsPerArticleID(int iArticleID) throws SQLException;

}
