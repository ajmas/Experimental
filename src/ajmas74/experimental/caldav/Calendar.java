package ajmas74.experimental.caldav;

public interface Calendar {

    /** RFC4791#5.3.1. */
    public String getDisplayName();

    /** RFC4791#5.3.2. */
	public void createEntry ( String data );
}
