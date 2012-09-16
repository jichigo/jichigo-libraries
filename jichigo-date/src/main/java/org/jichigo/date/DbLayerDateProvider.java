/*
 * Copyright (c) 2012 jichigo's developers team.
 *
 * jichigo's source code and binaries are distributed the MIT License.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial 
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE 
 * AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.jichigo.date;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for get date from DB layer(DB Server).
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author created by Kazuki Shimizu
 * @see {@link org.slf4j.Logger} Using logger.
 * @see {@link org.slf4j.LoggerFactory} Using logger.
 */
public class DbLayerDateProvider extends ModifiableDateProvider implements DateCreator {

    /**
     * Logger instance of slf4j.
     */
    private final Logger logger = LoggerFactory.getLogger(DbLayerDateProvider.class);

    /**
     * Data Source.
     */
    protected DataSource dataSource;

    /**
     * Sql for get date.
     */
    protected String sql;

    /**
     * Create New date.
     * <p>
     * Get date using sql from data Source(DB Server).
     * </p>
     * 
     * @return {@link java.sql.ResultSet#getTimestamp(int)}. Method argument is 1.
     * @throws IllegalStateException if dataSource is null.
     * @throws IllegalStateException if sql is null or empty.
     * @throws IllegalStateException if not exists result set.
     * @throws IllegalStateException if exists multiple result set.
     * @throws IllegalStateException if occur SQLException. But if occur SQLException on close, don't occurred.
     */
    @Override
    public Date newDate() {

        // check setting.
        if (dataSource == null) {
            throw new IllegalStateException("dataSource is null. please set.");
        }
        if (sql == null) {
            throw new IllegalStateException("sql is null. please set.");
        }
        if (sql.isEmpty()) {
            throw new IllegalStateException("sql is empty. please set.");
        }

        // get date.
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Date date = null;
        try {
            // execute query.
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            final List<Date> dates = new ArrayList<Date>();
            while (resultSet.next()) {
                dates.add((Date) resultSet.getObject(1));
            }
            // check result.
            if (dates.isEmpty()) {
                // if not exists.
                final String msg = "date is not found. please confirm setting. dataSource is [" + dataSource
                        + "]. sql is [" + sql + "].";
                throw new IllegalStateException(msg);
            }
            if (1 < dates.size()) {
                // if exists multiple.
                final String msg = "date is found multiple. found date is " + dates
                        + ". please confirm setting. dataSource is [" + dataSource + "]. sql is [" + sql + "].";
                throw new IllegalStateException(msg);
            }
            // set date.
            date = dates.get(0);
        } catch (final SQLException e) {
            final String msg = "fail get date from db server. please confirm setting. dataSource is [" + dataSource
                    + "]. sql is [" + sql + "].";
            throw new IllegalStateException(msg, e);
        } finally {
            // close.
            close(resultSet);
            close(statement);
            close(connection);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("date is {}", date);
        }
        return date;
    }

    /**
     * Inject data source.
     * 
     * @param dataSource data source.
     */
    @Inject
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Inject sql for get date.
     * 
     * @param sql sql for get date.
     */
    public void setSql(final String sql) {
        this.sql = sql;
    }

    /**
     * Close connection.
     * <p>
     * if occur SQLException, log error level and continue process.
     * </p>
     * 
     * @param connection database connection.
     */
    private void close(final Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (final SQLException e) {
                logger.error("fail close of connection.", e);
            }
        }
    }

    /**
     * Close statement.
     * <p>
     * if occur SQLException, log error level and continue process.
     * </p>
     * 
     * @param statement statement.
     */
    private void close(final Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (final SQLException e) {
                logger.error("fail close of statement.", e);
            }
        }
    }

    /**
     * Close result set.
     * <p>
     * if occur SQLException, log error level and continue process.
     * </p>
     * 
     * @param resultSet result set of query.
     */
    private void close(final ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (final SQLException e) {
                logger.error("fail close of resultSet.", e);
            }
        }
    }

}
