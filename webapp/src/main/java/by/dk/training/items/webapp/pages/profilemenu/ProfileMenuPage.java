package by.dk.training.items.webapp.pages.profilemenu;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.webapp.pages.AbstractPage;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class ProfileMenuPage extends AbstractPage {

	private static final long serialVersionUID = 1L;
	private UserProfile userProfile;

	public ProfileMenuPage(UserProfile userProfile) {
		super();
		this.userProfile = userProfile;

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new ProfileMenuPanel("profileMenu", userProfile));
	}
}
