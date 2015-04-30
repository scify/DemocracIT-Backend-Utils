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
@Table(name = "log.activity_step_type_lkp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActivityStepTypeLkp.findAll", query = "SELECT a FROM ActivityStepTypeLkp a"),
    @NamedQuery(name = "ActivityStepTypeLkp.findById", query = "SELECT a FROM ActivityStepTypeLkp a WHERE a.id = :id"),
    @NamedQuery(name = "ActivityStepTypeLkp.findByName", query = "SELECT a FROM ActivityStepTypeLkp a WHERE a.name = :name")})
public class ActivityStepTypeLkp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeId")
    private Collection<ActivitySteps> activityStepsCollection;

    public ActivityStepTypeLkp() {
    }

    public ActivityStepTypeLkp(Integer id) {
        this.id = id;
    }

    public ActivityStepTypeLkp(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof ActivityStepTypeLkp)) {
            return false;
        }
        ActivityStepTypeLkp other = (ActivityStepTypeLkp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.ActivityStepTypeLkp[ id=" + id + " ]";
    }
    
}
