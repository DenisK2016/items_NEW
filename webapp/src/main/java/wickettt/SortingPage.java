package wickettt;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class SortingPage extends BasePage
{
    private static final long serialVersionUID = 1L;

    /**
     * constructor
     */
    public SortingPage()
    {
        SortableContactDataProvider dp = new SortableContactDataProvider();
        final DataView<Contact> dataView = new DataView<Contact>("sorting", dp)
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final Item<Contact> item)
            {
                Contact contact = item.getModelObject();
//                item.add(new ActionPanel("actions", item.getModel()));
                item.add(new Label("contactid", String.valueOf(contact.getId())));
                item.add(new Label("firstname", contact.getFirstName()));
                item.add(new Label("lastname", contact.getLastName()));
                item.add(new Label("homephone", contact.getHomePhone()));
                item.add(new Label("cellphone", contact.getCellPhone()));

                item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>()
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public String getObject()
                    {
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));
            }
        };

        dataView.setItemsPerPage(8L);

        add(new OrderByBorder("orderByFirstName", "firstName", dp)
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        });

        add(new OrderByBorder("orderByLastName", "lastName", dp)
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        });

        int i = 0;
        i++;
        System.out.println(i);
        add(dataView);

        add(new PagingNavigator("navigator", dataView));
    }
}
