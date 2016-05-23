package wickettt;

import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Navigation panel for the examples project.
 * 
 * @author Eelco Hillenius
 */
public final class WicketExampleHeader extends Panel
{
	/**
	 * Construct.
	 * 
	 * @param id
	 *            id of the component
	 * @param exampleTitle
	 *            title of the example
	 * @param page
	 *            The example page
	 */
	public WicketExampleHeader(String id, String exampleTitle, WebPage page)
	{
		super(id);

		add(new DebugBar("debug"));
		add(new Label("exampleTitle", exampleTitle));
		BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("sources",
			SourcesPage.class, SourcesPage.generatePageParameters(page));
		add(link);

		PopupSettings settings = new PopupSettings("sources", PopupSettings.RESIZABLE);
		settings.setWidth(800);
		settings.setHeight(600);
		link.setPopupSettings(settings);
	}
}
