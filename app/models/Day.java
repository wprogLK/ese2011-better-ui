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
	private boolean hasEvent, exists = true, isToday = false, selected=false;
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
			ArrayList<IEvent> tmp= calendar.getAllEventsAtDate(Helper.parseStringToDate(strDate));
			
			for(IEvent e: tmp)
			{
				
				if(e.getStartDate().equals(Helper.parseStringToDate(strDate)))
				{
					System.out.println("DAY NR: " + day + " EVENT  " + e.getEventName());
					eventList.add(e);		//Look only for events with startdate today
				}
			}
			
		}
		catch (ParseException e)
		{
			this.exists = false;
		}
		
		this.eventIterator=this.eventList.iterator();
	}


//	public void checkTodayDate(Date date)
//	{
//	    String dateString = DateFormat.getDateInstance().format(date);
//
//		java.util.Calendar cal = java.util.Calendar.getInstance();
//	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
//	    String todayString = sdf.format(cal.getTime());
//
//		if(dateString.equals(todayString))
//		{
//			this.isToday = true;
//		}
//	}
	
	public String toString()
	{
		return "Day " + day;
	}
	
	public void setIsToday()
	{
		this.isToday=true;
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
		this.selected=true;
	}
	
	public boolean isSelected()
	{
		return this.selected;
	}
	
	public int getDay()
	{
		return this.day;
	}
	
	
	public IEvent nextIEvent() throws Exception
	{
		System.out.println("Try next Event of Day " + this.day);
		if(this.eventIterator==null)
		{
			//throw new Exception();
			this.eventIterator=this.eventList.iterator();
			System.out.println("SET ITERATOR");
		}
		
		if(this.eventIterator.hasNext())
		{
			return this.eventIterator.next();
		}
		else
		{
			this.eventIterator=this.eventList.iterator();
			if(this.eventIterator.hasNext())
			{
				return this.eventIterator.next();
			}
			else
			{
				return null;
			}
		}
	}
	
	public void initIterator()
	{
		this.eventIterator=this.eventList.iterator();
	}

}