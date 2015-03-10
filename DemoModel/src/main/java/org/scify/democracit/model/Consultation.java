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
    private String consultation_url;
    private Integer completed;
    private String completed_text;
    private String report_text;
    private String report_url;
    private Integer art_num;

    public Consultation(long id, long organization_id) {
        this.id = id;
        this.organization_id = organization_id;
    }

    public Consultation(long id, long organization_id, Timestamp start_date, Timestamp end_date, String title, String short_description,
            String consultation_url) {
        this.id = id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.title = title;
        this.short_description = short_description;
        this.organization_id = organization_id;
        this.consultation_url = consultation_url;
    }

    public Consultation(long id, long organization_id,
            Timestamp start_date, Timestamp end_date,
            String title, String short_description,
            String consultation_url, Integer completed,
            String completed_text, String report_text,
            String report_url, Integer art_num) {
        this.id = id;
        this.organization_id = organization_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.title = title;
        this.short_description = short_description;
        this.consultation_url = consultation_url;
        this.completed = completed;
        this.completed_text = completed_text;
        this.report_text = report_text;
        this.report_url = report_url;
        this.art_num = art_num;
    }

    public Timestamp getStartDate() {
        return start_date;
    }

    public Timestamp getEndDate() {
        return end_date;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return short_description;
    }

    public long getID() {
        return id;
    }

    public long getOrganizationID() {
        return organization_id;
    }

    public String getConsultation_url() {
        return consultation_url;
    }

    public Integer getCompleted() {
        return completed;
    }

    public String getCompleted_text() {
        return completed_text;
    }

    public Integer getArticlesCnt() {
        return art_num;
    }

    public String getReportText() {
        return report_text;
    }

    public String getReportURL() {
        return report_url;
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
        return "Consultation{" + "id=" + id
                + ", organization_id=" + organization_id
                + ", start_date=" + start_date
                + ", end_date=" + end_date
                + ", title=" + title
                + ", short_description=" + short_description
                + ", consultation_url=" + consultation_url
                + ", completed=" + completed
                + ", completed_text=" + completed_text
                + ", report_text=" + report_text
                + ", report_url=" + report_url
                + ", art_num=" + art_num
                + '}';
    }

}
