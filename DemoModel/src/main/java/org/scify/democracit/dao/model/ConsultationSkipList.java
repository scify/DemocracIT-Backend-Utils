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
 * @author George K.<gkiom@iit.demokritos.gr>
 */
@Entity
@Table(name = "log.consultation_skip_list")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConsultationSkipList.findAll", query = "SELECT c FROM ConsultationSkipList c"),
    @NamedQuery(name = "ConsultationSkipList.findByUrl", query = "SELECT c FROM ConsultationSkipList c WHERE c.url = :url"),
    @NamedQuery(name = "ConsultationSkipList.findById", query = "SELECT c FROM ConsultationSkipList c WHERE c.id = :id")})
public class ConsultationSkipList implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    public ConsultationSkipList() {
    }

    public ConsultationSkipList(Long id) {
        this.id = id;
    }

    public ConsultationSkipList(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof ConsultationSkipList)) {
            return false;
        }
        ConsultationSkipList other = (ConsultationSkipList) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.ConsultationSkipList[ id=" + id + " ]";
    }
    
}
