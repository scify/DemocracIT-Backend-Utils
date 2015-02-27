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
public class SourceType {

    private final int id;
    private String source_type;

    public SourceType(int id) {
        this.id = id;
    }

    public SourceType(int id, String source_type) {
        this.id = id;
        this.source_type = source_type;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public int getID() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + this.id;
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
        final SourceType other = (SourceType) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SourceType{" + "id=" + id + ", source_type=" + source_type + '}';
    }

}
