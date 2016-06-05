package by.dk.training.items.webapp.pages.home;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import by.dk.training.items.datamodel.Package;
import by.dk.training.items.services.PackageService;
import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.AbstractPage;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class HomePage extends AbstractPage {

	@Inject
	private PackageService packageService;
	private Date dateStart;
	private Date dateEnd;
	private boolean admin = AuthorizedSession.get().getRoles().contains("ADMIN");
	private boolean officer = AuthorizedSession.get().getRoles().contains("OFFICER");
	private boolean commander = AuthorizedSession.get().getRoles().contains("COMMANDER");

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public HomePage() {
		super();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Label allPacks = new Label("allpacks", Model.of(packageService.getAll().size()));
		add(allPacks);
		Date d1 = new Date();
		Date d2 = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d2);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		d2.setTime(cal.getTime().getTime());
		int i = cal.get(Calendar.MONTH) - 1;
		if (i == -1) {
			i = 11;
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
		}
		cal.set(Calendar.MONTH, i);
		d1.setTime(cal.getTime().getTime());
		Label lastMonth = new Label("month", Model.of(packageService.betweenDates(d1, d2).size()));
		add(lastMonth);
		dateStart = new Date();
		dateEnd = new Date();
		Model mod = new Model(packageService.betweenDates(dateStart, dateEnd).size());
		Label lab = new Label("quantity", Model.of(mod));
		lab.setOutputMarkupId(true);
		add(lab);
		DateTextField fieldStart = new DateTextField("datestart", new PropertyModel<>(this, "dateStart"), "dd-MM-yyyy");
		fieldStart.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				mod.setObject(packageService.betweenDates(dateStart, dateEnd).size());
				target.add(lab);

			}
		});
		add(fieldStart);
		fieldStart.add(new DatePicker());
		DateTextField fieldEnd = new DateTextField("dateend", new PropertyModel<>(this, "dateEnd"), "dd-MM-yyyy");
		fieldEnd.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {

				mod.setObject(packageService.betweenDates(dateStart, dateEnd).size());
				target.add(lab);
			}
		});
		add(fieldEnd);
		fieldEnd.add(new DatePicker());
		Label maxPrice = new Label("maxprice", Model.of(packageService.maxPrice().getPrice()));
		add(maxPrice);
		List<Package> packs = packageService.getAll();
		Map<String, Integer> map = new HashMap<>();
		for (Package p : packs) {
			if (map.get(p.getCountrySender()) == null) {
				map.put(p.getCountrySender(), 1);
			} else {
				map.put(p.getCountrySender(), (map.get(p.getCountrySender())) + 1);
			}

		}
		int maxCity = 0;
		String city = "";
		for (Map.Entry<String, Integer> entry : map.entrySet()) {

			if (maxCity < entry.getValue()) {
				maxCity = entry.getValue();
				city = entry.getKey();
			}
		}
		Label popularCity = new Label("city", Model.of(city));
		add(popularCity);
		
		Label role;
		if(admin){
			role = new Label("role", "администратор");
		} else if(commander){
			role = new Label("role", "командир");
		} else {
			role = new Label("role", "служащий");
		}
		add(role);
		Label space = new Label("space", " ");
		space.setVisible(false);
		add(space);

		if (admin) {
			allPacks.setVisible(false);
			lastMonth.setVisible(false);
			lab.setVisible(false);
			fieldStart.setVisible(false);
			fieldEnd.setVisible(false);
			maxPrice.setVisible(false);
			popularCity.setVisible(false);
			space.setVisible(true);
		}
	}

}
