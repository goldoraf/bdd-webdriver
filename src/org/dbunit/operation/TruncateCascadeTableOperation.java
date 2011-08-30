package org.dbunit.operation;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.IBatchStatement;
import org.dbunit.database.statement.IStatementFactory;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.datatype.ObmDataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TruncateCascadeTableOperation extends AbstractOperation{

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(DeleteAllOperation.class);
    private boolean cascade;

    public TruncateCascadeTableOperation(boolean cascade)
    {
    	this.cascade=cascade;
    }

    protected String getDeleteAllCommand()
    {
        return "truncate table ";
    }
	
	@Override
	public void execute(IDatabaseConnection connection, IDataSet dataSet)
			throws DatabaseUnitException, SQLException 
	{
		 logger.debug("execute(connection={}, dataSet={}) - start", connection, dataSet);
		
		IDataSet databaseDataSet = connection.createDataSet();
		
		DatabaseConfig databaseConfig = connection.getConfig();
		databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new ObmDataTypeFactory());
		boolean oldValue = databaseConfig.getFeature(DatabaseConfig.FEATURE_BATCHED_STATEMENTS);
		IStatementFactory statementFactory = (IStatementFactory)databaseConfig.getProperty(DatabaseConfig.PROPERTY_STATEMENT_FACTORY);
		IBatchStatement statement = statementFactory.createBatchStatement(connection);
		try
		{
			
			databaseConfig.setFeature(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, false);
		    int count = 0;
		    
		    Stack<String> tableNames = new Stack<String>();
		    Set<String> tablesSeen = new HashSet<String>();
		    ITableIterator iterator = dataSet.iterator();
		    while (iterator.next())
		    {
		        String tableName = iterator.getTableMetaData().getTableName();
		        if (!tablesSeen.contains(tableName))
		        {
		            tableNames.push(tableName);
		            tablesSeen.add(tableName);
		        }
		    }
		
		    // delete tables once each in reverse order of seeing them.
		    while (!tableNames.isEmpty())
		    {
		        String tableName = (String)tableNames.pop();
		
		        // Use database table name. Required to support case sensitive database.
		        ITableMetaData databaseMetaData = databaseDataSet.getTableMetaData(tableName);
		        tableName = databaseMetaData.getTableName();
		
		        StringBuffer sqlBuffer = new StringBuffer(128);
		        sqlBuffer.append(getDeleteAllCommand());
		        sqlBuffer.append(getQualifiedName(connection.getSchema(), tableName, connection));
		        if(isCascade()) {
		        	sqlBuffer.append(getCascadeCommand());
		        }
		        String sql = sqlBuffer.toString();
		        statement.addBatch(sql);
		
		        if(logger.isDebugEnabled())
		            logger.debug("Added SQL: {}", sql);
		        
		        count++;
		    }
		
		    if (count > 0)
		    {
		        statement.executeBatch();
		        statement.clearBatch();
		    }
		}
		finally
		{
		    statement.close();
		    databaseConfig.setFeature(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, oldValue);
		}
	}
	
	protected boolean isCascade()
	{
		return this.cascade;
	}
	
	protected String getCascadeCommand()
	{
		return " cascade";
	}
}
