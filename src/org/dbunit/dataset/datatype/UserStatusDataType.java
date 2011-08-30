package org.dbunit.dataset.datatype;

import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserStatusDataType  extends AbstractDataType{

	private static final Logger logger = LoggerFactory.getLogger(UserStatusDataType.class);
	
	public UserStatusDataType() {
		super("userstatus", Types.OTHER, String.class , false);
	}

	@Override
	public Object typeCast(Object value) throws TypeCastException {
		logger.debug("typeCast(value={}) - start", value);
		
		if (value instanceof String)
        {
            return value;
        }
		
		return null;
	}
	
	
}
