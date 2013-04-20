package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Book extends Controller {
  
  public static Result index() {
    List<models.Book> books = models.Book.find().findList();
    return ok(books.isEmpty() ? "No books" : books.toString());
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

