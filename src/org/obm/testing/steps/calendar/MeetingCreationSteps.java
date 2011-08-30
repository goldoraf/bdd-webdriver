package org.obm.testing.steps.calendar;

import static org.jbehave.Ensure.ensureThat;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.annotations.Then;

import org.obm.testing.pages.calendar.NewEventPage;
import org.obm.testing.pages.calendar.WaitingEventsPage;

public class MeetingCreationSteps extends AppointmentCreationSteps {
	
	@Given("I have $right rights on $user's calendar")
    public void haveRightOn(String right, String user) {
    	// we rely here on initial DB setup
    }
	
	@When("I create a meeting \"$title\" with $attendee $day between $beginHour:$beginMin and $endHour:$endMin")
	public void createMeeting(String title, String attendee, String day, Integer beginHour, Integer beginMin, Integer endHour, Integer endMin) {
		NewEventPage formPage = fillInEventDetails(title, day, beginHour, beginMin, endHour, endMin);
		formPage.addAttendee(getUserFullname(attendee));
		formPage.submit();
	}
	
	@Then("$attendee should see a waiting event named \"$title\" when he access his calendar")
	public void attendeeSeeAWaitingEvent(String attendee, String title) {
		logout();
		loginAs(attendee);
		nav.goToMyCalendar();
		WaitingEventsPage waitingEventsPage = new WaitingEventsPage(driver);
		
		ensureThat(currentPage(), is(waitingEventsPage));
		ensureThat(waitingEventsPage.containsEvent(title));
		ensureThat(waitingEventsPage.displaysCountEqualTo(1));
	}
}
