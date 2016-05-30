package by.dk.training.items.webapp.app;

import javax.inject.Inject;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import by.dk.training.items.webapp.pages.home.HomePage;
import by.dk.training.items.webapp.pages.login.LoginPage;

@Component("wicketWebApplicationBean")
public class WicketApplication extends AuthenticatedWebApplication {

	@Inject
	private ApplicationContext applicationContext;

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {
		super.init();
		getMarkupSettings().setStripWicketTags(true);
		getComponentInstantiationListeners().add(new SpringComponentInjector(this, getApplicationContext()));
		getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
		// mountPage("/products", ProductPage.class);
		// add your configuration here

		// //// DELETE!!!!!!!!!!!!!1
		// getDebugSettings().setDevelopmentUtilitiesEnabled(true);
		//
		// getRequestCycleSettings().addResponseFilter(new
		// ServerAndClientTimeFilter());
		// //// DELETE!!!!!!!!!!!!!!!
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return AuthorizedSession.class;
	}

}