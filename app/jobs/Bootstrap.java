package jobs;

import java.text.ParseException;
import interfaces.IUser;
import controllers.Application;
import models.*;
import models.AppExceptions.*;
import play.jobs.*;

@OnApplicationStart
public class Bootstrap extends Job
{
	private static AppCalendar appCalendar;

	public void doJob()
	{
		this.appCalendar = new AppCalendar();
		try
		{
			appCalendar.createUser("Alpha", "123");
			IUser userAlpha = appCalendar.loginUser("Alpha", "123");
			userAlpha.createNewCalendar("University");
			userAlpha.createPrivateEvent("University", "ESE exercise 2", Helper.parseStringToDate("28.09.2011"), Helper.parseStringToDate("5.10.2011"));
			userAlpha.createPublicEvent("University", "Holiday", Helper.parseStringToDate("24.12.2011"), Helper.parseStringToDate("15.02.2012"));
			userAlpha.createPrivateEvent("University", "FREE", Helper.parseStringToDate("21.10.2011"), Helper.parseStringToDate("22.10.2011"));
			userAlpha.createPrivateEvent("University", "FREE2", Helper.parseStringToDate("5.10.2011"), Helper.parseStringToDate("6.10.2011"));
			appCalendar.createUser("Beta", "456");
		}
		catch (UsernameAlreadyExistException e)
		{
			e.printStackTrace();
		}
		catch (CalendarIsNotUniqueException e)
		{
			e.printStackTrace();
		}
		catch (AccessDeniedException e)
		{
			e.printStackTrace();
		}
		catch (InvalidDateException e)
		{
			e.printStackTrace();
		}
		catch (UnknownCalendarException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		catch (UnknownUserException e)
		{
			e.printStackTrace();
		}
	}

	public static AppCalendar getAppCalendar()
	{
		return appCalendar;
	}
}
