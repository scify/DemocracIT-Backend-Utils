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
public class Article {

    private final long id;
    private final long consultation_id;
    private String title;
    private String body;
    private int order;

    public Article(long id, long consultation_id) {
        this.id = id;
        this.consultation_id = consultation_id;
    }

    public Article(long id, long consultation_id, String title, String body, int order) {
        this.id = id;
        this.consultation_id = consultation_id;
        this.title = title;
        this.body = body;
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getID() {
        return id;
    }

    public long getConsultationID() {
        return consultation_id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 11 * hash + (int) (this.consultation_id ^ (this.consultation_id >>> 32));
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
        final Article other = (Article) obj;
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
        return "Article{" + "id=" + id + ", consultation_id=" + consultation_id + ", title=" + title + ", body=" + body + ", order=" + order + '}';
    }

}
