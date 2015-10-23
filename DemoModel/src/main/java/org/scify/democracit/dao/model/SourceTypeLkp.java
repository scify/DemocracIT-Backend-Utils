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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
@Entity
@Table(name = "source_type_lkp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SourceTypeLkp.findAll", query = "SELECT s FROM SourceTypeLkp s"),
    @NamedQuery(name = "SourceTypeLkp.findById", query = "SELECT s FROM SourceTypeLkp s WHERE s.id = :id"),
    @NamedQuery(name = "SourceTypeLkp.findBySourceType", query = "SELECT s FROM SourceTypeLkp s WHERE s.sourceType = :sourceType")})
public class SourceTypeLkp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Column(name = "source_type")
    private String sourceType;

    public SourceTypeLkp() {
    }

    public SourceTypeLkp(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
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
        if (!(object instanceof SourceTypeLkp)) {
            return false;
        }
        SourceTypeLkp other = (SourceTypeLkp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.SourceTypeLkp[ id=" + id + " ]";
    }
    
}
