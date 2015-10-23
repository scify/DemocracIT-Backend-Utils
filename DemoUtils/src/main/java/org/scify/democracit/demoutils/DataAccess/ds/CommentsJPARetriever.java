/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess.ds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.ArticlesJpaController;
import org.scify.democracit.dao.jpacontroller.CommentOpengovJpaController;
import org.scify.democracit.dao.jpacontroller.CommentTermJpaController;
import org.scify.democracit.dao.jpacontroller.CommentsJpaController;
import org.scify.democracit.dao.jpacontroller.ConsultationJpaController;
import org.scify.democracit.dao.model.Articles;
import org.scify.democracit.dao.model.Comments;
import org.scify.democracit.dao.model.Consultation;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class CommentsJPARetriever implements ICommentsRetriever {

    private static CommentsJPARetriever storage;

    private ConsultationJpaController consultationController;
    private CommentOpengovJpaController commentOpenGovController;
    private CommentsJpaController commentsController;
    private CommentTermJpaController commentTermController;
    private ArticlesJpaController articlesController;

    public synchronized static CommentsJPARetriever getInstance(EntityManagerFactory emf) {
        if (storage == null) {
            storage = new CommentsJPARetriever(emf);
        }
        return storage;
    }

    private CommentsJPARetriever(EntityManagerFactory emf) {
        this.consultationController = new ConsultationJpaController(emf);
        this.commentOpenGovController = new CommentOpengovJpaController(emf);
        this.commentsController = new CommentsJpaController(emf);
        this.commentTermController = new CommentTermJpaController(emf);
        this.articlesController = new ArticlesJpaController(emf);
    }

    @Override
    public List<Comments> getCommentsPerConsultationID(int iConsultationID) throws SQLException {
        List<Comments> comments = new ArrayList();
        Consultation cons = consultationController.findConsultation(new Long(iConsultationID));
        comments.addAll(consultationController.findCommentsPerConsultation(cons));
        return comments;
    }


    @Override
    public List<Comments> getCommentsPerArticleID(int iArticleID) throws SQLException {
        List<Comments> comments = new ArrayList();
        Articles article = articlesController.findArticles(new Long(iArticleID));
        comments.addAll(article.getCommentsCollection());
        return comments;
    }
}
