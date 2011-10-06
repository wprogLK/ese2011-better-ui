/**
 * 
 */
package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lukas
 *
 */
public abstract class Helper 
{
	public static Date parseStringToDate(String str) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		return sdf.parse(str);
	}
}
