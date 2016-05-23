package wickettt;

import org.apache.wicket.Application;

/**
 * service locator class for contacts database
 * 
 * @author igor
 * 
 */
public class DatabaseLocator
{
    /**
     * @return contacts database
     */
    public static ContactsDatabase getDatabase()
    {
        RepeaterApplication app = (RepeaterApplication)Application.get();
        return app.getContactsDB();
    }
}
