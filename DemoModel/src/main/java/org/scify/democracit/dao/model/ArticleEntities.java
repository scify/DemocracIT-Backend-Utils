/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.scify.democracit.dao.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
@Entity
@Table(name = "article_entities")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ArticleEntities.findAll", query = "SELECT a FROM ArticleEntities a"),
    @NamedQuery(name = "ArticleEntities.findById", query = "SELECT a FROM ArticleEntities a WHERE a.id = :id"),
    @NamedQuery(name = "ArticleEntities.findByArticleId", query = "SELECT a FROM ArticleEntities a WHERE a.articleId = :articleId"),
    @NamedQuery(name = "ArticleEntities.findByStartIndex", query = "SELECT a FROM ArticleEntities a WHERE a.startIndex = :startIndex"),
    @NamedQuery(name = "ArticleEntities.findByEndIndex", query = "SELECT a FROM ArticleEntities a WHERE a.endIndex = :endIndex"),
    @NamedQuery(name = "ArticleEntities.findByUrlPdf", query = "SELECT a FROM ArticleEntities a WHERE a.urlPdf = :urlPdf"),
    @NamedQuery(name = "ArticleEntities.findByEntityType", query = "SELECT a FROM ArticleEntities a WHERE a.entityType = :entityType"),
    @NamedQuery(name = "ArticleEntities.findByEntityText", query = "SELECT a FROM ArticleEntities a WHERE a.entityText = :entityText"),
    @NamedQuery(name = "ArticleEntities.findByConsultationId", query = "SELECT a FROM ArticleEntities a WHERE a.consultationId = :consultationId"),
    @NamedQuery(name = "ArticleEntities.findByArticleTextWithUrl", query = "SELECT a FROM ArticleEntities a WHERE a.articleTextWithUrl = :articleTextWithUrl")})
public class ArticleEntities implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "article_id")
    private int articleId;
    @Basic(optional = false)
    @Column(name = "start_index")
    private long startIndex;
    @Basic(optional = false)
    @Column(name = "end_index")
    private long endIndex;
    @Column(name = "url_pdf")
    private String urlPdf;
    @Basic(optional = false)
    @Column(name = "entity_type")
    private String entityType;
    @Column(name = "entity_text")
    private String entityText;
    @Basic(optional = false)
    @Column(name = "consultation_id")
    private long consultationId;
    @Column(name = "article_text_with_url")
    private String articleTextWithUrl;

    public ArticleEntities() {
    }

    public ArticleEntities(Integer id) {
        this.id = id;
    }

    public ArticleEntities(Integer id, int articleId, long startIndex, long endIndex, String entityType, long consultationId) {
        this.id = id;
        this.articleId = articleId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.entityType = entityType;
        this.consultationId = consultationId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityText() {
        return entityText;
    }

    public void setEntityText(String entityText) {
        this.entityText = entityText;
    }

    public long getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(long consultationId) {
        this.consultationId = consultationId;
    }

    public String getArticleTextWithUrl() {
        return articleTextWithUrl;
    }

    public void setArticleTextWithUrl(String articleTextWithUrl) {
        this.articleTextWithUrl = articleTextWithUrl;
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
        if (!(object instanceof ArticleEntities)) {
            return false;
        }
        ArticleEntities other = (ArticleEntities) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.scify.democracit.dao.model.ArticleEntities[ id=" + id + " ]";
    }
    
}
