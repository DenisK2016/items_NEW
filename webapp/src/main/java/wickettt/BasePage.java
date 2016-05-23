package wickettt;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Base page for component demo pages.
 * 
 * @author igor
 */
public class BasePage extends ExamplePage
{
    private Contact selected;

    /**
     * Constructor
     */
    public BasePage()
    {
        add(new Label("selectedLabel", new PropertyModel<>(this, "selectedContactLabel")));
        add(new FeedbackPanel("feedback"));
    }

    /**
     * @return string representation of selected contact property
     */
    public String getSelectedContactLabel()
    {
        if (selected == null)
        {
            return "No Contact Selected";
        }
        else
        {
            return selected.getFirstName() + " " + selected.getLastName();
        }
    }

    /**
     * 
     */
    class ActionPanel extends Panel
    {
        /**
         * @param id
         *            component id
         * @param model
         *            model for contact
         */
        public ActionPanel(String id, IModel<Contact> model)
        {
            super(id, model);
            add(new Link("select")
            {
                @Override
                public void onClick()
                {
                    selected = (Contact)getParent().getDefaultModelObject();
                }
            });
        }
    }

    /**
     * @return selected contact
     */
    public Contact getSelected()
    {
        return selected;
    }

    /**
     * sets selected contact
     * 
     * @param selected
     */
    public void setSelected(Contact selected)
    {
        addStateChange();
        this.selected = selected;
    }
}
