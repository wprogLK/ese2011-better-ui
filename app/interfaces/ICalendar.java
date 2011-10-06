/**
 * Calendar framework
 */
package interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import models.Event;
import models.OnlyForTesting;
import models.AppExceptions.*;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */
public interface ICalendar
{
	public String getName();

	/* Functions to get particular events from calendars */

	/** Used to get a list with public events only from the specified calendar, that are set to happen at the date {@code startDate}.
	 * @param startDate Date when events take place.
	 * @return All public events with {@code startDate} as starting point.
	 */
	public Iterator<IEvent> getAllPublicEventsStartingFrom(Date startDate);

	/** Used to get an {@link ArrayList} with public events occurring on a given {@code date}.
	 * @param date Date form which all public events should be listed.
	 * @return All public events from a specified calendar.
	 */
	public ArrayList<IEvent> getAllPublicEventsAtDate(Date date);

	//////////////////////
	// ONLY FOR TESTING //
	//////////////////////

	/** Returns a single event that start at {@code startDate}.
	 * <p>
	 * <u><b>Warning</u></b>: This method is only for testing in the {@link IUser} interface. Normally the visibility is public but not implemented in the IUser interface!
	 * </p>
	 * @param eventName Title of the event to return.
	 * @param startDate The date when the event begins.
	 * @return The desired event object for reading or messing up (editing) with.
	 * @throws UnknownEventException If the event is not in the calendar.
	 */
	@OnlyForTesting
	public Event getEvent(String eventName, Date startDate) throws UnknownEventException;
}
