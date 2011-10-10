/**
 * Calendar framework
 */
package models;

import interfaces.ICalendar;
import interfaces.IEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import models.AppExceptions.*;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */

public class Calendar implements ICalendar
{
	private String name;
	private User owner;

	private ArrayList<IEvent> privateEvents;
	private ArrayList<IEvent> publicEvents;

	public Calendar(User owner, String name)
	{
		this.owner = owner;
		this.name = name;
		this.privateEvents = new ArrayList<IEvent>();
		this.publicEvents = new ArrayList<IEvent>();
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public Iterator<IEvent> getAllPublicEventsStartingFrom(Date startDate)
	{
		return this.getEventsWithStartDateOrMore(startDate, this.publicEvents).iterator();
	}

	@Override
	public ArrayList<IEvent> getAllPublicEventsAtDate(Date date)
	{
		return this.getEventsWithDate(date, this.publicEvents);
	}

	/** Creates a new private event in the given {@link Calendar}.
	 * @param name Title of the event to identify it.
	 * @param startDate Date of the event to begin.
	 * @param endDate Date of the event to end.
	 * @throws InvalidDateException If {@code endDate} is placed before {@code startDate}.
	 */
	public void createPrivateEvent(String name, Date startDate, Date endDate) throws InvalidDateException
	{
		Event newEvent = new Event(name, startDate, endDate);
		this.privateEvents.add(newEvent);
	}

	/** Creates a new public event in the given {@link Calendar}.
	 * @param name Title of the event to identify it.
	 * @param startDate Date of the event to begin.
	 * @param endDate Date of the event to end.
	 * @throws InvalidDateException If {@code endDate} is placed before {@code startDate}.
	 */
	public void createPublicEvent(String name, Date startDate, Date endDate) throws InvalidDateException
	{
		Event newEvent = new Event(name, startDate, endDate);
		newEvent.setPrivateVisibility(false);
		this.publicEvents.add(newEvent);
	}

	public User getOwner()
	{
		return this.owner;
	}

	/* Functions to get particular events from calendars */

	/** Provides an {@link Iterator} with all (public and private) events, that begin at the given {@code startDate} or after it.
	 * @param startDate Starting point of events.
	 * @return All events that are set to start at {@code startDate} or later.
	 */
	public Iterator<IEvent> getAllEventsStartingFrom(Date startDate)
	{
		ArrayList<IEvent> eventList = new ArrayList<IEvent>();
		eventList.addAll(this.privateEvents);
		eventList.addAll(this.publicEvents);
		eventList = this.getEventsWithStartDateOrMore(startDate, eventList);
		this.sortEvents(eventList);
		return eventList.iterator();
	}

	/** Provides all (public and private) events happening at a given {@code date}.
	 * @param date Date form which to list all current events.
	 * @return All matching events as an {@link ArrayList}.
	 */
	public ArrayList<IEvent> getAllEventsAtDate(Date date)
	{
		ArrayList<IEvent> eventList = new ArrayList<IEvent>();
		eventList.addAll(this.privateEvents);
		eventList.addAll(this.publicEvents);
		this.sortEvents(eventList);
		return this.getEventsWithDate(date, eventList);
	}

	@Override
	public Event getEvent(String eventName, Date startDate) throws UnknownEventException
	{
		Iterator<IEvent> iteratorAllEvents = this.getAllEventsStartingFrom(startDate);
		IEvent currentEvent = null;
		while (iteratorAllEvents.hasNext())
		{
			currentEvent = iteratorAllEvents.next();
			if (currentEvent.getEventName().equals(eventName) && currentEvent.getStartDate().equals(startDate))
			{
				return (Event) currentEvent;
			}
		}
		throw new UnknownEventException(eventName, startDate);
	}

	/* Functions used to modify events */

	public void editEvent(String eventName, Date startDate, String newEventName, Date newStartDate, Date newEndDate, Boolean newPrivateVisible) throws UnknownEventException, InvalidDateException
	{
		Event e = this.getEvent(eventName, startDate);

		if(newEventName != null)
		{
			e.setEventName(newEventName);
		}

		if(newStartDate != null)
		{
			e.setStartDate(newStartDate);
		}

		if(newEndDate != null)
		{
			e.setEndDate(newEndDate);
		}

		if(newPrivateVisible != null)
		{
			e.setPrivateVisibility(newPrivateVisible);
		}
	}

	/* Private methods */

	private ArrayList<IEvent> getEventsWithDate(Date date, ArrayList<IEvent> events)
	{
		ArrayList<IEvent> output = new ArrayList<IEvent>();
		for (IEvent event : events)
		{
			if (event.getEndDate().after(date) || event.getEndDate().equals(date))
			{
				output.add(event);
			}
		}
		return output;
	}

	private ArrayList<IEvent> getEventsWithStartDateOrMore(Date startDate, ArrayList<IEvent> events)
	{
		ArrayList<IEvent> output = new ArrayList<IEvent>();
		for (IEvent event : events)
		{
			if (event.getStartDate().after(startDate) || event.getStartDate().equals(startDate)) // = if (! event.getStartDate().before(startDate))
			{
				output.add(event);
			}
		}
		return output;
	}

	private void sortEvents(ArrayList<IEvent> inputList)
	{
		IEvent[] arrayOfEvents = new IEvent[inputList.size()];
		inputList.toArray(arrayOfEvents);

		Arrays.sort(arrayOfEvents);

		inputList.clear();

		for(IEvent e: arrayOfEvents)
		{
			inputList.add(e);
		}
	}

	/** Deletes the event from the calendar.
	 * @param eventName Title of the event to identify it.
	 * @param startDate Date when the event to be deleted starts.
	 * @throws UnknownEventException If the event is not in the calendar.
	 */
	public void deleteEvent(String eventName, Date startDate) throws UnknownEventException
	{
		Event eventToDelete = this.getEvent(eventName, startDate);
		if (eventToDelete.isPrivate())
		{
			this.privateEvents.remove(eventToDelete);
		}
		else
		{
			this.publicEvents.remove(eventToDelete);
		}
	}
}
