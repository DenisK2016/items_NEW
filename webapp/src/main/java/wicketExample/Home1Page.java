package wicketExample;

import org.apache.wicket.markup.html.basic.Label;

public class Home1Page extends BasePage {
 
    public Home1Page() {
        add(new Label("message", "Примеры wicket"));
    }
 
}