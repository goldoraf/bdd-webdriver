package org.obm.testing.support;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import org.jbehave.scenario.Configuration;
import org.jbehave.scenario.PropertyBasedConfiguration;
import org.jbehave.scenario.ScenarioRunner;
import org.jbehave.scenario.definition.StoryDefinition;
import org.jbehave.scenario.errors.InvalidScenarioResourceException;
import org.jbehave.scenario.parser.PatternScenarioParser;
import org.jbehave.scenario.parser.ScenarioParser;
import org.jbehave.scenario.reporters.ScenarioReporter;

public class Scenario extends org.jbehave.scenario.Scenario {
	
	private String module;
	private String name;
	private Steps steps;
	private Configuration configuration;
	
	public Scenario(String moduleName, String scenarioName, Steps candidateSteps) {
		module = moduleName;
	    name = scenarioName;
		steps = candidateSteps;
		configuration = new PropertyBasedConfiguration() {
			public ScenarioReporter forReportingScenarios() {
				return new Reporter();
			}
	    };
	}
	
	public void runScenario() throws Throwable {
		String scenarioString = loadScenario();
        ScenarioParser parser = new PatternScenarioParser();
		StoryDefinition story = parser.defineStoryFrom(scenarioString);
		ScenarioRunner scenarioRunner = new ScenarioRunner();
		scenarioRunner.run(story, configuration, steps);
	}
	
	private String loadScenario() {
		try {
			FileInputStream stream = new FileInputStream(getScenarioPath());
			byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(bytes);
            return output.toString();
        } catch (Exception e) {
            throw new InvalidScenarioResourceException("Failed to convert scenario resource to string", e);
        }
	}
	
	private String getScenarioPath() {
		return "scenarios/" + module + "/" + name;
	}
}
