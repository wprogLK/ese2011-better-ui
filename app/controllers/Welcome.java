package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import interfaces.IAppCalendar;
import interfaces.IEvent;
import interfaces.IUser;

import java.text.ParseException;
import java.util.*;

import models.*;
import models.AppExceptions.AccessDeniedException;
import models.AppExceptions.CalendarIsNotUniqueException;
import models.AppExceptions.UnknownCalendarException;
import models.AppExceptions.UnknownUserException;
import models.AppExceptions.*;

public class Welcome extends Controller
{
    public static void index() throws UnknownUserException
    {
    	render();
    }
}