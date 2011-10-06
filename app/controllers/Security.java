package controllers;

import java.text.ParseException;
import jobs.Bootstrap;
import models.*;
import models.AppExceptions.*;
import models.OnlyForTesting;
import interfaces.*;

public class Security extends Secure.Security
{
	public static boolean authenticate(String username, String password)
	{
		try
		{
			Bootstrap.getAppCalendar().loginUser(username, password);
			return true;
		}
		catch (UnknownUserException e)
		{
			e.printStackTrace();
		}
		catch (AccessDeniedException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
