package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import interfaces.IAppCalendar;
import interfaces.IEvent;
import interfaces.IUser;

import java.text.ParseException;
import java.util.*;

import jobs.Bootstrap;

import models.*;
import models.Calendar;
import models.AppExceptions.*;

@With(Secure.class)
public class Application extends Controller
{
	public static AppCalendar appCalendar=Bootstrap.getAppCalendar();
	public static IUser appUser;
	public static Calendar currentCalendar;
	
    public static void index() throws UnknownUserException
    {
    	List<String> allUserNames = showAllUsers();
    	String userName = Security.connected();
    	getCurrentUser();
    	if(userName!=null)
    	{
    		List<String> calendarNames = appCalendar.getAllCalendarsNamesFromUser(userName);
        	render(calendarNames, userName, allUserNames);
    	}
    	else
    	{
    		render(null, null);
    	}
    	
    }
    
    public static void createANewEvent(String eventName, String startDate, String endDate, boolean privateEvent) throws ParseException, AccessDeniedException, InvalidDateException, UnknownCalendarException
    {
    	getCurrentUser();
    	
    	Date start=Helper.parseStringToDate(startDate);
    	Date end=Helper.parseStringToDate(endDate);
    	
    	if(privateEvent)
    	{
    		System.out.println("CREATE PRIVATE EVENT");
    		appUser.createPrivateEvent(currentCalendar.getName(), eventName, start, end);
    	}
    	else
    	{
    		System.out.println("CREATE PUBLIC EVENT");
    		appUser.createPublicEvent(currentCalendar.getName(), eventName, start, end);
    	}
    	
    	listEventsFromCalendar(currentCalendar.getName());
    }
    public static void createANewPrivateEvent(String eventName, String startDate, String endDate) throws ParseException, AccessDeniedException, InvalidDateException, UnknownCalendarException
    {
    	createANewEvent(eventName, startDate, endDate, true);
    }
    
    public static void createANewPublicEvent(String eventName, String startDate, String endDate) throws ParseException, AccessDeniedException, InvalidDateException, UnknownCalendarException
    {
    	createANewEvent(eventName, startDate, endDate, false);
    }
    public static void mainMenuUser() throws UnknownUserException
    {
    	String userName = Security.connected();
    	System.out.println("USERNAME CONNECTED: " + userName);
    	List<String> calendarNames = appCalendar.getAllCalendarsNamesFromUser(userName);
    	ArrayList<String> allNames=appCalendar.getAllUserNames();
    	System.out.println("USERNAMES:");
    	for(String str:allNames)
    	{
    		System.out.println(str);
    	}
    	render();
    }

    public static List<String> showAllUsers()
    {
    	return appCalendar.getAllUserNames();
    }
    

    
    public static void listEventsFromCalendar(@Required String calendarName) throws UnknownCalendarException, AccessDeniedException, ParseException
    {
    	currentCalendar=appUser.getCalendar(calendarName);
    	
    	System.out.println("The appUser is " + appUser);
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
        System.out.println("Events (Iterator) are " + publicEventsIterator.hasNext());
        
        ArrayList<IEvent> events=new ArrayList<IEvent>();
    	
    	while(publicEventsIterator.hasNext())
    	{
    		IEvent currentEvent=publicEventsIterator.next();
    		System.out.println("CURRENT EVENT IS: " + currentEvent.getEventName());
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
    	ArrayList<String> allCalendarNames=appCalendar.getAllCalendarsNamesFromUser(userName);
    	render(allCalendarNames, userName);
    }
    
    private static void getCurrentUser()
    {
    	String userName = Security.connected();
    	
    	try {
			appUser=appCalendar.getCurrentUser(userName);
		} catch (UnknownUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}