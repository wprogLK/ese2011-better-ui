/**
 * Calendar framework
 */
package models;

import interfaces.IAppCalendar;
import interfaces.IEvent;
import interfaces.IUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import play.mvc.With;

import controllers.Secure;
import controllers.Security;

import models.AppExceptions.*;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */
public class AppCalendar implements IAppCalendar
{
	private  Authentication auth;

	public AppCalendar()
	{
			auth = new Authentication();
	}

	@Override
	public void createUser(String username, String password) throws UsernameAlreadyExistException, UnknownUserException
	{
		this.auth.createNewUser(username, password);
		User user = this.auth.getUser(username);
		user.save();
	}

	@Override
	public void changePassword(String username, String oldPassword, String newPassword) throws UnknownUserException, AccessDeniedException
	{
		this.auth.setNewPassword(username, oldPassword, newPassword);
	}

	@Override
	public void deleteUser(String username, String password) throws UnknownUserException, AccessDeniedException
	{
		this.auth.deleteUser(username, password);
	}

	@Override
	public ArrayList<String> getAllCalendarsNamesFromUser(String username) throws UnknownUserException
	{
		User user = this.auth.getUser(username);
		return user.getAllMyCalendarNames();
	}

	@Override
	public ArrayList<IEvent> getUsersCalendarPublicEventsAtDate(String username, String calendarName, Date date) throws UnknownUserException, UnknownCalendarException, AccessDeniedException
	{
		User user = this.auth.getUser(username);
		return user.getMyCalendarPublicEventsAtDate(calendarName, date);
	}

	@Override
	public Iterator<IEvent> getUsersCalendarPublicEvents(String username, String calendarName, Date startDate) throws UnknownUserException, UnknownCalendarException, AccessDeniedException
	{
		User user = this.auth.getUser(username);

		return user.getMyCalendarPublicEventsStartingFrom(calendarName, startDate);
	}

	@Override
	public IUser loginUser(String username, String password) throws UnknownUserException, AccessDeniedException
	{
		return this.auth.getUser(username, password);
	}

	@Override
	public ArrayList<String> getAllUserNames()
	{
		return this.auth.getAllUserNames();
	}

	public User getCurrentUser(String userName) throws UnknownUserException
	{
		return this.auth.getUser(userName);
	}
}
