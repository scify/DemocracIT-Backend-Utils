/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.model;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class CommentOpenGov {

    private final long id;
    private int initialid;
    private String url_source;
    private String fullname;

    public CommentOpenGov(long id) {
        this.id = id;
    }

    public CommentOpenGov(long id, int initialid, String url_source, String fullname) {
        this.id = id;
        this.initialid = initialid;
        this.url_source = url_source;
        this.fullname = fullname;
    }

    public long getID() {
        return id;
    }

    public int getInitialID() {
        return initialid;
    }

    public String getUrlSource() {
        return url_source;
    }

    public String getFullName() {
        return fullname;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final CommentOpenGov other = (CommentOpenGov) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CommentOpenGov{" + "id=" + id
                + ", initialid=" + initialid
                + ", url_source=" + url_source
                + ", fullname=" + fullname
                + '}';
    }
}
