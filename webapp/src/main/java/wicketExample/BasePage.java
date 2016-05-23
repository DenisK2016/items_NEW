package wicketExample;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/** 
 *
 * @author imp
 * @version 
 */
 
public abstract class BasePage extends WebPage {
 
    public BasePage() { 
        super(); 
        add(new HeaderPanel("headerpanel", "Примеры использования фреймворка wicket")); 
        BookmarkablePageLink homeLink = new BookmarkablePageLink("homeLink", Home1Page.class);
        homeLink.setAutoEnable(true);
        BookmarkablePageLink ex01Link = new BookmarkablePageLink("exampl01Link", Exampl01.class);
        ex01Link.setAutoEnable(true);
        add(homeLink);
        add(ex01Link);
        add(new FooterPanel("footerpanel", "следите за публикацией статей на сайте www.pgtk.edu.ru/gooamoko/"));
    } 
 
}