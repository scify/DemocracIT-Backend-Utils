/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.scify.democracit.dao.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
@Entity
@Table(name = "consultation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Consultation.findAll", query = "SELECT c FROM Consultation c"),
    @NamedQuery(name = "Consultation.findById", query = "SELECT c FROM Consultation c WHERE c.id = :id"),
    @NamedQuery(name = "Consultation.findByStartDate", query = "SELECT c FROM Consultation c WHERE c.startDate = :startDate"),
    @NamedQuery(name = "Consultation.findByEndDate", query = "SELECT c FROM Consultation c WHERE c.endDate = :endDate"),
    @NamedQuery(name = "Consultation.findByTitle", query = "SELECT c FROM Consultation c WHERE c.title = :title"),
    @NamedQuery(name = "Consultation.findByShortDescription", query = "SELECT c FROM Consultation c WHERE c.shortDescription = :shortDescription"),
    @NamedQuery(name = "Consultation.findByConsultationUrl", query = "SELECT c FROM Consultation c WHERE c.consultationUrl = :consultationUrl"),
    @NamedQuery(name = "Consultation.findByCompleted", query = "SELECT c FROM Consultation c WHERE c.completed = :completed"),
    @NamedQuery(name = "Consultation.findByCompletedText", query = "SELECT c FROM Consultation c WHERE c.completedText = :completedText"),
    @NamedQuery(name = "Consultation.findByReportText", query = "SELECT c FROM Consultation c WHERE c.reportText = :reportText"),
    @NamedQuery(name = "Consultation.findByReportUrl", query = "SELECT c FROM Consultation c WHERE c.reportUrl = :reportUrl"),
    @NamedQuery(name = "Consultation.findByNumOfArticles", query = "SELECT c FROM Consultation c WHERE c.numOfArticles = :numOfArticles")})
public class Consultation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Column(name = "short_description")
    private String shortDescription;
    @Basic(optional = false)
    @Column(name = "consultation_url")
    private String consultationUrl;
    @Column(name = "completed")
    private Short completed;
    @Column(name = "completed_text")
    private String completedText;
    @Column(name = "report_text")
    private String reportText;
    @Column(name = "report_url")
    private String reportUrl;
    @Column(name = "num_of_articles")
    private Integer numOfArticles;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consultationId")
    private Collection<RelevantMat> relevantMatCollection;
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    @ManyToOne
    private OrganizationLkp organizationId;
    @OneToMany(mappedBy = "consultationId")
    private Collection<Articles> articlesCollection;
    @OneToMany(mappedBy = "consultationId")
    private Collection<RelevantMaterial> relevantMaterialCollection;

    public Consultation() {
    }

    public Consultation(Long id) {
        this.id = id;
    }

    public Consultation(Long id, Date startDate, Date endDate, String title, String consultationUrl) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.consultationUrl = consultationUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getConsultationUrl() {
        return consultationUrl;
    }

    public void setConsultationUrl(String consultationUrl) {
        this.consultationUrl = consultationUrl;
    }

    public Short getCompleted() {
        return completed;
    }

    public void setCompleted(Short completed) {
        this.completed = completed;
    }

    public String getCompletedText() {
        return completedText;
    }

    public void setCompletedText(String completedText) {
        this.completedText = completedText;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public Integer getNumOfArticles() {
        return numOfArticles;
    }

    public void setNumOfArticles(Integer numOfArticles) {
        this.numOfArticles = numOfArticles;
    }

    @XmlTransient
    public Collection<RelevantMat> getRelevantMatCollection() {
        return relevantMatCollection;
    }

    public void setRelevantMatCollection(Collection<RelevantMat> relevantMatCollection) {
        this.relevantMatCollection = relevantMatCollection;
    }

    public OrganizationLkp getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(OrganizationLkp organizationId) {
        this.organizationId = organizationId;
    }

    @XmlTransient
    public Collection<Articles> getArticlesCollection() {
        return articlesCollection;
    }

    public void setArticlesCollection(Collection<Articles> articlesCollection) {
        this.articlesCollection = articlesCollection;
    }

    @XmlTransient
    public Collection<RelevantMaterial> getRelevantMaterialCollection() {
        return relevantMaterialCollection;
    }

    public void setRelevantMaterialCollection(Collection<RelevantMaterial> relevantMaterialCollection) {
        this.relevantMaterialCollection = relevantMaterialCollection;
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
        if (!(object instanceof Consultation)) {
            return false;
        }
        Consultation other = (Consultation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.Consultation[ id=" + id + " ]";
    }
    
}
