package by.dk.training.items.webapp.pages.packages.setting;

import java.math.BigDecimal;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;

public class SystemSettings extends Panel {

	private static final long serialVersionUID = 1L;
	private ModalWindow modalWindow;
	private static BigDecimal maxPrice = new BigDecimal("600000");
	private static Double maxWeight = 25.0;
	private static BigDecimal priceWeight = new BigDecimal("100000");
	private static BigDecimal percent = new BigDecimal("20");
	private static String paymentDeadline = "10";
	private static BigDecimal percentFine = new BigDecimal("1");

	public SystemSettings(ModalWindow modalWindow) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;

	}

	public static String getPaymentDeadline() {
		return paymentDeadline;
	}

	public static void setPaymentDeadline(String paymentDeadline) {
		SystemSettings.paymentDeadline = paymentDeadline;
	}

	public static BigDecimal getPercent() {
		return percent;
	}

	public static void setPercent(BigDecimal percent) {
		SystemSettings.percent = percent;
	}

	public static BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public static void setMaxPrice(BigDecimal maxPrice) {
		SystemSettings.maxPrice = maxPrice;
	}

	public static BigDecimal getPriceWeight() {
		return priceWeight;
	}

	public static void setPriceWeight(BigDecimal priceWeight) {
		SystemSettings.priceWeight = priceWeight;
	}

	public static Double getMaxWeight() {
		return maxWeight;
	}

	public static void setMaxWeight(Double maxWeight) {
		SystemSettings.maxWeight = maxWeight;
	}

	public static BigDecimal getPercentFine() {
		return percentFine;
	}

	public static void setPercentFine(BigDecimal percentFine) {
		SystemSettings.percentFine = percentFine;
	}

	@Override
	protected void onInitialize() {
		Form formSetting = new Form<>("formSetting");
		FeedbackPanel feedBack = new FeedbackPanel("feedback");
		formSetting.add(feedBack);
		add(formSetting);
		TextField<BigDecimal> priceField = new TextField<>("maxPrice", new PropertyModel<>(this, "maxPrice"));
		priceField.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
			}
		});
		formSetting.add(priceField);
		TextField<BigDecimal> weightField = new TextField<>("maxWeight", new PropertyModel<>(this, "maxWeight"));
		weightField.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
			}
		});
		formSetting.add(weightField);
		TextField<BigDecimal> priceWeightField = new TextField<>("priceWeight",
				new PropertyModel<>(this, "priceWeight"));
		priceWeightField.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
			}
		});
		formSetting.add(priceWeightField);
		TextField<BigDecimal> percentField = new TextField<>("percent", new PropertyModel<>(this, "percent"));
		percentField.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
			}
		});
		formSetting.add(percentField);
		TextField<String> days = new TextField<>("paymentDeadline", new PropertyModel<>(this, "paymentDeadline"));
		days.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
			}
		});
		formSetting.add(days);
		TextField<BigDecimal> fineField = new TextField<>("percentFine", new PropertyModel<>(this, "percentFine"));
		fineField.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
			}
		});
		formSetting.add(fineField);
		AjaxSubmitLink linkSave = new AjaxSubmitLink("save") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				setMaxPrice(maxPrice);
				setPercent(percent);
				setPercentFine(percentFine);
				setPriceWeight(priceWeight);
				setPaymentDeadline(paymentDeadline);
				modalWindow.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedBack);
			}
		};
		linkSave.add(AttributeModifier.append("title", "Сохранить"));
		formSetting.add(linkSave);
		formSetting.add(new AjaxLink<Void>("back") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		});
		super.onInitialize();
	}

}
