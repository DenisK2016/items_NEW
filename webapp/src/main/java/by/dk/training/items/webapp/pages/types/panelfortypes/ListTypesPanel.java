package by.dk.training.items.webapp.pages.types.panelfortypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.tree.DefaultNestedTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.tree.content.Folder;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.widget.menu.ContextMenu;
import com.googlecode.wicket.jquery.ui.widget.menu.ContextMenuBehavior;
import com.googlecode.wicket.jquery.ui.widget.menu.IMenuItem;
import com.googlecode.wicket.jquery.ui.widget.menu.MenuItem;

import by.dk.training.items.dataaccess.filters.TypeFilter;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.services.TypeService;

@AuthorizeAction(roles = { "ADMIN", "COMMANDER", "OFFICER" }, action = Action.RENDER)
public class ListTypesPanel extends Panel {

	private static final long serialVersionUID = 1L;
	@Inject
	private TypeService typeService;
	private ModalWindow modal1;
	private ContextMenu menu;

	public ListTypesPanel(String id) {
		super(id);

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		Form form = new Form<>("form");
		FeedbackPanel feedBack = new JQueryFeedbackPanel("feedback");
		form.add(feedBack.setOutputMarkupId(true));

		menu = new ContextMenu("contextMenu", newMenuItemList()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onContextMenu(AjaxRequestTarget target, Component component) {

			}

			@Override
			public void onClick(AjaxRequestTarget target, IMenuItem item) {
				target.add(this);
				target.add(feedBack);
			}
		};
		menu.setOutputMarkupId(true);
		add(menu);

		form.add(new DefaultNestedTree<Type>("treeTypes", new TreeTypeProvider()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected Component newContentComponent(String id, IModel<Type> node) {

				Folder fold = new Folder<Type>(id, this, node) {
					@Override
					protected IModel<?> newLabelModel(IModel<Type> model) {
						return new PropertyModel<String>(model, "typeName");
					}

					@Override
					protected boolean isClickable() {
						return true;
					}
				};
				fold.add(new ContextMenuBehavior(menu));
				return fold;

			}
		});
		add(form);

	}

	static List<IMenuItem> newMenuItemList() {
		List<IMenuItem> list = new ArrayList<IMenuItem>();

		list.add(new MenuItem("Информация о типе", JQueryIcon.FLAG));
		list.add(new MenuItem("Продукты типа", JQueryIcon.BOOKMARK));

		return list;
	}

	private class TreeTypeProvider implements ITreeProvider<Type> {

		private static final long serialVersionUID = 1L;
		private TypeFilter typeFilter;

		public TreeTypeProvider() {
			typeFilter = new TypeFilter();
			typeFilter.setFetchParentType(true);
			typeFilter.setFetchUser(true);
			typeFilter.setFetchChildTypes(true);
		}

		@Override
		public void detach() {

		}

		@Override
		public Iterator<Type> getRoots() {
			typeFilter.setId(null);
			List<Type> listT = new ArrayList<Type>(); // Типы у которых нет
														// парентов
			for (Type t : typeService.find(typeFilter)) {
				if (t.getParentType() == null) {
					listT.add(t);
				}
			}
			return listT.iterator();
		}

		@Override
		public boolean hasChildren(Type type) {
			typeFilter.setId(type.getId());
			return type.getChildTypes() == null || !typeService.find(typeFilter).get(0).getChildTypes().isEmpty();
		}

		@Override
		public Iterator<Type> getChildren(Type type) {
			return type.getChildTypes().iterator();
		}

		@Override
		public IModel<Type> model(Type object) {
			return new Model<Type>(object);
		}

	}

}
