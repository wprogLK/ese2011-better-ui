package models;

import interfaces.ICalendar;
import interfaces.IEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Day
{
	private IEvent currentEvent;
	private boolean exists = true, isToday = false, selected = false;
	private int day;

	private ArrayList<IEvent> eventList = new ArrayList<IEvent>();
	private Iterator<IEvent> eventIterator;


	public Day(ICalendar icalendar, int day, int month, int year)
	{
		this.day = day;
		Calendar calendar = (Calendar) icalendar;
		String strDate = day + "." + month + "." + year;
		try
		{
			ArrayList<IEvent> tmp = calendar.getAllEventsAtDate(Helper.parseStringToDate(strDate));

			for(IEvent e : tmp)
			{

				if(e.getStartDate().equals(Helper.parseStringToDate(strDate)))
				{
					// Look only for events with startDate today
					eventList.add(e);
				}
			}

		}
		catch (ParseException e)
		{
			this.exists = false;
		}

		this.eventIterator = this.eventList.iterator();
	}

	public String toString()
	{
		return "Day " + day;
	}

	public void setIsToday()
	{
		this.isToday = true;
	}

	public boolean hasEvents()
	{
		return !this.eventList.isEmpty();
	}

	public boolean exist()
	{
		return this.exists;
	}

	public boolean isToday()
	{
		return this.isToday;
	}


	public void setSelected()
	{
		this.selected = true;
	}

	public boolean isSelected()
	{
		return this.selected;
	}

	public int getDay()
	{
		return this.day;
	}


	public void nextIEvent()
	{
		if(this.eventIterator == null)
		{
			this.eventIterator = this.eventList.iterator();
		}

		if(this.eventIterator.hasNext())
		{
			this.currentEvent = this.eventIterator.next();
		}
		else
		{
			this.eventIterator = this.eventList.iterator();
			if(this.eventIterator.hasNext())
			{
				this.currentEvent = this.eventIterator.next();
			}
			else
			{
				this.currentEvent = null;
			}
		}
	}

	public void initIterator()
	{
		this.eventIterator = this.eventList.iterator();
	}

	public IEvent getCurrentEvent()
	{
		if(this.currentEvent == null)
		{
			this.nextIEvent();
		}

		return currentEvent;
	}
}