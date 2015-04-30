/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.dao.jpacontroller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.scify.democracit.dao.model.CommentOpengov;
import org.scify.democracit.dao.model.Users;
import org.scify.democracit.dao.model.SourceTypeLkp;
import org.scify.democracit.dao.model.DiscussionThread;
import org.scify.democracit.dao.model.Comments;
import org.scify.democracit.dao.model.Articles;
import org.scify.democracit.dao.model.CommentTerm;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.exceptions.IllegalOrphanException;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class CommentsJpaController implements Serializable {

    public CommentsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comments comments) {
        if (comments.getCommentTermCollection() == null) {
            comments.setCommentTermCollection(new ArrayList<CommentTerm>());
        }
        if (comments.getCommentsCollection() == null) {
            comments.setCommentsCollection(new ArrayList<Comments>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CommentOpengov commentOpengov = comments.getCommentOpengov();
            if (commentOpengov != null) {
                commentOpengov = em.getReference(commentOpengov.getClass(), commentOpengov.getId());
                comments.setCommentOpengov(commentOpengov);
            }
            Users userId = comments.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                comments.setUserId(userId);
            }
            SourceTypeLkp sourceTypeId = comments.getSourceTypeId();
            if (sourceTypeId != null) {
                sourceTypeId = em.getReference(sourceTypeId.getClass(), sourceTypeId.getId());
                comments.setSourceTypeId(sourceTypeId);
            }
            DiscussionThread discussionThreadId = comments.getDiscussionThreadId();
            if (discussionThreadId != null) {
                discussionThreadId = em.getReference(discussionThreadId.getClass(), discussionThreadId.getId());
                comments.setDiscussionThreadId(discussionThreadId);
            }
            Comments parentId = comments.getParentId();
            if (parentId != null) {
                parentId = em.getReference(parentId.getClass(), parentId.getId());
                comments.setParentId(parentId);
            }
            Articles articleId = comments.getArticleId();
            if (articleId != null) {
                articleId = em.getReference(articleId.getClass(), articleId.getId());
                comments.setArticleId(articleId);
            }
            Collection<CommentTerm> attachedCommentTermCollection = new ArrayList<CommentTerm>();
            for (CommentTerm commentTermCollectionCommentTermToAttach : comments.getCommentTermCollection()) {
                commentTermCollectionCommentTermToAttach = em.getReference(commentTermCollectionCommentTermToAttach.getClass(), commentTermCollectionCommentTermToAttach.getId());
                attachedCommentTermCollection.add(commentTermCollectionCommentTermToAttach);
            }
            comments.setCommentTermCollection(attachedCommentTermCollection);
            Collection<Comments> attachedCommentsCollection = new ArrayList<Comments>();
            for (Comments commentsCollectionCommentsToAttach : comments.getCommentsCollection()) {
                commentsCollectionCommentsToAttach = em.getReference(commentsCollectionCommentsToAttach.getClass(), commentsCollectionCommentsToAttach.getId());
                attachedCommentsCollection.add(commentsCollectionCommentsToAttach);
            }
            comments.setCommentsCollection(attachedCommentsCollection);
            em.persist(comments);
            if (commentOpengov != null) {
                Comments oldCommentsOfCommentOpengov = commentOpengov.getComments();
                if (oldCommentsOfCommentOpengov != null) {
                    oldCommentsOfCommentOpengov.setCommentOpengov(null);
                    oldCommentsOfCommentOpengov = em.merge(oldCommentsOfCommentOpengov);
                }
                commentOpengov.setComments(comments);
                commentOpengov = em.merge(commentOpengov);
            }
            if (userId != null) {
                userId.getCommentsCollection().add(comments);
                userId = em.merge(userId);
            }
            if (sourceTypeId != null) {
                sourceTypeId.getCommentsCollection().add(comments);
                sourceTypeId = em.merge(sourceTypeId);
            }
            if (discussionThreadId != null) {
                discussionThreadId.getCommentsCollection().add(comments);
                discussionThreadId = em.merge(discussionThreadId);
            }
            if (parentId != null) {
                parentId.getCommentsCollection().add(comments);
                parentId = em.merge(parentId);
            }
            if (articleId != null) {
                articleId.getCommentsCollection().add(comments);
                articleId = em.merge(articleId);
            }
            for (CommentTerm commentTermCollectionCommentTerm : comments.getCommentTermCollection()) {
                Comments oldCommentIdOfCommentTermCollectionCommentTerm = commentTermCollectionCommentTerm.getCommentId();
                commentTermCollectionCommentTerm.setCommentId(comments);
                commentTermCollectionCommentTerm = em.merge(commentTermCollectionCommentTerm);
                if (oldCommentIdOfCommentTermCollectionCommentTerm != null) {
                    oldCommentIdOfCommentTermCollectionCommentTerm.getCommentTermCollection().remove(commentTermCollectionCommentTerm);
                    oldCommentIdOfCommentTermCollectionCommentTerm = em.merge(oldCommentIdOfCommentTermCollectionCommentTerm);
                }
            }
            for (Comments commentsCollectionComments : comments.getCommentsCollection()) {
                Comments oldParentIdOfCommentsCollectionComments = commentsCollectionComments.getParentId();
                commentsCollectionComments.setParentId(comments);
                commentsCollectionComments = em.merge(commentsCollectionComments);
                if (oldParentIdOfCommentsCollectionComments != null) {
                    oldParentIdOfCommentsCollectionComments.getCommentsCollection().remove(commentsCollectionComments);
                    oldParentIdOfCommentsCollectionComments = em.merge(oldParentIdOfCommentsCollectionComments);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comments comments) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comments persistentComments = em.find(Comments.class, comments.getId());
            CommentOpengov commentOpengovOld = persistentComments.getCommentOpengov();
            CommentOpengov commentOpengovNew = comments.getCommentOpengov();
            Users userIdOld = persistentComments.getUserId();
            Users userIdNew = comments.getUserId();
            SourceTypeLkp sourceTypeIdOld = persistentComments.getSourceTypeId();
            SourceTypeLkp sourceTypeIdNew = comments.getSourceTypeId();
            DiscussionThread discussionThreadIdOld = persistentComments.getDiscussionThreadId();
            DiscussionThread discussionThreadIdNew = comments.getDiscussionThreadId();
            Comments parentIdOld = persistentComments.getParentId();
            Comments parentIdNew = comments.getParentId();
            Articles articleIdOld = persistentComments.getArticleId();
            Articles articleIdNew = comments.getArticleId();
            Collection<CommentTerm> commentTermCollectionOld = persistentComments.getCommentTermCollection();
            Collection<CommentTerm> commentTermCollectionNew = comments.getCommentTermCollection();
            Collection<Comments> commentsCollectionOld = persistentComments.getCommentsCollection();
            Collection<Comments> commentsCollectionNew = comments.getCommentsCollection();
            List<String> illegalOrphanMessages = null;
            if (commentOpengovOld != null && !commentOpengovOld.equals(commentOpengovNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain CommentOpengov " + commentOpengovOld + " since its comments field is not nullable.");
            }
            for (CommentTerm commentTermCollectionOldCommentTerm : commentTermCollectionOld) {
                if (!commentTermCollectionNew.contains(commentTermCollectionOldCommentTerm)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CommentTerm " + commentTermCollectionOldCommentTerm + " since its commentId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (commentOpengovNew != null) {
                commentOpengovNew = em.getReference(commentOpengovNew.getClass(), commentOpengovNew.getId());
                comments.setCommentOpengov(commentOpengovNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                comments.setUserId(userIdNew);
            }
            if (sourceTypeIdNew != null) {
                sourceTypeIdNew = em.getReference(sourceTypeIdNew.getClass(), sourceTypeIdNew.getId());
                comments.setSourceTypeId(sourceTypeIdNew);
            }
            if (discussionThreadIdNew != null) {
                discussionThreadIdNew = em.getReference(discussionThreadIdNew.getClass(), discussionThreadIdNew.getId());
                comments.setDiscussionThreadId(discussionThreadIdNew);
            }
            if (parentIdNew != null) {
                parentIdNew = em.getReference(parentIdNew.getClass(), parentIdNew.getId());
                comments.setParentId(parentIdNew);
            }
            if (articleIdNew != null) {
                articleIdNew = em.getReference(articleIdNew.getClass(), articleIdNew.getId());
                comments.setArticleId(articleIdNew);
            }
            Collection<CommentTerm> attachedCommentTermCollectionNew = new ArrayList<CommentTerm>();
            for (CommentTerm commentTermCollectionNewCommentTermToAttach : commentTermCollectionNew) {
                commentTermCollectionNewCommentTermToAttach = em.getReference(commentTermCollectionNewCommentTermToAttach.getClass(), commentTermCollectionNewCommentTermToAttach.getId());
                attachedCommentTermCollectionNew.add(commentTermCollectionNewCommentTermToAttach);
            }
            commentTermCollectionNew = attachedCommentTermCollectionNew;
            comments.setCommentTermCollection(commentTermCollectionNew);
            Collection<Comments> attachedCommentsCollectionNew = new ArrayList<Comments>();
            for (Comments commentsCollectionNewCommentsToAttach : commentsCollectionNew) {
                commentsCollectionNewCommentsToAttach = em.getReference(commentsCollectionNewCommentsToAttach.getClass(), commentsCollectionNewCommentsToAttach.getId());
                attachedCommentsCollectionNew.add(commentsCollectionNewCommentsToAttach);
            }
            commentsCollectionNew = attachedCommentsCollectionNew;
            comments.setCommentsCollection(commentsCollectionNew);
            comments = em.merge(comments);
            if (commentOpengovNew != null && !commentOpengovNew.equals(commentOpengovOld)) {
                Comments oldCommentsOfCommentOpengov = commentOpengovNew.getComments();
                if (oldCommentsOfCommentOpengov != null) {
                    oldCommentsOfCommentOpengov.setCommentOpengov(null);
                    oldCommentsOfCommentOpengov = em.merge(oldCommentsOfCommentOpengov);
                }
                commentOpengovNew.setComments(comments);
                commentOpengovNew = em.merge(commentOpengovNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getCommentsCollection().remove(comments);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getCommentsCollection().add(comments);
                userIdNew = em.merge(userIdNew);
            }
            if (sourceTypeIdOld != null && !sourceTypeIdOld.equals(sourceTypeIdNew)) {
                sourceTypeIdOld.getCommentsCollection().remove(comments);
                sourceTypeIdOld = em.merge(sourceTypeIdOld);
            }
            if (sourceTypeIdNew != null && !sourceTypeIdNew.equals(sourceTypeIdOld)) {
                sourceTypeIdNew.getCommentsCollection().add(comments);
                sourceTypeIdNew = em.merge(sourceTypeIdNew);
            }
            if (discussionThreadIdOld != null && !discussionThreadIdOld.equals(discussionThreadIdNew)) {
                discussionThreadIdOld.getCommentsCollection().remove(comments);
                discussionThreadIdOld = em.merge(discussionThreadIdOld);
            }
            if (discussionThreadIdNew != null && !discussionThreadIdNew.equals(discussionThreadIdOld)) {
                discussionThreadIdNew.getCommentsCollection().add(comments);
                discussionThreadIdNew = em.merge(discussionThreadIdNew);
            }
            if (parentIdOld != null && !parentIdOld.equals(parentIdNew)) {
                parentIdOld.getCommentsCollection().remove(comments);
                parentIdOld = em.merge(parentIdOld);
            }
            if (parentIdNew != null && !parentIdNew.equals(parentIdOld)) {
                parentIdNew.getCommentsCollection().add(comments);
                parentIdNew = em.merge(parentIdNew);
            }
            if (articleIdOld != null && !articleIdOld.equals(articleIdNew)) {
                articleIdOld.getCommentsCollection().remove(comments);
                articleIdOld = em.merge(articleIdOld);
            }
            if (articleIdNew != null && !articleIdNew.equals(articleIdOld)) {
                articleIdNew.getCommentsCollection().add(comments);
                articleIdNew = em.merge(articleIdNew);
            }
            for (CommentTerm commentTermCollectionNewCommentTerm : commentTermCollectionNew) {
                if (!commentTermCollectionOld.contains(commentTermCollectionNewCommentTerm)) {
                    Comments oldCommentIdOfCommentTermCollectionNewCommentTerm = commentTermCollectionNewCommentTerm.getCommentId();
                    commentTermCollectionNewCommentTerm.setCommentId(comments);
                    commentTermCollectionNewCommentTerm = em.merge(commentTermCollectionNewCommentTerm);
                    if (oldCommentIdOfCommentTermCollectionNewCommentTerm != null && !oldCommentIdOfCommentTermCollectionNewCommentTerm.equals(comments)) {
                        oldCommentIdOfCommentTermCollectionNewCommentTerm.getCommentTermCollection().remove(commentTermCollectionNewCommentTerm);
                        oldCommentIdOfCommentTermCollectionNewCommentTerm = em.merge(oldCommentIdOfCommentTermCollectionNewCommentTerm);
                    }
                }
            }
            for (Comments commentsCollectionOldComments : commentsCollectionOld) {
                if (!commentsCollectionNew.contains(commentsCollectionOldComments)) {
                    commentsCollectionOldComments.setParentId(null);
                    commentsCollectionOldComments = em.merge(commentsCollectionOldComments);
                }
            }
            for (Comments commentsCollectionNewComments : commentsCollectionNew) {
                if (!commentsCollectionOld.contains(commentsCollectionNewComments)) {
                    Comments oldParentIdOfCommentsCollectionNewComments = commentsCollectionNewComments.getParentId();
                    commentsCollectionNewComments.setParentId(comments);
                    commentsCollectionNewComments = em.merge(commentsCollectionNewComments);
                    if (oldParentIdOfCommentsCollectionNewComments != null && !oldParentIdOfCommentsCollectionNewComments.equals(comments)) {
                        oldParentIdOfCommentsCollectionNewComments.getCommentsCollection().remove(commentsCollectionNewComments);
                        oldParentIdOfCommentsCollectionNewComments = em.merge(oldParentIdOfCommentsCollectionNewComments);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = comments.getId();
                if (findComments(id) == null) {
                    throw new NonexistentEntityException("The comments with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comments comments;
            try {
                comments = em.getReference(Comments.class, id);
                comments.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comments with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            CommentOpengov commentOpengovOrphanCheck = comments.getCommentOpengov();
            if (commentOpengovOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comments (" + comments + ") cannot be destroyed since the CommentOpengov " + commentOpengovOrphanCheck + " in its commentOpengov field has a non-nullable comments field.");
            }
            Collection<CommentTerm> commentTermCollectionOrphanCheck = comments.getCommentTermCollection();
            for (CommentTerm commentTermCollectionOrphanCheckCommentTerm : commentTermCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comments (" + comments + ") cannot be destroyed since the CommentTerm " + commentTermCollectionOrphanCheckCommentTerm + " in its commentTermCollection field has a non-nullable commentId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Users userId = comments.getUserId();
            if (userId != null) {
                userId.getCommentsCollection().remove(comments);
                userId = em.merge(userId);
            }
            SourceTypeLkp sourceTypeId = comments.getSourceTypeId();
            if (sourceTypeId != null) {
                sourceTypeId.getCommentsCollection().remove(comments);
                sourceTypeId = em.merge(sourceTypeId);
            }
            DiscussionThread discussionThreadId = comments.getDiscussionThreadId();
            if (discussionThreadId != null) {
                discussionThreadId.getCommentsCollection().remove(comments);
                discussionThreadId = em.merge(discussionThreadId);
            }
            Comments parentId = comments.getParentId();
            if (parentId != null) {
                parentId.getCommentsCollection().remove(comments);
                parentId = em.merge(parentId);
            }
            Articles articleId = comments.getArticleId();
            if (articleId != null) {
                articleId.getCommentsCollection().remove(comments);
                articleId = em.merge(articleId);
            }
            Collection<Comments> commentsCollection = comments.getCommentsCollection();
            for (Comments commentsCollectionComments : commentsCollection) {
                commentsCollectionComments.setParentId(null);
                commentsCollectionComments = em.merge(commentsCollectionComments);
            }
            em.remove(comments);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comments> findCommentsEntities() {
        return findCommentsEntities(true, -1, -1);
    }

    public List<Comments> findCommentsEntities(int maxResults, int firstResult) {
        return findCommentsEntities(false, maxResults, firstResult);
    }

    private List<Comments> findCommentsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comments.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Comments findComments(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comments.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommentsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comments> rt = cq.from(Comments.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
