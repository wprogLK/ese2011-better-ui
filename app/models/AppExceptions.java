/**
 * Calendar framework
 */
package models;

import java.util.Date;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */

public class AppExceptions
{

	public static class UnknownUserException extends Exception
	{
		/**
		 * If {@code username} is not in the database.
		 */
		public UnknownUserException(String username)
		{
			super(String.format("The user \"%s\" does not exist!", username));
		}
	}

	public static class UsernameAlreadyExistException extends Exception
	{
		/**
		 * If {@code username} is already in the database.
		 */
		public UsernameAlreadyExistException(String username)
		{
			super(String.format("The user \"%s\" already exists! Please choose another name!", username));
		}
	}

	public static class AccessDeniedException extends Exception
	{
		/**
		 * When passwords do not match up.
		 */
		public AccessDeniedException(String username)
		{
			super(String.format("You provided user \"%s\" with an invalid password!", username));
		}
	}

	public static class UnknownCalendarException extends Exception
	{
		/**
		 * If the {@code user} has no calendar with such a name.
		 */
		public UnknownCalendarException(String calendarName)
		{
			super(String.format("The calendar \"%s\" does not exist!", calendarName));
		}
	}

	public static class CalendarIsNotUniqueException extends Exception
	{
		/**
		 * If a {@code calendar} with the same title already exists in the calendar list of the {@code user}.
		 */
		public CalendarIsNotUniqueException(Calendar calendar)
		{
			super(String.format("User \"%s\" already has a calendar named \"%s\" in the calendar list!", calendar.getOwner().getName(), calendar.getName()));
		}
	}

	public static class UnknownEventException extends Exception
	{
		/**
		 * If the event is not in the calendar.
		 */
		public UnknownEventException(String eventName, Date startDate)
		{
			super(String.format("The event \"%s\" with the start date \"%tc\" does not exist!", eventName, startDate));
		}
	}

	public static class InvalidDateException extends Exception
	{
		/**
		 * If {@code endDate} is placed before {@code startDate}.
		 */
		public InvalidDateException(Date startDate, Date endDate)
		{
			super(String.format("The start date \"%tc\" cannot begin after end date \"%tc\"!", startDate, endDate));
		}
	}
}
