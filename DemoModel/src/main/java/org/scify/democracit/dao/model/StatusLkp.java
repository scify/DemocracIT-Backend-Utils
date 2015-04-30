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
@Table(name = "log.status_lkp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StatusLkp.findAll", query = "SELECT s FROM StatusLkp s"),
    @NamedQuery(name = "StatusLkp.findById", query = "SELECT s FROM StatusLkp s WHERE s.id = :id"),
    @NamedQuery(name = "StatusLkp.findByStatusDescription", query = "SELECT s FROM StatusLkp s WHERE s.statusDescription = :statusDescription")})
public class StatusLkp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "status_description")
    private String statusDescription;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statusId")
    private Collection<Activities> activitiesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statusId")
    private Collection<ActivitySteps> activityStepsCollection;

    public StatusLkp() {
    }

    public StatusLkp(Short id) {
        this.id = id;
    }

    public StatusLkp(Short id, String statusDescription) {
        this.id = id;
        this.statusDescription = statusDescription;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    @XmlTransient
    public Collection<Activities> getActivitiesCollection() {
        return activitiesCollection;
    }

    public void setActivitiesCollection(Collection<Activities> activitiesCollection) {
        this.activitiesCollection = activitiesCollection;
    }

    @XmlTransient
    public Collection<ActivitySteps> getActivityStepsCollection() {
        return activityStepsCollection;
    }

    public void setActivityStepsCollection(Collection<ActivitySteps> activityStepsCollection) {
        this.activityStepsCollection = activityStepsCollection;
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
        if (!(object instanceof StatusLkp)) {
            return false;
        }
        StatusLkp other = (StatusLkp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.StatusLkp[ id=" + id + " ]";
    }

    
}
