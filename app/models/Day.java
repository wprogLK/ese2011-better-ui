package models;

import interfaces.ICalendar;
import interfaces.IEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Day
{
	private boolean hasEvent = true, exists = true, isToday = false;
	private int day;

	public Day(ICalendar icalendar, int day, int month, int year)
	{
		this.day = day;
		Calendar calendar = (Calendar) icalendar;
		String date = day + "." + month + "." + year;
		ArrayList<IEvent> eventList;
		try
		{
			eventList = calendar.getAllEventsAtDate(Helper.parseStringToDate(date));
			if(eventList.isEmpty())
			{
				this.hasEvent = false;
			}
		}
		catch (ParseException e)
		{
			this.exists = false;
		}
	}

	public void checkTodayDate(Date date)
	{
	    String dateString = DateFormat.getDateInstance().format(date);

		java.util.Calendar cal = java.util.Calendar.getInstance();
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
	    String todayString = sdf.format(cal.getTime());

		if(dateString.equals(todayString))
		{
			this.isToday = true;
		}
	}
}