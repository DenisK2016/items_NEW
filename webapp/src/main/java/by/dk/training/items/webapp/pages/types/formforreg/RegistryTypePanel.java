package by.dk.training.items.webapp.pages.types.formforreg;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.widget.tooltip.CustomTooltipBehavior;

import by.dk.training.items.datamodel.Type;
import by.dk.training.items.services.TypeService;
import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.types.TypePage;

@AuthorizeAction(roles = { "ADMIN", "COMMANDER", "OFFICER" }, action = Action.RENDER)
public class RegistryTypePanel extends Panel {

	private static final long serialVersionUID = 1L;

	private Type type;
	@Inject
	private TypeService typeService;
	private List<Type> listType = typeService.getAll();
	private String inTypes;
	private List<String> namesType = new ArrayList<>();
	private String descName = "Введите имя типа продукта";
	private String descParType = "Здесь можно выбрать тип, к которому относится создаваемый тип.";
	private String descSubmit = "Сохранить тип";
	private String descLink = "На страницу с продуктами.";
	private List<String> listParentsName;

	public String getInTypes() {
		return inTypes;
	}

	public void setInTypes(String inTypes) {
		this.inTypes = inTypes;
	}

	public RegistryTypePanel(String id) {
		super(id);
		type = new Type();

	}

	public RegistryTypePanel(String id, Type type) {
		super(id);
		this.type = type;

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<Type> form = new Form<Type>("formregType", new CompoundPropertyModel<Type>(type));
		form.add(new FeedbackPanel("feedback"));

		TextField<String> typeName = new TextField<String>("typeName");
		typeName.setRequired(true);
		typeName.add(StringValidator.maximumLength(100));
		typeName.add(StringValidator.minimumLength(2));
		typeName.add(new CoverTooltipBehavior(descName, null));

		form.add(typeName);

		for (int i = 0; i < listType.size(); i++) {
			Type t = listType.get(i);
			String nt = t.getTypeName();
			namesType.add(i, nt);
		}

		listParentsName = new ArrayList<>();
		List<Type> listTypeCopy = new ArrayList<>(listType);
		while (!listTypeCopy.isEmpty()) {
			for (int i = 0; i < listTypeCopy.size(); i++) {
				Type t = listTypeCopy.get(i);
				Type tPar = t.getParentType();
				String nt = t.getTypeName();
				String pn = "";
				int counter = 0;
				if (tPar != null) {
					pn = tPar.getTypeName();
					if (tPar.getParentType() != null) {
						while (true) {
							tPar = tPar.getParentType();

							if (tPar != null) {
								pn = "-" + pn;
								counter++;
							}

							if (tPar == null) {
								tPar = t.getParentType();
								break;
							}
						}
					}
				}
				if (tPar != null && listParentsName.contains(pn)) {
					for (int k = 0; k < (counter + 1); k++) {
						nt = "-" + nt;
					}
					int indexP = listParentsName.indexOf(pn);
					listParentsName.add(indexP + 1, nt);
					listTypeCopy.remove(t);
				}

				if (tPar == null) {
					listParentsName.add(nt);
					listTypeCopy.remove(t);
				}
			}
		}

		if (type.getParentType() != null) {
			int i = namesType.indexOf(type.getParentType().getTypeName());
			inTypes = listParentsName.get(i);
		}

		DropDownChoice<String> dropDown = new DropDownChoice<String>("ParentTypes",
				new PropertyModel<String>(this, "inTypes"), listParentsName);
		dropDown.setNullValid(true);
		dropDown.add(new CoverTooltipBehavior(descParType, null));
		form.add(dropDown);

		form.add(new SubmitLink("saveType") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {

				if (inTypes != null) {
					String nameType = inTypes.substring(inTypes.lastIndexOf("-") + 1);
					int indexName = namesType.indexOf(nameType);
					Type parentType = listType.get(indexName);
					type.setParentType(parentType);
				} else {
					type.setParentType(null);
				}

				if (type.getId() == null) {
					type.setIdUser(AuthorizedSession.get().getUser());
					typeService.register(type);
				} else {
					typeService.update(type);
				}

				setResponsePage(new TypePage());
			}
		}.add(new CoverTooltipBehavior(descSubmit, null)));

		add(form);

		add(new Link<TypePage>("BackToTypes") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TypePage());

			}
		}.add(new CoverTooltipBehavior(descLink, null)));

	}

	private static Options newOptions() {
		Options options = new Options();
		options.set("track", true);
		options.set("hide", "{ effect: 'drop', delay: 100 }");

		return options;
	}

	class CoverTooltipBehavior extends CustomTooltipBehavior {
		private static final long serialVersionUID = 1L;

		private final String name;
		private final String url;

		public CoverTooltipBehavior(String name, String url) {
			super(newOptions());

			this.name = name;
			this.url = url;
		}

		@Override
		protected WebMarkupContainer newContent(String markupId) {
			Fragment fragment = new Fragment(markupId, "tooltip-fragment", RegistryTypePanel.this);
			fragment.add(new Label("name", Model.of(this.name)));
			// fragment.add(new ContextImage("cover", Model.of(this.url)));

			return fragment;
		}

	}

}
