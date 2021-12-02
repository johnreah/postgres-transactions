package com.johnreah.cdi_jta_helper.persistence;

import com.arjuna.ats.jdbc.TransactionalDriver;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * delegates the transactionManager's call getConnection() to the H2DataSource.
 * The reference to the datasource is provided by the container.
 * @author mk
 *
 */

@ApplicationScoped
public class H2TransactionalDataSourceDelegate implements DataSource {
	
	private TransactionalDriver transactionalDriver;
	
	@Inject
    H2DataSourceWrapper wrapper;
	
		
	@PostConstruct
	private void init() throws SQLException {
		this.transactionalDriver = new TransactionalDriver();
		DriverManager.registerDriver(this.transactionalDriver);
	}
	
	
	@Override
	public Connection getConnection() throws SQLException{
		Properties props = new Properties();
		props.setProperty(TransactionalDriver.poolConnections,"true");
		// eval eclipselink pooling
		props.setProperty(TransactionalDriver.maxConnections,"15");
		props.put(TransactionalDriver.XADataSource,this.wrapper);
		return DriverManager.getConnection(TransactionalDriver.arjunaDriver,props);
	}

		
	@Override
	public PrintWriter getLogWriter() throws SQLException {
        throw new RuntimeException("not implemented");
	}
 
	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
	      throw new RuntimeException("not implemented");	  		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
	      throw new RuntimeException("not implemented");
	}

	@Override
	public int getLoginTimeout() throws SQLException {
	      throw new RuntimeException("not implemented");
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
	      throw new RuntimeException("not implemented");
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
	      throw new RuntimeException("not implemented");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
	      throw new RuntimeException("not implemented");
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
       return this.getConnection();
	}

	
}
