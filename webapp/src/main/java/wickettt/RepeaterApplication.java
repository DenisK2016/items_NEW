package wickettt;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.response.filter.ServerAndClientTimeFilter;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.stereotype.Component;

/**
 * application class for repeater examples application
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
@Component("repeaterApplicationBean")
public class RepeaterApplication extends WebApplication
{
    private final ContactsDatabase contactsDB = new ContactsDatabase(50);


    /**
     * Constructor.
     */
    public RepeaterApplication()
    {
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init()
    {
    	 super.init();
         getMarkupSettings().setStripWicketTags(true);
         // add your configuration here
         
         ////DELETE!!!!!!!!!!!!!1
         getDebugSettings().setDevelopmentUtilitiesEnabled(true);
         getComponentInstantiationListeners().add(new SpringComponentInjector(this));
         getRequestCycleSettings().addResponseFilter(new ServerAndClientTimeFilter());
         ////DELETE!!!!!!!!!!!!!!!
    }

    /**
     * @return contacts database
     */
    public ContactsDatabase getContactsDB()
    {
        return contactsDB;
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage()
    {
        return Index.class;
    }


}
