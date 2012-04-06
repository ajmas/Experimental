package ajmas74.experimental.caldav;

import java.util.Date;
import java.util.List;

public interface CalendarService {

	/** RFC4791#5.2.1. */
	public String getDescription();

	/** RFC4791#5.2.2. */
	public String getTimeZone();

	/** RFC4791#5.2.4. */
	public String getContentType ();

	/** RFC4791#5.2.5. */
	public int getMaxResourceSize();

	/** RFC4791#5.2.6. */
	public Date getMinimumSupportedDate();

	/** RFC4791#5.2.7. */
	public Date getMaximumSupportedDate();

	/** RFC4791#5.2.8. */
	public int getMaximumInstances();

	/** RFC4791#5.2.9. */
	public int getMaximumAttendeesPerInstances();

    /** RFC4791#5.3.1. */
    public boolean isAbleToMakeCalendars();

    public List<String> listCalendars( String user );
    
    /** RFC4791#5.3.1. */
    public Calendar createCalendar( String user );
//
//    public Calendar lookupCalendar( User user );
}
