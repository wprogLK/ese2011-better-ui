/**
 * Calendar framework
 */
package models;
import interfaces.IEvent;
import java.util.Date;
import models.AppExceptions.*;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */

public class Event implements IEvent, Comparable<Event>
{
	private String eventName;
	private Date startDate;
	private Date endDate;
	private boolean isPrivate;

	/** Constructor for specific event with the state 'private'
	 * @param eventName A precise name / description for the event
	 * @param startDate The begin of the mentioned event
	 * @param endDate The end of the mentioned event
	 * @throws InvalidDateException If {@code endDate} is placed before {@code startDate}.
	 */
	public Event(String eventName, Date startDate, Date endDate) throws InvalidDateException
	{

		checkValidDates(startDate, endDate);

		this.eventName = eventName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isPrivate = true;
	}

	private void checkValidDates(Date startDate, Date endDate) throws InvalidDateException
	{
		if (startDate.after(endDate))
		{
			throw new InvalidDateException(startDate, endDate);
		}

	}

	/////////////
	// GETTERS //
	/////////////

	@Override
	public String getEventName()
	{
		return this.eventName;
	}

	@Override
	public Date getStartDate()
	{
		return this.startDate;
	}

	@Override
	public Date getEndDate()
	{
		return this.endDate;
	}

	@Override
	public boolean isPrivate()
	{
		return this.isPrivate;
	}

	@Override
	public boolean isPublic()
	{
		return !this.isPrivate;
	}

	/////////////
	// SETTERS //
	/////////////

	public void setEventName(String name)
	{
		this.eventName = name;
	}

	public void setStartDate(Date startDate) throws InvalidDateException
	{
		checkValidDates(startDate, this.endDate);

		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) throws InvalidDateException
	{
		checkValidDates(this.startDate, endDate);

		this.endDate = endDate;
	}

	public void setPrivateVisibility(boolean value)
	{
		this.isPrivate = value;
	}

	/////////////
	// COMPARE //
	/////////////

	@Override
	public int compareTo(Event eventToCompare)
	{
		if(this.startDate.before(eventToCompare.getStartDate()))
		{
			return -1;
		}
		else if(this.startDate.after(eventToCompare.getStartDate()))
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
}
