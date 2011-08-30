package org.obm.testing.tools;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

public class DatabaseExport {
	public static void main(String[] args) throws Exception {
        Connection jdbcConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost/obm_fresh?zeroDateTimeBehavior=convertToNull", "root", "password");
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        // full database export
        IDataSet fullDataSet = connection.createDataSet();
        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("db_data/fresh_install.xml"));
	}
}
