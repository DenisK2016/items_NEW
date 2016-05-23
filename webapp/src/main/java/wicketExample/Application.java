package wicketExample;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.stereotype.Component;
/** 
 *
 * @author imp
 * @version 
 */
 
@Component("applicationBeaaan")
public class Application extends WebApplication {
 
    public Application() {
    }
 
    @Override
	public Class getHomePage() {
        return Home1Page.class;
    }
 
}
