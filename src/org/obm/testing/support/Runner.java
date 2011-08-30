package org.obm.testing.support;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.dbunit.ObmDatabaseTester;
import org.jbehave.scenario.RunnableScenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Runner {
	
	public static String driverName;
	
	public static void main(String[] args) throws Exception { // TODO
		CommandLineParser parser = new PosixParser();
		try {
		    CommandLine line = parser.parse(getOptions(), args);
		    initialize(line);
		    if (line.hasOption("module")) {
		        String module = line.getOptionValue("module");
		    	if (line.hasOption("scenario")) {
		        	runScenario(instantiateScenario(module, line.getOptionValue("scenario")));
		        } else {
		        	runScenarios(getModuleScenarios(module));
		        }
		    } else {
		    	runScenarios(scanScenarios());
		    }
		} catch (ParseException e) {
		    System.out.println("Unexpected exception:" + e.getMessage());
		}
	}
	
	private static void initialize(CommandLine line) throws Exception {
		Properties props = getProperties(line);
		driverName = props.getProperty("browser.driver");
		
		initDbUnit(props);
		Navigation.HOST = props.getProperty("obm.host");
	}
	
	public static WebDriver getDriver() {
        if (driverName.equals("htmlunit")) {
        	LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"); 
        	return new HtmlUnitDriver(true);
        } else {
        	return new FirefoxDriver();
        }
    }
	
	private static void initDbUnit(Properties prop) {
		ObmDatabaseTester.SERVER_SQL = prop.getProperty("db.type");
    	ObmDatabaseTester.OBM_DATABASE = prop.getProperty("db.name");
    	ObmDatabaseTester.OBM_LOGIN = prop.getProperty("db.login");
    	ObmDatabaseTester.OBM_PASSWORD = prop.getProperty("db.password");
    	ObmDatabaseTester.OBM_URL = prop.getProperty("db.host");
	}
	
	private static Properties getProperties(CommandLine line) throws Exception {
		Properties props = new Properties(loadDefaultProperties());
		if (line.hasOption("driver")) {
			props.setProperty("browser.driver", line.getOptionValue("driver"));
		}
		return props;
	}
	
	private static Properties loadDefaultProperties() throws Exception {
		Properties defaults = new Properties();
		FileInputStream in;
		in = new FileInputStream("conf/testing.properties");
		defaults.load(in); 
		in.close();
		return defaults;
	}
	
	private static List<RunnableScenario> scanScenarios() {
		List<RunnableScenario> scenarios = new ArrayList<RunnableScenario>();
		
		File actual = new File("scenarios");
        for (File d : actual.listFiles()) {
            if (d.isDirectory() && !d.getName().equals(".svn")) {
            	String module = d.getName();
            	scenarios = getModuleScenarios(module, scenarios);
            }
        }
        return scenarios;
	}
	
	private static List<RunnableScenario> getModuleScenarios(String moduleName) {
		return getModuleScenarios(moduleName, new ArrayList<RunnableScenario>());
	}
	
	private static List<RunnableScenario> getModuleScenarios(String moduleName, List<RunnableScenario> scenarioList) {
		File d = new File("scenarios/" + moduleName);
		for (File s : d.listFiles()) {
    		if (s.isFile()) {
    			String scenarioName = s.getName();
    			try {
    				scenarioList.add(instantiateScenario(moduleName, scenarioName));
    			} catch (Exception e) {
    				// TODO
    			}
    		}
    	}
		return scenarioList;
	}
	
	private static Scenario instantiateScenario(String moduleName, String scenarioName) throws Exception {
		String stepsClassName = "org.obm.testing.steps." + moduleName + "." + camelize(scenarioName) + "Steps";
		Steps steps = (Steps) Class.forName(stepsClassName).newInstance();
		Scenario scenario = new Scenario(moduleName, scenarioName, steps);
		return scenario;
	}
	
	private static void runScenarios(List<RunnableScenario> scenarios) {
		for (RunnableScenario scenario : scenarios) {
			runScenario(scenario);
		}
	}
	
	private static void runScenario(RunnableScenario scenario) {
		try {
			scenario.runScenario();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private static Options getOptions() {
		Options options = new Options();
		options.addOption(new Option("m", "module", true, "name of a specific module to test"));
		options.addOption(new Option("s", "scenario", true, "name of a specific scenario to run"));
		options.addOption(new Option("d", "driver", true, "browser driver to use (firefox, htmlunit)"));
		return options;
	}
	
	private static String camelize(String value) {
		String[] strings = StringUtils.split(value.toLowerCase(), "_");
		for (int i = 0; i < strings.length; i++){
			strings[i] = StringUtils.capitalize(strings[i]);
		}
		return StringUtils.join(strings);
	}
}
