/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Iterator;

/**
 *
 * @author George K.<gkiom@scify.org>
 */
public class SqlQueryBlockIterable implements Iterable<ResultSet> {

    String BaseQuery;
    Connection dbConnection;

    public SqlQueryBlockIterable(Connection dbCon, String sSQLQuery) {
        this.BaseQuery = sSQLQuery;
        this.dbConnection = dbCon;
    }

    @Override
    public Iterator<ResultSet> iterator() {
        return new SqlQueryBlockIterator(dbConnection, BaseQuery);
    }
}
