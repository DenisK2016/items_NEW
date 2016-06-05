package by.dk.training.items.webapp.pages.types.formforreg;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;

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
	private List<String> listParentsName;
	private ModalWindow modalWindow;

	public RegistryTypePanel(ModalWindow modalWindow) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;
		type = new Type();

	}

	public RegistryTypePanel(ModalWindow modalWindow, Type type) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;
		this.type = type;

	}

	public String getInTypes() {
		return inTypes;
	}

	public void setInTypes(String inTypes) {
		this.inTypes = inTypes;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<Type> form = new Form<Type>("formregType", new CompoundPropertyModel<Type>(type));
		form.add(new FeedbackPanel("feedback"));

		TextField<String> typeName = new TextField<String>("typeName");
		typeName.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		typeName.setRequired(true);
		typeName.add(StringValidator.maximumLength(100));
		typeName.add(StringValidator.minimumLength(2));

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
		dropDown.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		dropDown.setNullValid(true);
		form.add(dropDown);

		form.add(new AjaxLink<Void>("saveType") {

			@Override
			public void onClick(AjaxRequestTarget target) {
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
				modalWindow.close(target);
				setResponsePage(new TypePage());
			}
		});

		add(form);
		form.add(new AjaxLink<Void>("BackToTypes") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		});

		if (type.getId() == null) {
			form.add(new Label("regOrUpdate", "Регистрация нового типа"));
		} else {
			form.add(new Label("regOrUpdate", "Изменение типа"));
		}
	}

}
