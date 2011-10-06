/**
 * Calendar framework
 */
package interfaces;

import java.util.Date;

/**
 * @author Lukas Keller
 * @author Renato Corti
 *
 */
public interface IEvent
{
	public String getEventName();

	public Date getStartDate();

	public Date getEndDate();

	public boolean isPrivate();

	public boolean isPublic();
}
