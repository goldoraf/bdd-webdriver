package org.dbunit.operation;

import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.IPreparedBatchStatement;
import org.dbunit.database.statement.IStatementFactory;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ObmDataSet;
import org.dbunit.dataset.datatype.ObmDataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InsertEntityOperation extends InsertOperation
{

	private static final Logger logger = LoggerFactory.getLogger(InsertEntityOperation.class);
	private boolean cascade;
	
	public InsertEntityOperation(boolean cascade){
		this.cascade = cascade;
	}
	
	/**
     * Logger for this class
     */
	@Override
    public void execute(IDatabaseConnection connection, IDataSet dataSet)
    throws DatabaseUnitException, SQLException
	{
		logger.debug("execute(connection={}, dataSet={}) - start", connection, dataSet);
		
		DatabaseConfig databaseConfig = connection.getConfig();
		databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new ObmDataTypeFactory());
		IStatementFactory factory = (IStatementFactory)databaseConfig.getProperty(DatabaseConfig.PROPERTY_STATEMENT_FACTORY);
		
		new TruncateCascadeTableOperation(this.cascade).execute(connection, dataSet);
		
		super.execute(connection, dataSet);
		
		IPreparedBatchStatement statement = null;
		if(dataSet.getClass() == ObmDataSet.class) {
			ITableIterator dataSetIterator = dataSet.iterator();
			while(dataSetIterator.next()) {
				ITable table = dataSetIterator.getTable();
				String tableName = table.getTableMetaData().getTableName();
				if(((ObmDataSet)dataSet).isTableEntity(tableName)) {
					Integer nbInteger = table.getRowCount();
					String Entity = ((ObmDataSet) dataSet).getEntityNames(tableName);
					for(int i=0; i< nbInteger; i++) {
						String column = tableName.toLowerCase()+"_id";
						Object primarykey = table.getValue(i, column);
						String sql = "INSERT INTO Entity (entity_mailing) VALUES (TRUE);";
						statement = factory.createPreparedBatchStatement(sql, connection);
						statement.addBatch();
						statement.executeBatch();
						statement.clearBatch();
						sql = "INSERT INTO "+ucFirst(Entity)+"Entity ("+Entity+"entity_entity_id, "+Entity+"entity_"+Entity+"_id ) SELECT MAX(entity_id), "+primarykey+" FROM Entity;";
						statement = factory.createPreparedBatchStatement(sql, connection);
						statement.addBatch();
						statement.executeBatch();
		                statement.clearBatch();
					}
				}
			}
			
			if(statement != null) {
				statement.close();
			}
		}
	}
	
	private String ucFirst(String s) {
		return s.replaceFirst(String.valueOf(s.charAt(0)), String.valueOf(Character.toUpperCase(s.charAt(0))));
	}
}
