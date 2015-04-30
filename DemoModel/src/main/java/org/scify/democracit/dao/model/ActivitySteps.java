/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.scify.democracit.dao.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
@Entity
@Table(name = "log.activity_steps")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivitySteps.findAll", query = "SELECT a FROM ActivitySteps a"),
    @NamedQuery(name = "ActivitySteps.findById", query = "SELECT a FROM ActivitySteps a WHERE a.id = :id"),
    @NamedQuery(name = "ActivitySteps.findByStartDate", query = "SELECT a FROM ActivitySteps a WHERE a.startDate = :startDate"),
    @NamedQuery(name = "ActivitySteps.findByEndDate", query = "SELECT a FROM ActivitySteps a WHERE a.endDate = :endDate"),
    @NamedQuery(name = "ActivitySteps.findByMessage", query = "SELECT a FROM ActivitySteps a WHERE a.message = :message")})
public class ActivitySteps implements Serializable {
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
    @Column(name = "message")
    private String message;
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private StatusLkp statusId;
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ActivityStepTypeLkp typeId;
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Activities activityId;

    public ActivitySteps() {
    }

    public ActivitySteps(Long id) {
        this.id = id;
    }

    public ActivitySteps(Long id, Date startDate, Date endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public ActivityStepTypeLkp getTypeId() {
        return typeId;
    }

    public void setTypeId(ActivityStepTypeLkp typeId) {
        this.typeId = typeId;
    }

    public Activities getActivityId() {
        return activityId;
    }

    public void setActivityId(Activities activityId) {
        this.activityId = activityId;
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
        if (!(object instanceof ActivitySteps)) {
            return false;
        }
        ActivitySteps other = (ActivitySteps) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.ActivitySteps[ id=" + id + " ]";
    }
    
}
