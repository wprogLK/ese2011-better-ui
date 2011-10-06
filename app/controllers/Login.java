package controllers;

import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Login  extends Controller
{
	public static void main()
	{
		render();
	}
}
