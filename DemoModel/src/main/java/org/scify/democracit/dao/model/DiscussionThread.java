/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.scify.democracit.dao.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
@Entity
@Table(name = "discussion_thread")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiscussionThread.findAll", query = "SELECT d FROM DiscussionThread d"),
    @NamedQuery(name = "DiscussionThread.findById", query = "SELECT d FROM DiscussionThread d WHERE d.id = :id"),
    @NamedQuery(name = "DiscussionThread.findByFrom", query = "SELECT d FROM DiscussionThread d WHERE d.from = :from"),
    @NamedQuery(name = "DiscussionThread.findByTo", query = "SELECT d FROM DiscussionThread d WHERE d.to = :to")})
public class DiscussionThread implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "from")
    private short from;
    @Basic(optional = false)
    @Column(name = "to")
    private short to;
    @OneToMany(mappedBy = "discussionThreadId")
    private Collection<Comments> commentsCollection;

    public DiscussionThread() {
    }

    public DiscussionThread(Long id) {
        this.id = id;
    }

    public DiscussionThread(Long id, short from, short to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getFrom() {
        return from;
    }

    public void setFrom(short from) {
        this.from = from;
    }

    public short getTo() {
        return to;
    }

    public void setTo(short to) {
        this.to = to;
    }

    @XmlTransient
    public Collection<Comments> getCommentsCollection() {
        return commentsCollection;
    }

    public void setCommentsCollection(Collection<Comments> commentsCollection) {
        this.commentsCollection = commentsCollection;
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
        if (!(object instanceof DiscussionThread)) {
            return false;
        }
        DiscussionThread other = (DiscussionThread) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.DiscussionThread[ id=" + id + " ]";
    }
    
}
