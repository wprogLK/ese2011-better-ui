/**
 * Calendar framework
 */
package interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import models.*;
import models.AppExceptions.*;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */
public interface IUser
{
	/////////////////////////
	// CALENDAR OPERATIONS //
	/////////////////////////

	/** Creates a new {@link Calendar} for the specified user.
	 * @param nameOfCalendar The title of the calendar to be created. The name must be unique.
	 * @throws CalendarIsNotUniqueException If a {@code calendar} with the same title already exists in the calendar list of the {@code user}.
	 */
	public void createNewCalendar(String nameOfCalendar) throws CalendarIsNotUniqueException;

	/** Removes the calendar including all events in it from the user.
	 * @param nameOfCalendar The title the calendar was given at creation time.
	 * @throws UnknownCalendarException If the {@code user} has no calendar with such a name.
	 */
	public void deleteCalendar(String nameOfCalendar) throws UnknownCalendarException;

	/** Provides an {@link ArrayList} with all calendar titles created by the user.
	 * @return The string literals of the calendars as {@link ArrayList}.
	 */
	public ArrayList<String> getAllMyCalendarNames();

	/** Tells, if the user has any calendars.
	 * @return If the {@link Calendar} {@link ArrayList} is empty, {@code true} is returned, {@code false} in all other cases.
	 */
	public boolean hasNoCalendar();

	/** Provides all (public and private) events at a given {@link Date} from the specified calendar as an {@link ArrayList}.
	 * @param calendarName Title of the calendar to identify it.
	 * @param date Date form which to list all events.
	 * @return All events in the calendar.
	 * @throws AccessDeniedException When passwords do not match up.
	 * @throws UnknownCalendarException If the {@code user} has no calendar with such a name.
	 * @see Authentication#getUser(String, String)
	 */
	public ArrayList<IEvent> getMyCalendarAllEventsAtDate(String calendarName, Date date) throws UnknownCalendarException, AccessDeniedException;

	/** Provides an {@link Iterator} with all (public and private) events from the specified calendar, that begin at the given date {@code startDate}.
	 * @param calendarName Title of the calendar to identify it.
	 * @param startDate Starting point of events.
	 * @return All events that are set to start at {@code startDate}.
	 * @throws AccessDeniedException When passwords do not match up.
	 * @throws UnknownCalendarException If the {@code user} has no calendar with such a name.
	 */
	public Iterator<IEvent> getMyCalendarAllEventsStartingFrom(String calendarName, Date startDate) throws UnknownCalendarException, AccessDeniedException;

	public Iterator<IEvent> getMyCalendarPublicEventsStartingFrom(String calendarName, Date startDate) throws UnknownCalendarException;

	public ArrayList<IEvent> getMyCalendarPublicEventsAtDate(String calendarName, Date date) throws UnknownCalendarException;

	///////////////////
	// EVENT ACTIONS //
	///////////////////

	/** Creates a new private event in the given {@link Calendar}.
	 * @param calendarName Title of the calendar to identify it.
	 * @param eventName Title of the event to identify it.
	 * @param startDate Date of the event to begin.
	 * @param endDate Date of the event to end.
	 * @throws AccessDeniedException When passwords do not match up.
	 * @throws InvalidDateException If {@code endDate} is placed before {@code startDate}.
	 * @throws UnknownCalendarException If the {@code user} has no calendar with such a name.
	 */
	public void createPrivateEvent(String calendarName, String eventName, Date startDate, Date endDate) throws AccessDeniedException, InvalidDateException, UnknownCalendarException;

	/** Creates a new public event in the given {@link Calendar}.
	 * @param calendarName Title of the calendar to identify it.
	 * @param eventName Title of the event to identify it.
	 * @param startDate Date of the event to begin.
	 * @param endDate Date of the event to end.
	 * @throws AccessDeniedException When passwords do not match up.
	 * @throws UnknownCalendarException If the {@code user} has no calendar with such a name.
	 * @throws InvalidDateException If {@code endDate} is placed before {@code startDate}.
	 */
	public void createPublicEvent(String calendarName, String eventName, Date startDate, Date endDate) throws AccessDeniedException, InvalidDateException, UnknownCalendarException;

	public void editEventName(String calendarName, String eventName, Date startDate, String newEventName) throws AccessDeniedException, UnknownEventException, UnknownCalendarException, InvalidDateException;

	public void editEventStartDate(String calendarName, String eventName, Date startDate, Date newStartDate) throws AccessDeniedException, UnknownEventException, UnknownCalendarException, InvalidDateException;

	public void editEventEndDate(String calendarName, String eventName, Date startDate, Date newEndDate) throws AccessDeniedException, UnknownEventException, UnknownCalendarException, InvalidDateException;

	public void editEventStateToPublic(String calendarName, String eventName, Date startDate) throws AccessDeniedException, UnknownEventException, UnknownCalendarException, InvalidDateException;

	public void editEventStateToPrivate(String calendarName, String eventName, Date startDate) throws AccessDeniedException, UnknownEventException, UnknownCalendarException, InvalidDateException;

	/** Deletes the event from the calendar.
	 * @param calendarName Title of the calendar to identify it.
	 * @param eventName Title of the event to identify it.
	 * @param startDate Date when the event to be deleted starts.
	 * @throws UnknownEventException If the event is not in the calendar.
	 * @throws AccessDeniedException When passwords do not match up.
	 * @throws UnknownCalendarException If the {@code user} has no calendar with such a name.
	 */
	public void deleteEvent(String calendarName, String eventName, Date startDate) throws AccessDeniedException, UnknownEventException, UnknownCalendarException;

	String getName();

	//////////////////////
	// ONLY FOR TESTING //
	//////////////////////

	/** Provides a {@link Calendar} object. It can be used to perform further operations on the calendar.
	 * <p>
	 * <u><b>Warning</u></b>: This method is only for testing in the IUser interface. Normally the visibility is public but not implemented in the IUser interface!
	 * </p>
	 * @param calendarName The title the calendar was given at creation time.
	 * @return The calendar with the corresponding {@code calendarName}.
	 * @throws UnknownCalendarException If the {@code user} has no calendar with such a name.
	 */
	@OnlyForTesting
	public Calendar getCalendar(String calendarName) throws UnknownCalendarException;

}
