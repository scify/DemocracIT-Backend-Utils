/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.dao.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
@Entity
@Table(name = "discussion_thread")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiscussionThread.findAll", query = "SELECT d FROM DiscussionThread d"),
    @NamedQuery(name = "DiscussionThread.findById", query = "SELECT d FROM DiscussionThread d WHERE d.id = :id"),
    @NamedQuery(name = "DiscussionThread.findByRelatedtext", query = "SELECT d FROM DiscussionThread d WHERE d.relatedtext = :relatedtext"),
    @NamedQuery(name = "DiscussionThread.findByTagid", query = "SELECT d FROM DiscussionThread d WHERE d.tagid = :tagid")})
public class DiscussionThread implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "relatedtext")
    private String relatedtext;
    @Basic(optional = false)
    @Column(name = "tagid")
    private String tagid;
    @JoinColumn(name = "typeid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DiscussionThreadTypes typeid;

    public DiscussionThread() {
    }

    public DiscussionThread(Long id) {
        this.id = id;
    }

    public DiscussionThread(Long id, String relatedtext, String tagid) {
        this.id = id;
        this.relatedtext = relatedtext;
        this.tagid = tagid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelatedtext() {
        return relatedtext;
    }

    public void setRelatedtext(String relatedtext) {
        this.relatedtext = relatedtext;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public DiscussionThreadTypes getTypeid() {
        return typeid;
    }

    public void setTypeid(DiscussionThreadTypes typeid) {
        this.typeid = typeid;
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
