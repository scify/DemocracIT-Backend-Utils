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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
@Entity
@Table(name = "comment_opengov")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommentOpengov.findAll", query = "SELECT c FROM CommentOpengov c"),
    @NamedQuery(name = "CommentOpengov.findByOpengovid", query = "SELECT c FROM CommentOpengov c WHERE c.opengovid = :opengovid"),
    @NamedQuery(name = "CommentOpengov.findByFullname", query = "SELECT c FROM CommentOpengov c WHERE c.fullname = :fullname"),
    @NamedQuery(name = "CommentOpengov.findById", query = "SELECT c FROM CommentOpengov c WHERE c.id = :id")})
public class CommentOpengov implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "opengovid")
    private Integer opengovid;
    @Column(name = "fullname")
    private String fullname;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Comments comments;

    public CommentOpengov() {
    }

    public CommentOpengov(Long id) {
        this.id = id;
    }

    public Integer getOpengovid() {
        return opengovid;
    }

    public void setOpengovid(Integer opengovid) {
        this.opengovid = opengovid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
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
        if (!(object instanceof CommentOpengov)) {
            return false;
        }
        CommentOpengov other = (CommentOpengov) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.CommentOpengov[ id=" + id + " ]";
    }
    
}
