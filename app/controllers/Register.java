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
import models.AppExceptions.*;

public class Register extends Controller
{
	
	public static IUser appUser;
	public static AppCalendar appCalendar=Bootstrap.getAppCalendar();
	
    public static void index() throws UnknownUserException
    {
    	render();
    }

    public static void createNewUser(@Required String userName, @Required String password) throws UsernameAlreadyExistException, UnknownUserException
    {
        if(validation.hasErrors())
        {
            flash.error("You have to provide an username and a password!");
            index();
        }
    	appCalendar.createUser(userName, password);
    	Application.index();
    }
}