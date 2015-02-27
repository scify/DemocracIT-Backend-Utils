/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess.DBUtils;

import com.google.gson.Gson;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class JSONMessage {

    private final String message;
    private final String details;

    public JSONMessage(String message, String other) {
        this.message = message;
        this.details = other;
    }

    public JSONMessage(String message) {
        this.message = message;
        this.details = "";
    }

    public String getMessage() {
        return message;
    }

    public String getMetadata() {
        return details;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.message != null ? this.message.hashCode() : 0);
        hash = 59 * hash + (this.details != null ? this.details.hashCode() : 0);
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
        final JSONMessage other = (JSONMessage) obj;
        if ((this.message == null) ? (other.message != null) : !this.message.equals(other.message)) {
            return false;
        }
        if ((this.details == null) ? (other.details != null) : !this.details.equals(other.details)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JSONMessage{" + "message=" + message + ", metadata=" + details + '}';
    }

    public String toJSON() {
        return new Gson().toJson(this, JSONMessage.class);
    }
}
