/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.text;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;
import org.scify.democracit.dao.model.Comments;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class HtmlDocumentCleaner {

    public static String cleanDocumentToPlainText(String sDocument) {
        if (sDocument != null) {
            sDocument = sDocument.trim();
            OutputSettings settings = new OutputSettings();
            // keep only restricted html entities (like quote, etc)
            settings.escapeMode(Entities.EscapeMode.xhtml);
            // keep basic formatting
            return Jsoup.clean(sDocument, "", Whitelist.none(), settings);
        } else {
            return "";
        }
    }

    public static Comments cleanCommentFromHtml(Comments cComment) {
        String text = cComment.getComment();
        text = cleanDocumentToPlainText(text);
        cComment.setComment(text);
        return cComment;
    }
}
