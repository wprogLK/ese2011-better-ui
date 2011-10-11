package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import interfaces.*;

import java.text.ParseException;
import java.util.*;

import jobs.Bootstrap;

import models.*;
import models.AppExceptions.UnknownCalendarException;
import models.Calendar;
import models.AppExceptions.*;

@With(Secure.class)
public class Application extends Controller
{
	public static AppCalendar appCalendar = Bootstrap.getAppCalendar();
	public static IUser appUser;
	public static Calendar currentCalendar;
//	public static ArrayList<ArrayList<Integer>> listDays;
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
    	currentCalendar=appUser.getCalendar(calendarName);

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

        Date realStartDate= Helper.parseStringToDate(startDate);
        Iterator<IEvent> publicEventsIterator = appCalendar.getUsersCalendarPublicEvents(userName, calendarName, realStartDate);

        ArrayList<IEvent> events=new ArrayList<IEvent>();

    	while(publicEventsIterator.hasNext())
    	{
    		IEvent currentEvent = publicEventsIterator.next();

    		events.add(currentEvent) ;
    	}

    	render(events, userName, calendarName);
    }

    public static void createNewCalendar(@Required String calendarName) throws CalendarIsNotUniqueException, UnknownUserException
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

    public static void calendar(String strCalendar, int selectedDay,int todayDay, int month, int year) throws UnknownCalendarException
    {
    	System.out.println("SELCECTED DAY " + selectedDay + " DAY: " + todayDay + " MONTH: " + month + " YEAR: " + year);	//TODO DELETE IT
    	
    	Date dateToday=new Date();
    	dateToday.setDate(todayDay);
    	if(todayDay==-1 && month==-1 && year==-1)
    	{
    		
    		todayDay=dateToday.getDate();
    		month=dateToday.getMonth()+1;
    		year=dateToday.getYear()+1900;
    		selectedDay=todayDay;
    		
    		System.out.println("DATE TODAY: " + dateToday);
    		
    	}
    	
    	ICalendar calendar = appUser.getCalendar(strCalendar);
    	Day objDay=null;
    	try 
    	{
    		objDay=createDayList(calendar, month, year, selectedDay, todayDay);
	    	System.out.println("OBJ TODAY :" + objDay.toString());			
	    	objDay.initIterator();
		} catch (ParseException e) {
			System.out.println("ERROR!");
			e.printStackTrace();
			System.exit(1);			//TODO
		}
    	
    	List<List<Day>> realList=(List<List<Day>>) listDays;
    
    	ArrayList<Integer> numbersArrayList=new ArrayList<Integer>();
    	numbersArrayList.add(0);
    	numbersArrayList.add(1);
    	numbersArrayList.add(2);
    	numbersArrayList.add(3);
    	
    	List<Integer> numbers=(List<Integer>)  numbersArrayList;
    	int realDay=todayDay;
    	System.out.println("CALENDAR: OBJ DAY: " + objDay.getDay());
    	render(calendar, realList, numbers, realDay, month, year , objDay);
    }

    private static void getCurrentUser() throws UnknownUserException
    {
    	String userName = Security.connected();
		appUser = appCalendar.getCurrentUser(userName);
    }

	private static Day createDayList(ICalendar cal, int month, int year, int selectedDay, int todayDay) throws ParseException
	{
		Day returnDay=null;
		ArrayList<List<Day>> tmpListDays=new ArrayList<List<Day>>();
	//	listDays = new ArrayList<ArrayList<Integer>>();
		int j = 1;
		for (int i = 0; i < 4; i++)
		{
			List<Day> dayNumberListTmp=(List<Day>) new ArrayList<Day>();
			//ArrayList dayNumberList = new ArrayList<Day>();
			for (int k = 0; k < 8; k++)
			{
				Day d = new Day(cal, j, month, year);
				
				if(j==todayDay)
				{
					System.out.println("TODAY IS " + todayDay + " j= " + j);
					d.setIsToday();
				}
				if(j==selectedDay)
				{
					System.out.println("-----_______DAY SELECTED " + d.getDay());
					returnDay=d; 
					d.setSelected();
				}
				
				dayNumberListTmp.add(d);
				//dayNumberList.add(d);
				j++;
				if(j == 33)
				{
					dayNumberListTmp.remove(7);
				}
			}
			tmpListDays.add(dayNumberListTmp);
		}
		
		listDays=(List<List<Day>>) tmpListDays;
		return returnDay;
	}
	
	public static void showNextEvent(Day day)
	{
		//TODO OLD METHOD
			System.out.println("DAY " + day.getDay());
			IEvent event;
			try 
			{
				day.nextIEvent();
				event=day.getCurrentEvent();
				if(event!=null)
				{
					render(event);
				}
			} 
			catch (Exception e) 
			{
				System.out.println("SHOW NOT POSSIBLE");
			}
			
	}
	
	public static void showEvent(IEvent event)
	{
		render(event);
	}
}