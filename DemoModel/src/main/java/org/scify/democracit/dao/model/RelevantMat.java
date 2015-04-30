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
@Table(name = "relevant_mat")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RelevantMat.findAll", query = "SELECT r FROM RelevantMat r"),
    @NamedQuery(name = "RelevantMat.findById", query = "SELECT r FROM RelevantMat r WHERE r.id = :id"),
    @NamedQuery(name = "RelevantMat.findByTitle", query = "SELECT r FROM RelevantMat r WHERE r.title = :title"),
    @NamedQuery(name = "RelevantMat.findByUrlSource", query = "SELECT r FROM RelevantMat r WHERE r.urlSource = :urlSource"),
    @NamedQuery(name = "RelevantMat.findByActualPdfUrl", query = "SELECT r FROM RelevantMat r WHERE r.actualPdfUrl = :actualPdfUrl"),
    @NamedQuery(name = "RelevantMat.findByMd5Hash", query = "SELECT r FROM RelevantMat r WHERE r.md5Hash = :md5Hash"),
    @NamedQuery(name = "RelevantMat.findByRelativePath", query = "SELECT r FROM RelevantMat r WHERE r.relativePath = :relativePath")})
public class RelevantMat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Column(name = "url_source")
    private String urlSource;
    @Basic(optional = false)
    @Column(name = "actual_pdf_url")
    private String actualPdfUrl;
    @Basic(optional = false)
    @Column(name = "md5_hash")
    private String md5Hash;
    @Column(name = "relative_path")
    private String relativePath;
    @JoinColumn(name = "consultation_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Consultation consultationId;

    public RelevantMat() {
    }

    public RelevantMat(Long id) {
        this.id = id;
    }

    public RelevantMat(Long id, String urlSource, String actualPdfUrl, String md5Hash) {
        this.id = id;
        this.urlSource = urlSource;
        this.actualPdfUrl = actualPdfUrl;
        this.md5Hash = md5Hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlSource() {
        return urlSource;
    }

    public void setUrlSource(String urlSource) {
        this.urlSource = urlSource;
    }

    public String getActualPdfUrl() {
        return actualPdfUrl;
    }

    public void setActualPdfUrl(String actualPdfUrl) {
        this.actualPdfUrl = actualPdfUrl;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public Consultation getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Consultation consultationId) {
        this.consultationId = consultationId;
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
        if (!(object instanceof RelevantMat)) {
            return false;
        }
        RelevantMat other = (RelevantMat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.RelevantMat[ id=" + id + " ]";
    }
    
}
