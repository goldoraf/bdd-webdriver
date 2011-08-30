package org.obm.testing.support;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jbehave.scenario.definition.Blurb;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.definition.KeyWords;
import org.jbehave.scenario.i18n.I18nKeyWords;
import org.jbehave.scenario.reporters.ScenarioReporter;

public class Reporter implements ScenarioReporter {

	private final PrintStream output;
	private final Properties outputPatterns;
	private final KeyWords keywords;
	private final boolean reportErrors;
	private String stepsCache;
	private Throwable cause;

	public Reporter() {
		this(System.out);
	}

	public Reporter(PrintStream output) {
		this(output, new Properties(), new I18nKeyWords(), true);
	}

	public Reporter(Properties outputPatterns) {
		this(System.out, outputPatterns, new I18nKeyWords(), true);
	}

	public Reporter(KeyWords keywords) {
		this(System.out, new Properties(), keywords, true);
	}

	public Reporter(PrintStream output,
			Properties outputPatterns, KeyWords keywords, boolean reportErrors) {
		this.output = output;
		this.outputPatterns = outputPatterns;
		this.keywords = keywords;
		this.reportErrors = reportErrors;
	}

	public void successful(String step) {
		stepsCache.concat(format("successful", "{0}", step) + "\n");
	}

	public void pending(String step) {
		stepsCache.concat(format("pending", "{0} ({1})", step, keywords
						.pending()) + "\n");
	}

	public void notPerformed(String step) {
		stepsCache.concat(format("notPerformed", "{0} ({1})", step, keywords
				.notPerformed()) + "\n");
	}

	public void failed(String step, Throwable cause) {
		this.cause = cause;
		stepsCache.concat(format("failed", "{0} ({1})", step, keywords.failed()) + "\n");
	}

	public void afterScenario() {
		output.println();
		if (reportErrors && cause != null) {
			output.println(stepsCache);
			cause.printStackTrace(output);
			output.println();
		}
	}

	public void beforeScenario(String title) {
		cause = null;
		stepsCache = "";
		output.println(format("beforeScenario", "{0} {1}\n", keywords
				.scenario(), title));
	}

	public void afterStory() {
		output.println(format("afterStory", ""));
	}

	public void beforeStory(Blurb blurb) {
		String[] storyLines = blurb.asString().split("\r|\n|\r\n");
		output.println(format("beforeStory", "{0}", storyLines[0]));
	}

	public void givenScenarios(List<String> givenScenarios) {
		output.println(format("givenScenarios", "{0} {1}\n", keywords
				.givenScenarios(), givenScenarios));
	}

	public void examplesTable(ExamplesTable table) {
		output.println(format("examplesTable", "{0}\n\n{1}\n\n", keywords
				.examplesTable(), table));
	}

	public void examplesTableRow(Map<String, String> tableRow) {
		output.println(format("examplesTableRow", "\n{0} {1}\n", keywords
				.examplesTableRow(), tableRow));
	}

	protected String format(String key, String defaultPattern, Object... args) {
		return MessageFormat.format(lookupPattern(key, defaultPattern), args);
	}

	protected String lookupPattern(String key, String defaultPattern) {
		if (outputPatterns.containsKey(key)) {
			return outputPatterns.getProperty(key);
		}
		return defaultPattern;
	}

}

