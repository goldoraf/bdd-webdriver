package org.obm.testing.steps.calendar;

import static org.jbehave.Ensure.ensureThat;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.annotations.Then;

import org.obm.testing.pages.calendar.CalendarPage;
import org.obm.testing.pages.calendar.NewEventPage;
import org.obm.testing.support.Steps;

public class AppointmentCreationSteps extends Steps {
	
	@When("I create an appointment \"$title\" $day between $beginHour:$beginMin and $endHour:$endMin")
	public void createAppointment(String title, String day, Integer beginHour, Integer beginMin, Integer endHour, Integer endMin) {
		NewEventPage formPage = fillInEventDetails(title, day, beginHour, beginMin, endHour, endMin);
		formPage.submit();
	}
	
	@Then("I should be redirected to my agenda")
	public void redirectedToAgenda() {
		CalendarPage calendarPage = new CalendarPage(driver);
		ensureThat(currentPage(), is(calendarPage));
	}
	
	@Then("I should be informed that the event has been inserted")
	public void verifySuccessfulInsertionMessagePresence() {
		CalendarPage calendarPage = new CalendarPage(driver);
		ensureThat(calendarPage.displaysSuccessfulInsertionMessage());
	}
	
	@Then("I should see an event \"$title\" in my agenda")
	public void seeTheAppointment(String title) {
		CalendarPage calendarPage = new CalendarPage(driver);
		ensureThat(calendarPage.grid(), containsText(title));
	}
	
	protected NewEventPage fillInEventDetails(String title, String day, Integer beginHour, Integer beginMin, Integer endHour, Integer endMin) {
		NewEventPage formPage = nav.goToNewEventPage();
		formPage.setTitle(title);
		formPage.setBeginDate(getDate(day, beginHour, beginMin));
		formPage.setEndDate(getDate(day, endHour, endMin));
		return formPage;
	}
}
