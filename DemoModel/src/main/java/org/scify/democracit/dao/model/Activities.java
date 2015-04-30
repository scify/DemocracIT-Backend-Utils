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
@Table(name = "log.activities")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Activities.findAll", query = "SELECT a FROM Activities a"),
    @NamedQuery(name = "Activities.findById", query = "SELECT a FROM Activities a WHERE a.id = :id"),
    @NamedQuery(name = "Activities.findByStartDate", query = "SELECT a FROM Activities a WHERE a.startDate = :startDate"),
    @NamedQuery(name = "Activities.findByEndDate", query = "SELECT a FROM Activities a WHERE a.endDate = :endDate"),
    @NamedQuery(name = "Activities.findByMessage", query = "SELECT a FROM Activities a WHERE a.message = :message")})
public class Activities implements Serializable {
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
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "message")
    private String message;
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private StatusLkp statusId;
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ModuleLkp moduleId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activityId")
    private Collection<ActivitySteps> activityStepsCollection;

    public Activities() {
    }

    public Activities(Long id) {
        this.id = id;
    }

    public Activities(Long id, Date startDate) {
        this.id = id;
        this.startDate = startDate;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StatusLkp getStatusId() {
        return statusId;
    }

    public void setStatusId(StatusLkp statusId) {
        this.statusId = statusId;
    }

    public ModuleLkp getModuleId() {
        return moduleId;
    }

    public void setModuleId(ModuleLkp moduleId) {
        this.moduleId = moduleId;
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
        if (!(object instanceof Activities)) {
            return false;
        }
        Activities other = (Activities) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.Activities[ id=" + id + " ]";
    }
    
}
