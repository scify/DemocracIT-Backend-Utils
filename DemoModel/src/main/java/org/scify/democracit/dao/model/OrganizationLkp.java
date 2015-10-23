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
@Table(name = "organization_lkp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrganizationLkp.findAll", query = "SELECT o FROM OrganizationLkp o"),
    @NamedQuery(name = "OrganizationLkp.findById", query = "SELECT o FROM OrganizationLkp o WHERE o.id = :id"),
    @NamedQuery(name = "OrganizationLkp.findByTitle", query = "SELECT o FROM OrganizationLkp o WHERE o.title = :title"),
    @NamedQuery(name = "OrganizationLkp.findByUrlInitial", query = "SELECT o FROM OrganizationLkp o WHERE o.urlInitial = :urlInitial"),
    @NamedQuery(name = "OrganizationLkp.findByUrlCollapsed", query = "SELECT o FROM OrganizationLkp o WHERE o.urlCollapsed = :urlCollapsed")})
public class OrganizationLkp implements Serializable {
    @Column(name = "group_title")
    private String groupTitle;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Column(name = "url_initial")
    private String urlInitial;
    @Column(name = "url_collapsed")
    private String urlCollapsed;
    @OneToMany(mappedBy = "organizationId")
    private Collection<Consultation> consultationCollection;

    public OrganizationLkp() {
    }

    public OrganizationLkp(Long id) {
        this.id = id;
    }

    public OrganizationLkp(Long id, String title) {
        this.id = id;
        this.title = title;
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

    public String getUrlInitial() {
        return urlInitial;
    }

    public void setUrlInitial(String urlInitial) {
        this.urlInitial = urlInitial;
    }

    public String getUrlCollapsed() {
        return urlCollapsed;
    }

    public void setUrlCollapsed(String urlCollapsed) {
        this.urlCollapsed = urlCollapsed;
    }

    @XmlTransient
    public Collection<Consultation> getConsultationCollection() {
        return consultationCollection;
    }

    public void setConsultationCollection(Collection<Consultation> consultationCollection) {
        this.consultationCollection = consultationCollection;
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
        if (!(object instanceof OrganizationLkp)) {
            return false;
        }
        OrganizationLkp other = (OrganizationLkp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.OrganizationLkp[ id=" + id + " ]";
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }
    
}
