package org.obm.testing.steps.calendar;

import static org.hamcrest.core.IsNot.not;
import static org.jbehave.Ensure.ensureThat;

import java.io.FileWriter;
import java.io.IOException;

import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.annotations.Then;

import org.obm.testing.pages.calendar.CalendarPage;
import org.obm.testing.pages.calendar.ConsultEventPage;
import org.obm.testing.pages.calendar.NewEventPage;

public class EventAttachmentsSteps extends AppointmentCreationSteps {
	
	@When("I create a meeting \"$title\" with $attendee $day between $beginHour:$beginMin and $endHour:$endMin, with an attached file \"$fileName\"")
	public void createMeetingWithAttachedFile(String title, String attendee, String day, Integer beginHour, Integer beginMin, Integer endHour, Integer endMin, String fileName) throws IOException {
		NewEventPage formPage = fillInEventDetails(title, day, beginHour, beginMin, endHour, endMin);
		formPage.addAttendee(getUserFullname(attendee));
		formPage.attachDocument(createTmpFile(fileName));
		formPage.submit();
	}
	
	@When("I create a meeting \"$title\" with $attendee $day between $beginHour:$beginMin and $endHour:$endMin, with attachments authorized")
	public void createMeetingWithAttachmentsAuthorized(String title, String attendee, String day, Integer beginHour, Integer beginMin, Integer endHour, Integer endMin) {
		NewEventPage formPage = fillInEventDetails(title, day, beginHour, beginMin, endHour, endMin);
		formPage.addAttendee(getUserFullname(attendee));
		formPage.allowAttachments();
		formPage.submit();
	}
	
	@Then("I should be able to download the file \"$fileName\" when I consult the \"$title\" event")
	public void ownerShouldSeeTheDocument(String fileName, String title) {
		CalendarPage calendarPage = new CalendarPage(driver);
		ConsultEventPage eventPage = calendarPage.consultEvent(title);
		ensureThat(eventPage.content(), containsText(fileName));
	}
	
	@Then("$attendee should be able to download the file \"$fileName\" when he consults the \"$title\" event")
	public void anAttendeeShouldSeeTheDocument(String attendee, String fileName, String title) {
		logout();
		loginAs(attendee);
		ConsultEventPage eventPage = nav.goToMyCalendar().consultEvent(title);
		ensureThat(eventPage.content(), containsText(fileName));
	}
	
	@Then("an user with read permissions on $owner calendar shouldn't see the file \"$fileName\" when he consults the \"$title\" event")
	public void otherUsersShouldnotSeeTheDocument(String owner, String fileName, String title) {
		logout();
		loginAs("user2");
		ConsultEventPage eventPage = nav.goToMyCalendar().displayAgenda(getUserFullname(owner)).consultEvent(title);
		ensureThat(eventPage.content(), not(containsText(fileName)));
	}
	
	@Then("$attendee should be able to attach a file \"$fileName\" when he consults the \"$title\" event")
	public void anAttendeeCanAttachDocuments(String attendee, String fileName, String title) throws IOException {
		logout();
		loginAs(attendee);
		ConsultEventPage eventPage = nav.goToMyCalendar().consultEvent(title);
		ensureThat(eventPage.displaysDocumentAttachmentZone());
		eventPage.attachDocument(createTmpFile(fileName));
		ensureThat(eventPage.content(), containsText(fileName));
	}
	
	protected String createTmpFile(String fileName) throws IOException {
		FileWriter file;
		String filePath = "/tmp/" + fileName;
		file = new FileWriter(filePath);
		file.write("hello world");
		file.close();
		return filePath;
	}
}
