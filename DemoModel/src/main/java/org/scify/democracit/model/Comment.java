/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.model;

import java.sql.Timestamp;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class Comment {

    private long id;
    private String url_source;
    private long article_id;
    private long parent_id;
    private String comment;
    private long source_type_id;
    private long discussion_thread_id;
    private long user_id;
    private Timestamp date_added;
    private int revision;
    private String depth;
    private long initialid;

    public Comment(long id, String url_source, long article_id,
            long parent_id, String comment, long source_type_id,
            long discussion_thread_id, long user_id, Timestamp date_added,
            int revision, String depth, long initialId) {
        this.id = id;
        this.url_source = url_source;
        this.article_id = article_id;
        this.parent_id = parent_id;
        this.comment = comment;
        this.source_type_id = source_type_id;
        this.discussion_thread_id = discussion_thread_id;
        this.user_id = user_id;
        this.date_added = date_added;
        this.revision = revision;
        this.depth = depth;
        this.initialid = initialId;
    }

    public long getID() {
        return id;
    }

    public String getUrlSource() {
        return url_source;
    }

    public long getArticleID() {
        return article_id;
    }

    public long getParentID() {
        return parent_id;
    }

    public String getComment() {
        return comment;
    }

    public long getSourceTypeID() {
        return source_type_id;
    }

    public long getDiscussionThreadID() {
        return discussion_thread_id;
    }

    public long getUserID() {
        return user_id;
    }

    public Timestamp getDateAdded() {
        return date_added;
    }

    public int getRevision() {
        return revision;
    }

    public String getDepth() {
        return depth;
    }

    public long getInitialID() {
        return initialid;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 67 * hash + (this.url_source != null ? this.url_source.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comment other = (Comment) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.url_source == null) ? (other.url_source != null) : !this.url_source.equals(other.url_source)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", url_source=" + url_source + ", article_id=" + article_id + ", parent_id=" + parent_id + ", comment=" + comment + ", source_type_id=" + source_type_id + ", discussion_thread_id=" + discussion_thread_id + ", user_id=" + user_id + ", date_added=" + date_added + ", revision=" + revision + ", depth=" + depth + ", initialid=" + initialid + '}';
    }

}
