/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess.ds;

import javax.sql.DataSource;

/**
 * implemented on dataSource 
 * @author George K. <gkiom@scify.org>
 */
public class AbstractDSAccess {

    protected DataSource dataSource;

    public AbstractDSAccess(DataSource datasource) {
        this.dataSource = datasource;
    }
}
