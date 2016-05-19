package by.dk.training.items.webapp.app;

import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.response.filter.ServerAndClientTimeFilter;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import by.dk.training.items.webapp.pages.home.HomePage;

@Component("wicketWebApplicationBean")
public class WicketApplication extends WebApplication {
	
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
//        mountPage("/productDetails", ProductDetailsPage.class);
        // add your configuration here
        
        ////DELETE!!!!!!!!!!!!!1
        getDebugSettings().setDevelopmentUtilitiesEnabled(true);
        
        getRequestCycleSettings().addResponseFilter(new ServerAndClientTimeFilter());
        ////DELETE!!!!!!!!!!!!!!!
    }  
    
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    

}