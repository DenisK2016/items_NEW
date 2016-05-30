package by.dk.training.items.webapp.app;

import javax.inject.Inject;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;

import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.UserProfileService;

public class AuthorizedSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserProfileService userProfileService;

	private UserProfile user;

	private Roles roles;

	public AuthorizedSession(Request request) {
		super(request);
		Injector.get().inject(this);
	}

	public static AuthorizedSession get() {
		return (AuthorizedSession) Session.get();
	}

	@Override
	protected boolean authenticate(final String userName, final String password) {
		user = userProfileService.getByNameAndPassword(userName, password);
		return user != null;
	}

	@Override
	public Roles getRoles() {
		boolean role1 = isSignedIn() && (roles != null);
		boolean role2 = isSignedIn() && (roles == null);
		if (role1 || role2) {
			roles = new Roles();
			roles.addAll(userProfileService.resolveRoles(user.getId()));
		}
		if (!isSignedIn() && (roles == null)) {
			roles = new Roles();
			roles.add("UNKNOWN");
		}
		return roles;
	}

	@Override
	public void signOut() {
		super.signOut();
		user = null;
		roles = null;
	}

	public UserProfile getUser() {
		return user;
	}

}
