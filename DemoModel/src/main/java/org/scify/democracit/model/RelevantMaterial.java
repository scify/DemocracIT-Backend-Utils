/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.model;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class RelevantMaterial {

    private final long id;
    private final long consultation_id;
    private String title;
    private String url_source;

    public RelevantMaterial(long id, long consultation_id) {
        this.id = id;
        this.consultation_id = consultation_id;
    }

    public RelevantMaterial(long id, long consultation_id, String title, String url_source) {
        this.id = id;
        this.consultation_id = consultation_id;
        this.title = title;
        this.url_source = url_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURLSource() {
        return url_source;
    }

    public void setURLSource(String url_source) {
        this.url_source = url_source;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 41 * hash + (int) (this.consultation_id ^ (this.consultation_id >>> 32));
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
        final RelevantMaterial other = (RelevantMaterial) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.consultation_id != other.consultation_id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RelevantMaterial{" + "id=" + id + ", consultation_id=" + consultation_id + ", title=" + title + ", url_source=" + url_source + '}';
    }

}
