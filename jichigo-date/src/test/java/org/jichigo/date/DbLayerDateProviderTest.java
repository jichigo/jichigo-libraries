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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import javax.sql.DataSource;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

/**
 * Test Case of DbLayerDateFactory.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ LoggerFactory.class, Log4jLoggerAdapter.class })
public class DbLayerDateProviderTest {

    /**
     * if success,<br>
     * 1. verify get date.(main verify)<br>
     * 2. verify called close method.(main verify)<br>
     */
    @Test
    public void provideSuccess() throws InterruptedException, SQLException {

        // ------
        // setup.
        // ------
        // make mock.
        DataSource mockDataSource = PowerMockito.mock(DataSource.class);
        Connection mockConnection = PowerMockito.mock(Connection.class);
        Statement mockStatement = PowerMockito.mock(Statement.class);
        ResultSet mockResultSet = PowerMockito.mock(ResultSet.class);

        // bind mock.
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery("SELECT current_timestamp")).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true, false);

        Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());
        Mockito.when(mockResultSet.getObject(1)).thenReturn(expectedTimestamp);

        Thread.sleep(50);

        // ------
        // test
        // ------
        DbLayerDateProvider target = new DbLayerDateProvider();
        target.setDataSource(mockDataSource);
        target.setSql("SELECT current_timestamp");
        Date actualDate = target.provide();

        // ------
        // assert
        // ------
        {
            Assert.assertEquals(expectedTimestamp, actualDate);
        }
        {
            Mockito.verify(mockResultSet, Mockito.times(1)).close();
            Mockito.verify(mockStatement, Mockito.times(1)).close();
            Mockito.verify(mockResultSet, Mockito.times(1)).close();
        }

    }

    /**
     * if not exists resultSet,<br>
     * 1. verify {@link IllegalStateException}. message & cause.<br>
     * 2. verify called close method.(main verify)<br>
     */
    @Test
    public void provideNoResultSet() throws InterruptedException, SQLException {

        // ------
        // setup.
        // ------
        // make mock.
        DataSource mockDataSource = PowerMockito.mock(DataSource.class);
        Connection mockConnection = PowerMockito.mock(Connection.class);
        Statement mockStatement = PowerMockito.mock(Statement.class);
        ResultSet mockResultSet = PowerMockito.mock(ResultSet.class);

        // bind mock.
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery("SELECT sys_date FROM systemdate WHERE type = '1'")).thenReturn(
                mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        // ------
        // test
        // ------
        DbLayerDateProvider target = new DbLayerDateProvider();
        target.setDataSource(mockDataSource);
        target.setSql("SELECT sys_date FROM systemdate WHERE type = '1'");
        IllegalStateException actualException = null;
        try {
            target.provide();
            Assert.fail();
        } catch (IllegalStateException e) {
            actualException = e;
        }

        // ------
        // assert
        // ------
        // assert exception.
        {
            Assert.assertEquals(IllegalStateException.class, actualException.getClass());
            String expectedExceptionMessage = "date is not found. please confirm setting. dataSource is ["
                    + mockDataSource + "]. sql is [" + "SELECT sys_date FROM systemdate WHERE type = '1'" + "].";
            Assert.assertThat(actualException.getMessage(), CoreMatchers.is(expectedExceptionMessage));
            Assert.assertNull(actualException.getCause());
        }
        // verify called close method.
        {
            Mockito.verify(mockResultSet, Mockito.times(1)).close();
            Mockito.verify(mockStatement, Mockito.times(1)).close();
            Mockito.verify(mockResultSet, Mockito.times(1)).close();
        }
    }

    /**
     * if exists multiple resultSet,<br>
     * 1. verify {@link IllegalStateException}. message & cause.<br>
     * 2. verify called close method.(main verify)<br>
     */
    @Test
    public void provideMultipleResultSet() throws InterruptedException, SQLException {

        // ------
        // setup.
        // ------
        // make mock.
        DataSource mockDataSource = PowerMockito.mock(DataSource.class);
        Connection mockConnection = PowerMockito.mock(Connection.class);
        Statement mockStatement = PowerMockito.mock(Statement.class);
        ResultSet mockResultSet = PowerMockito.mock(ResultSet.class);

        // bind mock.
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery("SELECT sys_date FROM systemdate WHERE type = '1'")).thenReturn(
                mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true, true, false);

        Timestamp expectedTimestamp1 = new Timestamp(System.currentTimeMillis());
        Timestamp expectedTimestamp2 = new Timestamp(expectedTimestamp1.getTime() + 1);
        Mockito.when(mockResultSet.getObject(1)).thenReturn(expectedTimestamp1, expectedTimestamp2);

        // ------
        // test
        // ------
        DbLayerDateProvider target = new DbLayerDateProvider();
        target.setDataSource(mockDataSource);
        target.setSql("SELECT sys_date FROM systemdate WHERE type = '1'");
        IllegalStateException actualException = null;
        try {
            target.provide();
            Assert.fail();
        } catch (IllegalStateException e) {
            actualException = e;
        }

        // ------
        // assert
        // ------
        // assert exception.
        {
            Assert.assertEquals(IllegalStateException.class, actualException.getClass());
            String expectedExceptionMessage = "date is found multiple. found date is "
                    + Arrays.asList(expectedTimestamp1, expectedTimestamp2)
                    + ". please confirm setting. dataSource is [" + mockDataSource + "]. sql is ["
                    + "SELECT sys_date FROM systemdate WHERE type = '1'" + "].";
            Assert.assertThat(actualException.getMessage(), CoreMatchers.is(expectedExceptionMessage));
            Assert.assertNull(actualException.getCause());
        }
        // verify called close method.
        {
            Mockito.verify(mockResultSet, Mockito.times(1)).close();
            Mockito.verify(mockStatement, Mockito.times(1)).close();
            Mockito.verify(mockResultSet, Mockito.times(1)).close();
        }
    }

    /**
     * if dataSource is null,<br>
     * 1. verify {@link IllegalStateException} (message & cause).
     */
    @Test
    public void provideDataSourceIsNull() throws InterruptedException, SQLException {

        // ------
        // test
        // ------
        DbLayerDateProvider target = new DbLayerDateProvider();
        target.setDataSource(null);
        target.setSql("SELECT sys_date FROM systemdate WHERE type = '1'");

        IllegalStateException actualException = null;
        try {
            target.provide();
            Assert.fail();
        } catch (IllegalStateException e) {
            actualException = e;
        }

        // ------
        // assert
        // ------
        {
            Assert.assertEquals(IllegalStateException.class, actualException.getClass());
            String expectedExceptionMessage = "dataSource is null. please set.";
            Assert.assertThat(actualException.getMessage(), CoreMatchers.is(expectedExceptionMessage));
            Assert.assertNull(actualException.getCause());
        }
    }

    /**
     * if sql is null,<br>
     * 1. verify {@link IllegalStateException} (message & cause).
     */
    @Test
    public void provideSqlIsNull() throws InterruptedException, SQLException {

        // ------
        // setup.
        // ------
        DataSource mockDataSource = PowerMockito.mock(DataSource.class);

        // ------
        // test
        // ------
        DbLayerDateProvider target = new DbLayerDateProvider();
        target.setDataSource(mockDataSource);
        target.setSql(null);
        IllegalStateException actualException = null;
        try {
            target.provide();
            Assert.fail();
        } catch (IllegalStateException e) {
            actualException = e;
        }

        // ------
        // assert
        // ------
        {
            Assert.assertEquals(IllegalStateException.class, actualException.getClass());
            String expectedExceptionMessage = "sql is null. please set.";
            Assert.assertThat(actualException.getMessage(), CoreMatchers.is(expectedExceptionMessage));
            Assert.assertNull(actualException.getCause());
        }
    }

    /**
     * if sql is empty,<br>
     * 1. verify {@link IllegalStateException} (message & cause).
     */
    @Test
    public void provideSqlIsEmpty() throws InterruptedException, SQLException {

        // ------
        // setup
        // ------
        DataSource mockDataSource = PowerMockito.mock(DataSource.class);

        // ------
        // test
        // ------
        DbLayerDateProvider target = new DbLayerDateProvider();
        target.setDataSource(mockDataSource);
        target.setSql("");
        IllegalStateException actualException = null;
        try {
            target.provide();
            Assert.fail();
        } catch (IllegalStateException e) {
            actualException = e;
        }

        // ------
        // assert
        // ------
        {
            Assert.assertEquals(IllegalStateException.class, actualException.getClass());
            String expectedExceptionMessage = "sql is empty. please set.";
            Assert.assertThat(actualException.getMessage(), CoreMatchers.is(expectedExceptionMessage));
            Assert.assertNull(actualException.getCause());
        }
    }

    /**
     * if SQLException on get date,<br>
     * 1. verify {@link IllegalStateException} (message & cause).
     */
    @Test
    public void provideSQLException() throws InterruptedException, SQLException {

        // ------
        // setup.
        // ------
        SQLException expectedSQLException = new SQLException();

        // make mock.
        DataSource mockDataSource = PowerMockito.mock(DataSource.class);

        // bind mock.
        Mockito.when(mockDataSource.getConnection()).thenThrow(expectedSQLException);

        // ------
        // test
        // ------
        // set input.
        DbLayerDateProvider target = new DbLayerDateProvider();
        target.setDataSource(mockDataSource);
        target.setSql("SELECT sys_date FROM systemdate WHERE type = '1'");

        IllegalStateException actualException = null;
        try {
            target.provide();
            Assert.fail();
        } catch (IllegalStateException e) {
            actualException = e;
        }

        // ------
        // assert
        // ------
        // assert exception.
        {
            Assert.assertEquals(IllegalStateException.class, actualException.getClass());
            String expectedExceptionMessage = "fail get date from db server. please confirm setting. dataSource is ["
                    + mockDataSource + "]. sql is [" + "SELECT sys_date FROM systemdate WHERE type = '1'" + "].";
            Assert.assertThat(actualException.getMessage(), CoreMatchers.is(expectedExceptionMessage));
            Assert.assertEquals(expectedSQLException, actualException.getCause());
        }
    }

    /**
     * if SQLException on close,<br>
     * 1. verify get date.<br>
     * 2. verify called close method.<br>
     * 3. verify called logger.error method. (main verify)<br>
     */
    @Test
    public void provideSQLExceptionOnClose() throws InterruptedException, SQLException {

        // ------
        // setup
        // ------
        SQLException expectedSQLException = new SQLException();

        // make mock.
        DataSource mockDataSource = PowerMockito.mock(DataSource.class);
        Connection mockConnection = PowerMockito.mock(Connection.class);
        Statement mockStatement = PowerMockito.mock(Statement.class);
        ResultSet mockResultSet = PowerMockito.mock(ResultSet.class);
        Logger mockLogger = PowerMockito.spy(LoggerFactory.getLogger(DbLayerDateProvider.class));
        PowerMockito.mockStatic(LoggerFactory.class);

        // bind mock.
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery("SELECT current_timestamp")).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true, false);
        Timestamp expectedTimestamp = new Timestamp(System.currentTimeMillis());
        Mockito.when(mockResultSet.getObject(1)).thenReturn(expectedTimestamp);
        Mockito.doThrow(expectedSQLException).when(mockConnection).close();
        Mockito.doThrow(expectedSQLException).when(mockStatement).close();
        Mockito.doThrow(expectedSQLException).when(mockResultSet).close();
        PowerMockito.when(LoggerFactory.getLogger(DbLayerDateProvider.class)).thenReturn(mockLogger);

        Thread.sleep(50);

        // ------
        // test
        // ------
        // set input.
        DbLayerDateProvider target = new DbLayerDateProvider();
        target.setDataSource(mockDataSource);
        target.setSql("SELECT current_timestamp");
        // execute.
        Date actualDate = null;
        {
            actualDate = target.provide();
        }

        // ------
        // assert
        // ------
        // assert date.
        {
            Assert.assertEquals(expectedTimestamp, actualDate);
        }
        // verify called close method.
        {
            Mockito.verify(mockResultSet, Mockito.times(1)).close();
            Mockito.verify(mockStatement, Mockito.times(1)).close();
            Mockito.verify(mockResultSet, Mockito.times(1)).close();
        }
        // verify called log method.
        {
            Mockito.verify(mockLogger, Mockito.times(1)).error("fail close of connection.", expectedSQLException);
            Mockito.verify(mockLogger, Mockito.times(1)).error("fail close of statement.", expectedSQLException);
            Mockito.verify(mockLogger, Mockito.times(1)).error("fail close of resultSet.", expectedSQLException);
        }
    }

}
