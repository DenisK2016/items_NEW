package wicketExample;

import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

public class Exampl01 extends BasePage {
 
	// свойства класса
	  private static int counter = 0; // счетчик
	  private Label linkLabel; // надпись
	  private ArrayList<String> messages; // список строк
	  private TextField inputText;
	  private String inText = "";  // переменная для хранения текста, вводимого в поле ввода
	  private String outText = ""; // переменная для хранения текста, отображаемого в параграфе
	 
	  /**
	   * Get the value of inText
	   *
	   * @return the value of inText
	   */
	  public String getInText() {
	    return inText;
	  }
	 
	  /**
	   * Set the value of inText
	   *
	   * @param inText new value of inText
	   */
	  public void setInText(String inText) {
	    this.inText = inText;
	  }
	  
	// Конструктор класса
	    public Exampl01() {
	    	// если список еще не существует, то создаем его
	        if (messages == null)
	          messages = new ArrayList(); 
	    // Создаем надпись (заголовок страницы)
	        add(new Label("message", "Пример 1"));
	    // Добавляем ссылку
	        add(new AjaxFallbackLink("link") {
	          // переопределяем метод onClick ссылки для обработки перехода по ней
	          @Override
	          public void onClick(AjaxRequestTarget target) {
	            // увеличиваем счетчик на единицу
	            counter++;
	            if (target != null) {
	              // обновляем компонент на странице, если страница существует
	              target.add(linkLabel);
	            }
	          }
	        });
	        // Создаем компонент надписи
	        linkLabel = new Label("linkLabel", new PropertyModel(this, "counter"));
	        // Устанавливаем идентификатор для надписи (нужно для Ajax)
	        linkLabel.setOutputMarkupId(true);
	        // добавляем компонент на страницу
	        add(linkLabel);
	        
	        // создаем форму
	        Form echoForm = new Form("echoForm");
	        // создаем поле для ввода текста
	        inputText = new TextField("inputText", new PropertyModel(this, "inText"));
	        // добавляем поле в форму
	        echoForm.add(inputText);
	        // Создаем кнопку и добавляем в форму
	        echoForm.add(new Button("sendButton") {
	          @Override
	          // переопределяем метод onSubmit кнопки
	          public void onSubmit() {
	        	// добавляем строку в список
	        	  messages.add(getInText());
	            // очищаем поле ввода
	            setInText("");
	          }
	        });	       
	        // добавляем на страницу форму
	        add(echoForm);
	     // Добавляем список (строки в таблицу)
	        add(new ListView("messages", messages) {
	          @Override
	          // этот метод выполняется для каждого элемента списка
	          protected void populateItem(ListItem item) {
	            // получаем объект списка
	            String text = (String)item.getModelObject();
	            // Добавляем текст в столбец таблицы
	            item.add(new Label("post", text));
	            // Добавляем ссылку
	            item.add(new Link("deleteLink", item.getModel()) {
	              // переопределяем метод для обработки перехода по ссылке
	              @Override
				public void onClick() {
	                // получаем строку (объект) с которой связана ссылка
	                String selected = (String)getModelObject();
	                // определяем индекс объекта в списке
	                int pos = messages.indexOf(selected);
	                // удаляем из списка объект (строку) если он там присутствует
	                if (pos >= 0) messages.remove(pos);
	              }
	            });
	          }
	        });
	    }
 
}
