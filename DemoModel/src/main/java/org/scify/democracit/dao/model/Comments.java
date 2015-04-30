/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.dao.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
@Entity
@Table(name = "comments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comments.findAll", query = "SELECT c FROM Comments c"),
    @NamedQuery(name = "Comments.findById", query = "SELECT c FROM Comments c WHERE c.id = :id"),
    @NamedQuery(name = "Comments.findByUrlSource", query = "SELECT c FROM Comments c WHERE c.urlSource = :urlSource"),
    @NamedQuery(name = "Comments.findByComment", query = "SELECT c FROM Comments c WHERE c.comment = :comment"),
    @NamedQuery(name = "Comments.findByDateAdded", query = "SELECT c FROM Comments c WHERE c.dateAdded = :dateAdded"),
    @NamedQuery(name = "Comments.findByRevision", query = "SELECT c FROM Comments c WHERE c.revision = :revision"),
    @NamedQuery(name = "Comments.findByDepth", query = "SELECT c FROM Comments c WHERE c.depth = :depth")
})
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "url_source")
    private String urlSource;
    @Column(name = "comment")
    private String comment;
    @Basic(optional = false)
    @Column(name = "date_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdded;
    @Column(name = "revision")
    private Short revision;
    @Column(name = "depth")
    private String depth;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commentId")
    private Collection<CommentTerm> commentTermCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "comments")
    private CommentOpengov commentOpengov;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private Users userId;
    @JoinColumn(name = "source_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SourceTypeLkp sourceTypeId;
    @JoinColumn(name = "discussion_thread_id", referencedColumnName = "id")
    @ManyToOne
    private DiscussionThread discussionThreadId;
    @OneToMany(mappedBy = "parentId")
    private Collection<Comments> commentsCollection;
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    private Comments parentId;
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Articles articleId;

    public Comments() {
    }

    public Comments(Long id) {
        this.id = id;
    }

    public Comments(Long id, Date dateAdded) {
        this.id = id;
        this.dateAdded = dateAdded;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlSource() {
        return urlSource;
    }

    public void setUrlSource(String urlSource) {
        this.urlSource = urlSource;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Short getRevision() {
        return revision;
    }

    public void setRevision(Short revision) {
        this.revision = revision;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    @XmlTransient
    public Collection<CommentTerm> getCommentTermCollection() {
        return commentTermCollection;
    }

    public void setCommentTermCollection(Collection<CommentTerm> commentTermCollection) {
        this.commentTermCollection = commentTermCollection;
    }

    public CommentOpengov getCommentOpengov() {
        return commentOpengov;
    }

    public void setCommentOpengov(CommentOpengov commentOpengov) {
        this.commentOpengov = commentOpengov;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public SourceTypeLkp getSourceTypeId() {
        return sourceTypeId;
    }

    public void setSourceTypeId(SourceTypeLkp sourceTypeId) {
        this.sourceTypeId = sourceTypeId;
    }

    public DiscussionThread getDiscussionThreadId() {
        return discussionThreadId;
    }

    public void setDiscussionThreadId(DiscussionThread discussionThreadId) {
        this.discussionThreadId = discussionThreadId;
    }

    @XmlTransient
    public Collection<Comments> getCommentsCollection() {
        return commentsCollection;
    }

    public void setCommentsCollection(Collection<Comments> commentsCollection) {
        this.commentsCollection = commentsCollection;
    }

    public Comments getParentId() {
        return parentId;
    }

    public void setParentId(Comments parentId) {
        this.parentId = parentId;
    }

    public Articles getArticleId() {
        return articleId;
    }

    public void setArticleId(Articles articleId) {
        this.articleId = articleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comments)) {
            return false;
        }
        Comments other = (Comments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.Comments[ id=" + id + " ]";
    }

}
