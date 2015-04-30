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
 * @author George K.<gkiom@iit.demokritos.gr>
 */
@Entity
@Table(name = "comment_term")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommentTerm.findAll", query = "SELECT c FROM CommentTerm c"),
    @NamedQuery(name = "CommentTerm.findById", query = "SELECT c FROM CommentTerm c WHERE c.id = :id"),
    @NamedQuery(name = "CommentTerm.findByTermString", query = "SELECT c FROM CommentTerm c WHERE c.termString = :termString"),
    @NamedQuery(name = "CommentTerm.findByTermFrequency", query = "SELECT c FROM CommentTerm c WHERE c.termFrequency = :termFrequency"),
    @NamedQuery(name = "CommentTerm.findByActive", query = "SELECT c FROM CommentTerm c WHERE c.active = :active"),
    @NamedQuery(name = "CommentTerm.findByNGramOrder", query = "SELECT c FROM CommentTerm c WHERE c.nGramOrder = :nGramOrder"),
    @NamedQuery(name = "CommentTerm.findByTermStringAndCommentID", 
            query = "SELECT c FROM CommentTerm c WHERE c.termString = :termString AND c.commentId= :commentId")
})
public class CommentTerm implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "term_string")
    private String termString;
    @Column(name = "term_frequency")
    private Integer termFrequency;
    @Column(name = "active")
    private Short active;
    @Basic(optional = false)
    @Column(name = "n_gram_order")
    private short nGramOrder;
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Comments commentId;

    public CommentTerm() {
    }

    public CommentTerm(Long id) {
        this.id = id;
    }

    public CommentTerm(Long id, short nGramOrder) {
        this.id = id;
        this.nGramOrder = nGramOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTermString() {
        return termString;
    }

    public void setTermString(String termString) {
        this.termString = termString;
    }

    public Integer getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(Integer termFrequency) {
        this.termFrequency = termFrequency;
    }

    public Short getActive() {
        return active;
    }

    public void setActive(Short active) {
        this.active = active;
    }

    public short getNGramOrder() {
        return nGramOrder;
    }

    public void setNGramOrder(short nGramOrder) {
        this.nGramOrder = nGramOrder;
    }

    public Comments getCommentId() {
        return commentId;
    }

    public void setCommentId(Comments commentId) {
        this.commentId = commentId;
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
        if (!(object instanceof CommentTerm)) {
            return false;
        }
        CommentTerm other = (CommentTerm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.CommentTerm[ id=" + id + " ]";
    }
    
}
