/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.model;

import java.sql.Timestamp;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class Consultation {

    private final long id;
    private final long organization_id;
    private Timestamp start_date;
    private Timestamp end_date;
    private String title;
    private String short_description;

    public Consultation(long id, long organization_id) {
        this.id = id;
        this.organization_id = organization_id;
    }

    public Consultation(long id, Timestamp start_date, Timestamp end_date, String title, String short_description, long organization_id) {
        this.id = id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.title = title;
        this.short_description = short_description;
        this.organization_id = organization_id;
    }

    public Timestamp getStartDate() {
        return start_date;
    }

    public Timestamp getEndDate() {
        return end_date;
    }

    public void setEndDate(Timestamp end_date) {
        this.end_date = end_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return short_description;
    }

    public void setShortDescription(String short_description) {
        this.short_description = short_description;
    }

    public long getID() {
        return id;
    }

    public long getOrganizationID() {
        return organization_id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Consultation other = (Consultation) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Consultation{" + "id=" + id + ", start_date=" + start_date + ", end_date=" + end_date + ", title=" + title + ", short_description=" + short_description + ", organization_id=" + organization_id + '}';
    }

}
