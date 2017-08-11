package com.mysql.jdbc;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author zhangpeng
 */
public class AnalyticDbConnection extends ConnectionImpl
{
    
    private static final Constructor<?> JDBC_4_CONNECTION_CTOR;
    
    static
    {
        if (Util.isJdbc4())
        {
            try
            {
                JDBC_4_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.AnalyticDbJdbc4Connection").getConstructor(
                    new Class[]{String.class, Integer.TYPE, Properties.class, String.class, String.class});
            } catch (SecurityException e)
            {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e)
            {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        } else
        {
            JDBC_4_CONNECTION_CTOR = null;
        }
    }
    
    public AnalyticDbConnection()
    {
        super();
    }
    
    public AnalyticDbConnection(final String hostToConnectTo, final int portToConnectTo, final Properties info,
        final String databaseToConnectTo, final String url) throws SQLException
    {
        super(hostToConnectTo, portToConnectTo, info, databaseToConnectTo, url);
    }
    
    @Override
    public ResultSetInternalMethods execSQL(final StatementImpl callingStatement, final String sql, final int maxRows,
        final Buffer packet,
        final int resultSetType, final int resultSetConcurrency, final boolean streamResults, final String catalog,
        final Field[] cachedMetadata) throws SQLException
    {
        return execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata, false);
    }
    
    @Override
    public ResultSetInternalMethods execSQL(final StatementImpl callingStatement, final String sql, final int maxRows,
        final Buffer packet,
        final int resultSetType, final int resultSetConcurrency, final boolean streamResults, final String catalog,
        final Field[] cachedMetadata,
        final boolean isBatch) throws SQLException
    {
        return super.execSQL(callingStatement, SqlUtils.fix(sql), maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata, isBatch);
    }
    
    protected static Connection getInstance(String hostToConnectTo, int portToConnectTo, Properties info,
        String databaseToConnectTo, String url)
        throws SQLException
    {
        if (!Util.isJdbc4())
        {
            return new AnalyticDbConnection(hostToConnectTo, portToConnectTo, info, databaseToConnectTo, url);
        }
        
        return (Connection) Util.handleNewInstance(JDBC_4_CONNECTION_CTOR, new Object[]{hostToConnectTo, Integer.valueOf(portToConnectTo), info,
            databaseToConnectTo, url}, null);
    }
}
