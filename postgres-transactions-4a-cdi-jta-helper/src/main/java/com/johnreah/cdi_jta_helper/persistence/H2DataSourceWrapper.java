package com.johnreah.cdi_jta_helper.persistence;

import org.h2.jdbcx.JdbcDataSource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * wrapps the h2-xa datasource (used both by the transactionalDriver and the
 * readonly-connection. the url is configured via the "java:h2url" resource
 * @author Michael Krauter
 *
 */

@ApplicationScoped
public class H2DataSourceWrapper implements DataSource,XADataSource {
			
    public static final String jndi_H2Url = "java:h2Url";
        
	@Resource(lookup = H2DataSourceWrapper.jndi_H2Url)
	private String h2Url;

	private JdbcDataSource ds;	
	
	@PostConstruct
	private void init() {
		this.ds = new JdbcDataSource();
		this.ds.setUrl(this.h2Url);
		this.ds.setUser("sa");
		this.ds.setPassword("");	
	}
	
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
        return this.ds.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
        this.ds.setLogWriter(out);		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		this.ds.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return this.ds.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.ds.getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.ds.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.ds.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
       return this.ds.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
       return this.ds.getConnection();
	}
	
	public void scriptDatabase(String filename) throws SQLException{
		Connection c = this.ds.getConnection();
		c.createStatement().execute("SCRIPT COLUMNS TO '"+filename+"' ");
		c.close(); 
	}


	@Override
	public XAConnection getXAConnection() throws SQLException {
           return this.ds.getXAConnection();
	}


	@Override
	public XAConnection getXAConnection(String user, String password) throws SQLException {
          return this.getXAConnection();
	}


}
