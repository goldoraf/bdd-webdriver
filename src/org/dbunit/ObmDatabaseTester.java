package org.dbunit;
import java.io.IOException;

import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ObmDataSet;
import org.dbunit.operation.CompositeOperation;
import org.dbunit.operation.InsertEntityOperation;
import org.dbunit.operation.TruncateCascadeTableOperation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


public class ObmDatabaseTester extends JdbcDatabaseTester {
	
	//private static final Logger logger = LoggerFactory.getLogger(ObmDatabaseTester.class);
	
	private static final String MYSQL_DRIVER_CLASS = "org.gjt.mm.mysql.Driver";
	private static final String PGSQL_DRIVER_CLASS = "org.postgresql.Driver";
	
	private static final String MYSQL_JDBC = "jdbc:mysql://";
	private static final String PGSQL_JDBC = "jdbc:postgresql://";
	
	public static String SERVER_SQL = "pgsql";
	public static String OBM_URL = "test-trunk-pg.scorpion";
	public static String OBM_LOGIN = "obm";
	public static String OBM_PASSWORD = "obm";
	public static String OBM_DATABASE = "obm";
	
	private ObmDataSet dataSet;
	private static boolean CASCADE = false;
	
	public ObmDatabaseTester() throws Exception {
		super(getDriverClass(), createConnectionUrl(), OBM_LOGIN, OBM_PASSWORD);
		this.dataSet = new ObmDataSet();
		this.dataSet.addDataFile("1_domain_3_users");
		this.dataSet.addDataFile("stale_data");
		this.setDataSet(this.dataSet);
		this.setSetUpOperation(new CompositeOperation(new TruncateCascadeTableOperation(CASCADE), new InsertEntityOperation(CASCADE)));
		this.setTearDownOperation(new CompositeOperation(new TruncateCascadeTableOperation(CASCADE), new InsertEntityOperation(CASCADE)));
	}
	
	public void addTable(String tableName, String filePath) throws DataSetException, IOException {
		this.dataSet.addTable(tableName,filePath);
	}
	
	public void addTable(String tableName) throws DataSetException, IOException {
		this.dataSet.addTable(tableName);
	}
	
	public void addEntityTable(String tableName, String entityName, String filePath) throws Exception {
		this.dataSet.addEntityTable(tableName, entityName, filePath);
	}
	
	public void addEntityTable(String tableName, String entityName) throws Exception {
		this.dataSet.addEntityTable(tableName, entityName);
	}

	public void onSetup() throws Exception {
		super.onSetup();
	}
	
	public void onTearDown() throws Exception {
		/*IDataSet dataSet = new FlatXmlDataSet(new File("db_data/full_data.xml"),false,true);
		this.setDataSet(dataSet);*/
		super.onTearDown();
	}
	
	private static String getDriverClass() {
		if(SERVER_SQL.equals("mysql")) {
			return MYSQL_DRIVER_CLASS;
		} else if (SERVER_SQL.equals("pgsql")) {
			CASCADE = true;
			return PGSQL_DRIVER_CLASS;
		}
		
		new Throwable("Function getDriverClass() : Unknow sql server !");
		return null;
	}
	
	private static String getJdbcConnector() {
		if(SERVER_SQL.equals("mysql")) {
			return MYSQL_JDBC;
		} else if (SERVER_SQL.equals("pgsql")) {
			return PGSQL_JDBC;
		}
		
		new Throwable("Function getJdbcConnector() : Unknow sql server !");
		return null;
	}
	
	public static String createConnectionUrl() {
		//zeroDateTimeBehavior=convertToNull permet de recuperer les timestamp de la forme "0000-00-00 00:00:00"
		return getJdbcConnector()+OBM_URL+"/"+OBM_DATABASE+"?zeroDateTimeBehavior=convertToNull";
	}
}
