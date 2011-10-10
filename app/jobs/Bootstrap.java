package jobs;

import java.text.ParseException;
import interfaces.IUser;
import controllers.Application;
import models.*;
import models.AppExceptions.AccessDeniedException;
import models.AppExceptions.CalendarIsNotUniqueException;
import models.AppExceptions.InvalidDateException;
import models.AppExceptions.UnknownCalendarException;
import models.AppExceptions.UnknownUserException;
import models.AppExceptions.UsernameAlreadyExistException;
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

			appCalendar.createUser("Beta", "123");
		}
		catch (UsernameAlreadyExistException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (CalendarIsNotUniqueException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (AccessDeniedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvalidDateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnknownCalendarException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnknownUserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static AppCalendar getAppCalendar()
	{
		return appCalendar;
	}
}
