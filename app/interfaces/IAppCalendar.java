/**
 * Calendar framework
 */
package interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import models.Authentication;
import models.AppExceptions.*;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */
public interface IAppCalendar
{
		/* Functions for the user management */

		/** Adds a new user to our database and play! save the user
		 * @param username The name the new user should have.
		 * @param password A string consisting of a random set of characters kept secret to make the access unique to the user.
		 * No quality checking is done at this point.
		 * @throws UsernameAlreadyExistException If {@code username} is already in the database.
		 * @throws UnknownUserException 
		 */
		public void createUser(String username, String password) throws UsernameAlreadyExistException, UnknownUserException;

		/** Lets the user change the password and updates the user database.
		 * @param username Name of the user whose password should be changed.
		 * @param oldPassword Password to verify access.
		 * @param newPassword Password used to grant access in the near future.
		 * @throws UnknownUserException If {@code username} is not in the database.
		 * @throws AccessDeniedException When passwords do not match up.
		 */
		public void changePassword(String username, String oldPassword, String newPassword) throws UnknownUserException, AccessDeniedException;

		/** Removes the user from the database. Calendars this user created will be deleted as well.
		 * @param username Name of the user to delete.
		 * @param password Confirmation for the deletion.
		 * @throws UnknownUserException If {@code username} is not in the database.
		 * @throws AccessDeniedException When passwords do not match up.
		 */
		public void deleteUser(String username, String password) throws UnknownUserException, AccessDeniedException;

		/* Getters for calendars & events for users that are not owners of the calendars */

		/** Provides all calendar names as an {@link ArrayList} of strings of a specific users.
		 * The {@link IUser} used here must not leave this function as it does not ask for a password for it.
		 * @param username Owner of the calendars requested
		 * @return An {@link ArrayList} with all calendar names of the user as represented as strings.
		 * @throws UnknownUserException If {@code username} is not in the database.
		 * @see IAppCalendar#loginUser(String, String)
		 */
		public ArrayList<String> getAllCalendarsNamesFromUser(String username) throws UnknownUserException;

		/**
		 * Returns all public events from a {@link ICalendar} that occur at a given {@code date} as an {@link ArrayList}.
		 * The {@link IUser} used here must not leave this function as it does not ask for a password for it.
		 * @param username Owner of the calendar.
		 * @param calendarName Title of a calendar to identify it.
		 * @param date Date from which to list all public events.
		 * @return An {@link ArrayList} with all public events from the calendar.
		 * @throws UnknownUserException If {@code username} is not in the database.
		 * @throws UnknownCalendarException If the {@code user} has no calendar with such a name.
		 * @throws AccessDeniedException When passwords do not match up.
		 * @see IAppCalendar#loginUser(String, String)
		 */
		public ArrayList<IEvent> getUsersCalendarPublicEventsAtDate(String username, String calendarName, Date date) throws UnknownUserException, UnknownCalendarException, AccessDeniedException;

		/**
		 * Returns all public events from a {@link ICalendar} that start at the given date {@code startDate} as an {@link Iterator}.
		 * The {@link IUser} used here must not leave this function as it does not ask for a password for it.
		 * @param username Owner of the calendar.
		 * @param calendarName Title of a calendar to identify it.
		 * @param startDate Public events that start at {@code startDate} are returned.
		 * @return An {@link Iterator} with all public events that start at {@code startDate}.
		 * @throws UnknownUserException If {@code username} is not in the database.
		 * @throws UnknownCalendarException If the {@code user} has no calendar with such a name.
		 * @throws AccessDeniedException When passwords do not match up.
		 */
		public Iterator<IEvent> getUsersCalendarPublicEvents(String username, String calendarName, Date startDate) throws UnknownUserException, UnknownCalendarException, AccessDeniedException;

		/**
		 * @param username {@link IUser} from which the object should be returned.
		 * @param password Secret string of randomly composed characters chosen at creation time.
		 * @return Returns the {@link IUser} provided that the user exists and the corresponding
		 * password for the given user is correct.
		 * Not to be confused with the {@link Authentication#getUser(String, String)} function
		 * which is used for internal processing.
		 * @throws UnknownUserException If {@code username} is not in the database.
		 * @throws AccessDeniedException When passwords do not match up.
		 */
		public IUser loginUser(String username, String password) throws UnknownUserException, AccessDeniedException;

		public ArrayList<String> getAllUserNames();
	}

