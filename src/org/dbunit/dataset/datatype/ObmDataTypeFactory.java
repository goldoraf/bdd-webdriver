package org.dbunit.dataset.datatype;

import java.util.Arrays;
import java.util.Collection;

public class ObmDataTypeFactory extends DefaultDataTypeFactory{
	
	private static final Collection<String> DATABASE_PRODUCTS = Arrays.asList(new String[]{"PostgreSQL","MySQL"});

	/**
	 * @see IDbProductRelatable#getValidDbProducts()
	 */
	@Override
	public Collection<String> getValidDbProducts()
	{
	    return DATABASE_PRODUCTS;
	}
	
	@Override
	public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException
	{
		if(sqlTypeName.equals("userstatus"))
		{
			return new UserStatusDataType();
		}
		return super.createDataType(sqlType, sqlTypeName);
	}
	
}
