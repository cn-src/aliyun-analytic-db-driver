package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author zhangpeng
 */
public class AnalyticDbDriver extends NonRegisteringDriver
{
    private static final String REPLICATION_URL_PREFIX = "jdbc:mysql:replication://";
    
    static {
        try {
            java.sql.DriverManager.registerDriver(new AnalyticDbDriver());
        } catch (SQLException E) {
            throw new RuntimeException("Can't register driver!");
        }
    }
    public AnalyticDbDriver() throws SQLException
    {
    }
    
    public java.sql.Connection connect(String url, Properties info) throws SQLException
    {
        
        if (url != null)
        {
            if (StringUtils.startsWithIgnoreCase(url, LOADBALANCE_URL_PREFIX))
            {
                return connectLoadBalanced(url, info);
            } else if (StringUtils.startsWithIgnoreCase(url, REPLICATION_URL_PREFIX))
            {
                return connectReplicationConnection(url, info);
            }
        }
        
        Properties props = null;
        
        if ((props = parseURL(url, info)) == null)
        {
            return null;
        }
        
        if (!"1".equals(props.getProperty(NUM_HOSTS_PROPERTY_KEY)))
        {
            return connectFailover(url, info);
        }
        
        try
        {
            Connection newConn = AnalyticDbConnection.getInstance(host(props), port(props), props, database(props), url);
            
            return newConn;
        } catch (SQLException sqlEx)
        {
            // Don't wrap SQLExceptions, throw
            // them un-changed.
            throw sqlEx;
        } catch (Exception ex)
        {
            SQLException sqlEx = SQLError.createSQLException(
                Messages.getString("NonRegisteringDriver.17") + ex.toString() + Messages.getString("NonRegisteringDriver.18"),
                SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE, null);
            
            sqlEx.initCause(ex);
            
            throw sqlEx;
        }
    }
    
    private java.sql.Connection connectLoadBalanced(String url, Properties info) throws SQLException
    {
        Properties parsedProps = parseURL(url, info);
        
        if (parsedProps == null)
        {
            return null;
        }
        
        // People tend to drop this in, it doesn't make sense
        parsedProps.remove("roundRobinLoadBalance");
        
        int numHosts = Integer.parseInt(parsedProps.getProperty(NUM_HOSTS_PROPERTY_KEY));
        
        List<String> hostList = new ArrayList<String>();
        
        for (int i = 0; i < numHosts; i++)
        {
            int index = i + 1;
            
            hostList.add(parsedProps.getProperty(HOST_PROPERTY_KEY + "." + index) + ":" + parsedProps.getProperty(PORT_PROPERTY_KEY + "." + index));
        }
        
        LoadBalancingConnectionProxy proxyBal = new LoadBalancingConnectionProxy(hostList, parsedProps);
        
        return (java.sql.Connection) java.lang.reflect.Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                                              new Class[]{LoadBalancedConnection.class}, proxyBal);
    }
    
    private java.sql.Connection connectFailover(String url, Properties info) throws SQLException
    {
        Properties parsedProps = parseURL(url, info);
        
        if (parsedProps == null)
        {
            return null;
        }
        
        // People tend to drop this in, it doesn't make sense
        parsedProps.remove("roundRobinLoadBalance");
        parsedProps.setProperty("autoReconnect", "false");
        
        int numHosts = Integer.parseInt(parsedProps.getProperty(NUM_HOSTS_PROPERTY_KEY));
        
        List<String> hostList = new ArrayList<String>();
        
        for (int i = 0; i < numHosts; i++)
        {
            int index = i + 1;
            
            hostList.add(parsedProps.getProperty(HOST_PROPERTY_KEY + "." + index) + ":" + parsedProps.getProperty(PORT_PROPERTY_KEY + "." + index));
        }
        
        FailoverConnectionProxy connProxy = new FailoverConnectionProxy(hostList, parsedProps);
        
        return (java.sql.Connection) java.lang.reflect.Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                                              new Class[]{Connection.class}, connProxy);
    }
    
}
