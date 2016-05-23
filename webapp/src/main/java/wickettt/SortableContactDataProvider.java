package wickettt;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;


/**
 * implementation of IDataProvider for contacts that keeps track of sort information
 * 
 * @author igor
 * 
 */
public class SortableContactDataProvider extends SortableDataProvider<Contact, String> implements IFilterStateLocator<ContactFilter>
{
    private ContactFilter contactFilter = new ContactFilter();

    /**
     * constructor
     */
    public SortableContactDataProvider()
    {
        // set default sort
        setSort("firstName", SortOrder.DESCENDING);
    }

    protected ContactsDatabase getContactsDB()
    {
        return DatabaseLocator.getDatabase();
    }

    @Override
    public Iterator<Contact> iterator(long first, long count)
    {
        List<Contact> contactsFound = getContactsDB().getIndex(getSort());
        
        return filterContacts(contactsFound).
            subList((int)first, (int)(first + count)).
            iterator();
    }

    private List<Contact> filterContacts(List<Contact> contactsFound)
    {
        ArrayList<Contact> result = new ArrayList<>();
        Date dateFrom = contactFilter.getDateFrom();
        Date dateTo = contactFilter.getDateTo();
        
        for (Contact contact : contactsFound)
        {
        Date bornDate = contact.getBornDate();
        
        if(dateFrom != null && bornDate.before(dateFrom))
        {
            continue;
        }
        
        if(dateTo != null && bornDate.after(dateTo))
        {
            continue;
        }
        
        result.add(contact);
        }
        
        return result;
    }

    /**
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
     */
    @Override
    public long size()
    {
        return filterContacts(getContactsDB().getIndex(getSort())).size();
    }

    /**
     * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
     */
    @Override
    public IModel<Contact> model(Contact object)
    {
        return new DetachableContactModel(object);
    }

    @Override
    public ContactFilter getFilterState()
    {
        return contactFilter;
    }

    @Override
    public void setFilterState(ContactFilter state)
    {
        contactFilter  = state;
    }

    
}
