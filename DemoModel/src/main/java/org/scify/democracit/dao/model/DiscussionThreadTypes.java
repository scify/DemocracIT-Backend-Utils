/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.dao.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
@Entity
@Table(name = "discussion_thread_types")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiscussionThreadTypes.findAll", query = "SELECT d FROM DiscussionThreadTypes d"),
    @NamedQuery(name = "DiscussionThreadTypes.findById", query = "SELECT d FROM DiscussionThreadTypes d WHERE d.id = :id"),
    @NamedQuery(name = "DiscussionThreadTypes.findByDescription", query = "SELECT d FROM DiscussionThreadTypes d WHERE d.description = :description")})
public class DiscussionThreadTypes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeid")
    private Collection<DiscussionThread> discussionThreadCollection;

    public DiscussionThreadTypes() {
    }

    public DiscussionThreadTypes(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<DiscussionThread> getDiscussionThreadCollection() {
        return discussionThreadCollection;
    }

    public void setDiscussionThreadCollection(Collection<DiscussionThread> discussionThreadCollection) {
        this.discussionThreadCollection = discussionThreadCollection;
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
        if (!(object instanceof DiscussionThreadTypes)) {
            return false;
        }
        DiscussionThreadTypes other = (DiscussionThreadTypes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.DiscussionThreadTypes[ id=" + id + " ]";
    }
    
}
