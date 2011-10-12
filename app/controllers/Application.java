package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import interfaces.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import jobs.Bootstrap;

import models.*;
import models.Calendar;
import models.AppExceptions.*;

@With(Secure.class)
public class Application extends Controller
{
	public static AppCalendar appCalendar = Bootstrap.getAppCalendar();
	public static IUser appUser;
	public static Calendar currentCalendar;
	public static List<List<Day>> listDays;
	public static Day objSelectedDay;

    public static void index() throws UnknownUserException
    {
    	List<String> allUserNames = showAllUsers();
    	String userName = Security.connected();
    	getCurrentUser();
    	if(userName != null)
    	{
    		List<String> calendarNames = appCalendar.getAllCalendarsNamesFromUser(userName);
        	render(calendarNames, userName, allUserNames);
    	}
    	else
    	{
    		render(null, null);
    	}
    }

    public static void createANewEvent(String eventName, String startDate, String endDate, boolean privateEvent) throws ParseException, AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownUserException
    {
    	getCurrentUser();

    	Date start = Helper.parseStringToDate(startDate);
    	Date end = Helper.parseStringToDate(endDate);

    	if(privateEvent)
    	{
    		appUser.createPrivateEvent(currentCalendar.getName(), eventName, start, end);
    	}
    	else
    	{
    		appUser.createPublicEvent(currentCalendar.getName(), eventName, start, end);
    	}

    	listEventsFromCalendar(currentCalendar.getName());
    }
    public static void createANewPrivateEvent(String eventName, String startDate, String endDate) throws ParseException, AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownUserException
    {
    	createANewEvent(eventName, startDate, endDate, true);
    }

    public static void createANewPublicEvent(String eventName, String startDate, String endDate) throws ParseException, AccessDeniedException, InvalidDateException, UnknownCalendarException, UnknownUserException
    {
    	createANewEvent(eventName, startDate, endDate, false);
    }

    public static List<String> showAllUsers()
    {
    	return appCalendar.getAllUserNames();
    }

    public static void listEventsFromCalendar(@Required String calendarName) throws UnknownCalendarException, AccessDeniedException, ParseException
    {
    	currentCalendar = appUser.getCalendar(calendarName);

    	ArrayList<IEvent> eventsList = appUser.getMyCalendarAllEventsAtDate(calendarName, Helper.parseStringToDate("01.01.1970"));
    	render(calendarName, eventsList);
    }

    public static IAppCalendar getApp()
    {
		return appCalendar;
    }

    public static void showPublicEvents(@Required String userName, @Required String calendarName, @Required String startDate) throws UnknownUserException, AccessDeniedException, UnknownCalendarException, ParseException, UsernameAlreadyExistException, CalendarIsNotUniqueException, InvalidDateException
    {
        if(validation.hasErrors())
        {
            flash.error("No field must remain empty!");
            index();
        }

        Date realStartDate = Helper.parseStringToDate(startDate);
        Iterator<IEvent> publicEventsIterator = appCalendar.getUsersCalendarPublicEvents(userName, calendarName, realStartDate);

        ArrayList<IEvent> events = new ArrayList<IEvent>();

    	while(publicEventsIterator.hasNext())
    	{
    		IEvent currentEvent = publicEventsIterator.next();

    		events.add(currentEvent) ;
    	}

    	render(events, userName, calendarName);
    }

    public static void createNewCalendar(@Required String calendarName) throws UnknownUserException, CalendarIsNotUniqueException
    {

        if(validation.hasErrors())
        {
            flash.error("No field must remain empty!");
            index();
        }

        appUser.createNewCalendar(calendarName);
        index();
    }

    public static void deleteCalendar(@Required String calendarName) throws UnknownCalendarException, UnknownUserException
    {
        if(validation.hasErrors())
        {
            flash.error("No field must remain empty!");
            index();
        }

    	appUser.deleteCalendar(calendarName);
        index();
    }

    public static void mainMenuUser(String userName, String password) throws UnknownUserException, AccessDeniedException, UsernameAlreadyExistException, CalendarIsNotUniqueException, InvalidDateException, UnknownCalendarException, ParseException
    {
    	try
    	{
    		appCalendar.loginUser(userName, password);
    		getCurrentUser();
    	}
    	catch(UnknownUserException e)
    	{
    		error(e);
    	}
    	catch(AccessDeniedException e)
    	{
    		error(e);
    	}
    	render(userName);
    }

    public static void showCalendars(String userName) throws UnknownUserException
    {
    	ArrayList<String> allCalendarNames = appCalendar.getAllCalendarsNamesFromUser(userName);
    	render(allCalendarNames, userName);
    }

    public static void calendar(String strCalendar, int selectedDay, int todayDay, int month, int year) throws UnknownCalendarException, ParseException
    {
    	Date dateToday = new Date();
    	dateToday.setDate(todayDay);
    	if(todayDay == -1 && month == -1 && year == -1)
    	{

    		todayDay = dateToday.getDate();
    		month = dateToday.getMonth() + 1;
    		year = dateToday.getYear() + 1900;
    		selectedDay = todayDay;
    	}

    	ICalendar calendar = appUser.getCalendar(strCalendar);
    	Day objDay = createDayList(calendar, month, year, selectedDay, todayDay);
	    objDay.initIterator();

    	List<List<Day>> realList = listDays;
    	ArrayList<Integer> numbersArrayList = new ArrayList<Integer>();
    	numbersArrayList.add(0);
    	numbersArrayList.add(1);
    	numbersArrayList.add(2);
    	numbersArrayList.add(3);
    	numbersArrayList.add(4);

    	List<Integer> numbers = (List<Integer>) numbersArrayList;
    	int realDay = todayDay;

    	SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
    	String strMonth = sdf.format(Helper.parseStringToDate(selectedDay+"."+month+"."+year));
    	render(calendar, realList, numbers, realDay, month, strMonth, year, objDay);
    }

    private static void getCurrentUser() throws UnknownUserException
    {
    	String userName = Security.connected();
		appUser = appCalendar.getCurrentUser(userName);
    }

	private static Day createDayList(ICalendar cal, int month, int year, int selectedDay, int todayDay) throws ParseException
	{
		Day returnDay = null;
		ArrayList<List<Day>> tmpListDays = new ArrayList<List<Day>>();
		int j = 1;
		for (int i = 0; i < 5; i++)
		{
			List<Day> dayNumberListTmp = (List<Day>) new ArrayList<Day>();
			for (int k = 0; k < 7; k++)
			{
				if(j == 32)
				{
					break;
				}
				Day d = new Day(cal, j, month, year);

				if(j == todayDay)
				{
					d.setIsToday();
				}
				if(j == selectedDay)
				{
					returnDay = d;
					d.setSelected();
				}

				dayNumberListTmp.add(d);
				//dayNumberList.add(d);
				j++;
			}
			tmpListDays.add(dayNumberListTmp);
		}

		listDays = tmpListDays;
		return returnDay;
	}

/*
	public static void showNextEvent(Day day)
	{
		// OLD METHOD
		System.out.println("DAY " + day.getDay());
		IEvent event;
		day.nextIEvent();
		event = day.getCurrentEvent();
		if(event != null)
		{
			render(event);
		}
	}
*/

	public static void oneMonthBack(String cal, int selectedDay, int todayDay, int month, int year) throws UnknownCalendarException, ParseException
	{
		month -= 1;
		if(month <= 0)
		{
			month += 12;
			year -= 1;
		}
		calendar(cal, selectedDay, todayDay, month, year);
	}

	public static void oneMonthForward(String cal, int selectedDay, int todayDay, int month, int year) throws UnknownCalendarException, ParseException
	{
		month += 1;
		if(month >= 13)
		{
			month -= 12;
			year += 1;
		}
		calendar(cal, selectedDay, todayDay, month, year);
	}
}