package wickettt;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.Strings;

/**
 * Base class for all example pages.
 * 
 * @author Jonathan Locke
 */
public class WicketExamplePage extends WebPage
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public WicketExamplePage()
	{
		this(new PageParameters());
	}

	/**
	 * Constructor
	 * 
	 * @param pageParameters
	 */
	public WicketExamplePage(final PageParameters pageParameters)
	{
		super(pageParameters);

		final String packageName = getClass().getPackage().getName();
		add(new WicketExampleHeader("mainNavigation", Strings.afterLast(packageName, '.'), this));
		explain();
	}


	/**
	 * Construct.
	 * 
	 * @param model
	 */
	public WicketExamplePage(IModel<?> model)
	{
		super(model);
	}

	/**
	 * Override base method to provide an explanation
	 */
	protected void explain()
	{
	}
}
