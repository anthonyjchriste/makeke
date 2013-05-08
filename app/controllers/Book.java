package controllers;

import static play.data.Form.form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.User;
import views.html.bookexplorer;
import views.html.error;

public class Book extends Controller {
  
  public static Result index() {
    if(User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.", "Please login or create an account."));
    }
   
    List<models.Book> books = models.Book.find().findList();
    return ok(bookexplorer.render(books));
  }
  
  public static Result filter() {
    Set<models.Book> bookSet = new HashSet<>();
    DynamicForm searchForm = form().bindFromRequest();
    
    for(String nameWord : searchForm.get("name").split(" ")) {
      bookSet.addAll(models.Book.find().where().contains("name", nameWord).findSet());
    }    
    
    return ok(bookexplorer.render(new ArrayList<>(bookSet)));
  }
  
  public static Result details(String bookId) {
    models.Book book = models.Book.find().where().eq("bookId", bookId).findUnique();
    return (book == null) ? notFound("No book found") : ok(book.toString());
  }
  
  public static Result newBook() {
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();
    
    if(bookForm.hasErrors()) {
      return badRequest("Not all requirements met");
    }
    
    models.Book book = bookForm.get();
    book.save();
    return ok(book.toString());
  }
  
  public static Result delete(String bookId) {
    models.Book book = models.Book.find().where().eq("bookId", bookId).findUnique();
    
    if(book != null) {
      book.delete();
    }
    
    return ok();
  }
}

