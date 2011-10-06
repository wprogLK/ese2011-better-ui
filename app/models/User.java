/**
 * Calendar framework
 */
package models;

import interfaces.IEvent;
import interfaces.IUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.Entity;

import controllers.Secure;

import play.db.jpa.Model;
import play.mvc.With;

import models.AppExceptions.*;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */

/** User object that represents an user.
 */
@Entity
public class User extends Model implements IUser
{
	public ArrayList<Calendar> calendars;
	public String name;

	/** Constructor for an user object. It contains the {@link Calendar} object.
	 * @param name The user name must be unique.
	 */
	public User(String name)
	{
		this.name = name;
		this.calendars = new ArrayList<Calendar>();
	}

	@Override
	public void createNewCalendar(String nameOfCalendar) throws CalendarIsNotUniqueException
	{
		isCalendarnameUnique(nameOfCalendar);

		Calendar newCalendar = new Calendar(this, nameOfCalendar);
		this.calendars.add(newCalendar);
	}

	private void isCalendarnameUnique(String nameOfCalendar) throws CalendarIsNotUniqueException
	{
		for (Calendar c : this.calendars)
		{
			if (c.getName().equals(nameOfCalendar))
			{
				throw new CalendarIsNotUniqueException(c);
			}
		}
	}

	@Override
	public void deleteCalendar(String nameOfCalendar) throws UnknownCalendarException
	{
		Calendar calendarToDelete = this.getCalendar(nameOfCalendar);
		this.calendars.remove(calendarToDelete);
	}

	@Override
	public ArrayList<String> getAllMyCalendarNames()
	{
		ArrayList<String> allCalendarNames = new ArrayList<String>();

		for (Calendar c : this.calendars)
		{
			allCalendarNames.add(c.getName());
		}
		return allCalendarNames;
	}

	@Override
	public boolean hasNoCalendar()
	{
		return this.calendars.isEmpty();
	}

	@Override
	public ArrayList<IEvent> getMyCalendarAllEventsAtDate(String calendarName, Date date) throws UnknownCalendarException, AccessDeniedException
	{
		Calendar calendar = this.getCalendar(calendarName);

		return calendar.getAllEventsAtDate(date);
	}

	@Override
	public Iterator<IEvent> getMyCalendarAllEventsStartingFrom(String calendarName, Date startDate) throws UnknownCalendarException, AccessDeniedException
	{
		Calendar calendar = this.getCalendar(calendarName);

		return calendar.getAllEventsStartingFrom(startDate);
	}

	@Override
	public Iterator<IEvent> getMyCalendarPublicEventsStartingFrom(String calendarName, Date startDate) throws UnknownCalendarException
	{
		Calendar calendar = this.getCalendar(calendarName);
		
		return calendar.getAllPublicEventsStartingFrom(startDate);
	}

	@Override
	public ArrayList<IEvent> getMyCalendarPublicEventsAtDate(String calendarName, Date date) throws UnknownCalendarException
	{
		Calendar calendar = this.getCalendar(calendarName);

		return calendar.getAllPublicEventsAtDate(date);
	}

	/* Operation on events */

	@Override
	public void createPrivateEvent(String calendarName, String eventName, Date startDate, Date endDate) throws AccessDeniedException, InvalidDateException, UnknownCalendarException
	{
		Calendar calendar = this.getCalendar(calendarName);

		calendar.createPrivateEvent(eventName, startDate, endDate);
	}

	@Override
	public void createPublicEvent(String calendarName, String eventName, Date startDate, Date endDate) throws AccessDeniedException, InvalidDateException, UnknownCalendarException
	{
		Calendar calendar = this.getCalendar(calendarName);

		calendar.createPublicEvent(eventName, startDate, endDate);
	}

	@Override
	public void editEventName(String calendarName, String eventName, Date startDate, String newEventName) throws AccessDeniedException, UnknownEventException, UnknownCalendarException, InvalidDateException
	{
		Calendar calendar = this.getCalendar(calendarName);
		calendar.editEvent(eventName, startDate, newEventName, null, null, null);
	}

	@Override
	public void editEventStartDate(String calendarName, String eventName, Date startDate, Date newStartDate) throws AccessDeniedException, UnknownEventException, UnknownCalendarException, InvalidDateException
	{
		Calendar calendar = this.getCalendar(calendarName);
		calendar.editEvent(eventName, startDate, null, newStartDate, null, null);
	}

	@Override
	public void editEventEndDate(String calendarName, String eventName, Date startDate, Date newEndDate) throws AccessDeniedException, UnknownEventException, UnknownCalendarException, InvalidDateException
	{
		Calendar calendar = this.getCalendar(calendarName);
		calendar.editEvent(eventName, startDate, null, null, newEndDate, null);
	}

	@Override
	public void editEventStateToPublic(String calendarName, String eventName, Date startDate) throws AccessDeniedException, UnknownEventException, UnknownCalendarException, InvalidDateException
	{
		Calendar calendar = this.getCalendar(calendarName);
		calendar.editEvent(eventName, startDate, null, null, null, false);
	}

	@Override
	public void editEventStateToPrivate(String calendarName, String eventName, Date startDate) throws AccessDeniedException, UnknownEventException, UnknownCalendarException, InvalidDateException
	{
		Calendar calendar = this.getCalendar(calendarName);
		calendar.editEvent(eventName, startDate, null, null, null, true);
	}

	@Override
	public void deleteEvent(String calendarName, String eventName, Date startDate) throws AccessDeniedException, UnknownEventException, UnknownCalendarException
	{
		Calendar calendar = this.getCalendar(calendarName);
		calendar.deleteEvent(eventName, startDate);
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public Calendar getCalendar(String calendarName) throws UnknownCalendarException
	{
		Iterator<Calendar> iteratorCalendar = this.calendars.iterator();
		while (iteratorCalendar.hasNext())
		{
			Calendar currentCalendar = iteratorCalendar.next();
			if (currentCalendar.getName().equals(calendarName))
			{
				return currentCalendar;
			}
		}
		throw new UnknownCalendarException(calendarName);
	}
}
