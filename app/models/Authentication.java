/**
 * Calendar framework
 */
package models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import play.mvc.With;
import controllers.Secure;

import models.AppExceptions.*;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */

/** Handles the user database with the corresponding passwords and functions to alter or
 * access the {@link User} objects used for read-write access to calendars and events.
 */
public final class Authentication
{
	private static ArrayList<Tuple> userDatabase;

	public Authentication()
	{
		this.userDatabase = new ArrayList<Tuple>();
	}

	private class Tuple
	{
		private User user;
		private String password;
		public Tuple(User user, String password)
		{
			this.user = user;
			this.password = password;
		}
		public String getUsername()
		{
			return this.user.getName();
		}
		public User getUser()
		{
			return this.user;
		}
		public String getPassword()
		{
			return this.password;
		}
		public void setPassword(String newPassword)
		{
			this.password = newPassword;
		}

		public String toString()
		{
			return this.user.getName();
		}
	}

	public User createNewUser(String username, String password) throws UsernameAlreadyExistException
	{
		isUsernameUnused(username);

		User newUser = new User(username);
		newUser.save();
		this.userDatabase.add(new Tuple(newUser, password));
		return newUser;
	}

	private void isUsernameUnused(String username) throws UsernameAlreadyExistException
	{
		for (Tuple t : userDatabase)
		{
			if (t.getUsername().equals(username))
			{
				throw new UsernameAlreadyExistException(username);
			}
		}
	}

	/** Used to get the User object which is required to work with calendar objects (add, modify, delete events).
	 * This are actions, that should only be performed by the owner of the calendar.
	 * It is intended that the {@link User} reaches the external user interface
	 * that the user has access to its methods (add, modify, delete events, calendars).
	 * So this function requires a password.
	 * For access to read only functions and public events
	 * there is the {@link Authentication#getUser(String)} function available, which does not ask for a password.
	 * but must not reach the outer interface.
	 * @param username The user object that is to be passed.
	 * @param password The secret to verify access.
	 * @return Provides the {@link User}.
	 * @throws UnknownUserException If {@code username} is not in the database.
	 * @throws AccessDeniedException When passwords do not match up.
	 */
	public User getUser(String username, String password) throws UnknownUserException, AccessDeniedException
	{
		Iterator<Tuple> iteratorUsers = this.userDatabase.iterator();
		while (iteratorUsers.hasNext())
		{
			Tuple t = iteratorUsers.next();
			if (t.getUsername().equals(username))
			{
				if (t.getPassword().equals(password))
				{
					return t.getUser();
				}
				else
				{
					throw new AccessDeniedException(username);
				}
			}
		}
		throw new UnknownUserException(username);
	}

	/** This is a more permissive function to get the {@link User}. It does not ask for a password.
	 * In contrast to {@link Authentication#getUser(String, String)},
	 * functions using this routine must not pass the user object to the outer interface
	 * since it provides write access to calendars (add, modify, delete events) as well.
	 * @param username {@link User} from which calendars titles or public events are needed.
	 * @return User object used to gain access to all {@link Calendar} objects.
	 * @throws UnknownUserException If {@code username} is not in the database.
	 */
	public User getUser(String username) throws UnknownUserException
	{
		Iterator<Tuple> iteratorUsers = this.userDatabase.iterator();
		while (iteratorUsers.hasNext())
		{
			Tuple t = iteratorUsers.next();
			if (t.getUsername().equals(username))
			{
				return t.getUser();
			}
		}
		throw new UnknownUserException(username);
	}

	public void setNewPassword(String username, String oldPassword, String newPassword) throws UnknownUserException, AccessDeniedException
	{
		this.getUser(username, oldPassword);
		for (Tuple t : this.userDatabase)
		{
			if (t.getUsername().equals(username))
			{
				t.setPassword(newPassword);
				break;
			}
		}
	}

	public void deleteUser(String username, String password) throws UnknownUserException, AccessDeniedException
	{
		this.getUser(username, password);
		for (Tuple t : this.userDatabase)
		{
			if (t.getUsername().equals(username))
			{
				this.userDatabase.remove(t);
				break;
			}
		}
	}

	public ArrayList<String> getAllUserNames()
	{
		ArrayList<String> userNames = new ArrayList<String>();

		for(Tuple t:this.userDatabase)
		{
			String userName = t.getUsername();
			userNames.add(userName);
		}
		return userNames;
	}
}
