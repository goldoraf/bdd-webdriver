package org.dbunit.dataset;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.xml.FlatXmlDataSet;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class ObmDataSet extends DefaultDataSet{

	//private static final Logger logger = LoggerFactory.getLogger(DefaultDataSet.class);
	
	protected Hashtable<String, String> entityTables;
	
	public ObmDataSet() throws IOException, DataSetException {
		super();
		entityTables = new Hashtable<String, String>();
	}
	
	public void addDataFile(String filename) throws DataSetException, IOException {
		IDataSet data = new FlatXmlDataSet(new File("db_data/"+filename+".xml"),false,true);
		
		ITableIterator tables = data.iterator();
		
		//logger.debug("addTable(table={}) - start", data.getTable());
        
        this.initialize();
        
        while (tables.next()) {
        	ITable table = tables.getTable();
        	super._orderedTableNameMap.add(table.getTableMetaData().getTableName(), table);
        }
		
	}
	
	public void addTable(String nameTable) throws DataSetException, IOException {
		this.addTable(nameTable, null);
	}
	
	public void addTable(String nameTable, String filename) throws DataSetException, IOException {
		if(isEmpty(filename)) {
			filename = nameTable;
		}
		addDataFile(filename);
	}
	
	public void addEntityTable(String nameTable, String entityName, String filePath) throws IOException, DatabaseUnitException, SQLException {
	    
	    this.addTable(nameTable, filePath);
	    
	    if(isEmpty(entityName)) {
	    	entityName = nameTable.toLowerCase();
	    }
	    this.entityTables.put(nameTable, entityName);
	}
	
	public void addEntityTable(String nameTable, String entityName) throws IOException, DatabaseUnitException, SQLException {
	    this.addEntityTable(nameTable, entityName, null);
	}
	
	public Hashtable<String, String> getEntityTable() {
		return this.entityTables;
	}
	
	public String getEntityNames(String tableName) {
		return this.entityTables.get(tableName);
	}
	
	public boolean isTableEntity(String tableName) {
		return this.entityTables.containsKey(tableName);
	}
	
	private boolean isEmpty(String chaine) {
		return ("".equals(chaine) || chaine == null );
	}
	
}