/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.model;

import java.util.Objects;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class User {

    private final long id;
    private final int source_type_id;
    private final int role_id;
    private String first_name;
    private String last_name;
    private String nickname;
    private String email;
    private String url;

    public User(long id, String first_name, String last_name, String nickname, String email, String url, int source_type_id, int role_id) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.nickname = nickname;
        this.email = email;
        this.url = url;
        this.source_type_id = source_type_id;
        this.role_id = role_id;
    }

    public User(long id, int source_type_id, int role_id) {
        this.id = id;
        this.source_type_id = source_type_id;
        this.role_id = role_id;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public long getID() {
        return id;
    }

    public int getSourceTypeID() {
        return source_type_id;
    }

    public int getRoleID() {
        return role_id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 89 * hash + Objects.hashCode(this.first_name);
        hash = 89 * hash + Objects.hashCode(this.last_name);
        hash = 89 * hash + Objects.hashCode(this.nickname);
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
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.first_name, other.first_name)) {
            return false;
        }
        if (!Objects.equals(this.last_name, other.last_name)) {
            return false;
        }
        if (!Objects.equals(this.nickname, other.nickname)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", source_type_id=" + source_type_id + ", role_id=" + role_id + ", first_name=" + first_name + ", last_name=" + last_name + ", nickname=" + nickname + ", email=" + email + ", url=" + url + '}';
    }

}
