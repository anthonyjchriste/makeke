package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.requestCreate;
import views.html.requestEdit;
import views.html.error;

public class Request extends Controller {

  public static Result index() {
    List<models.Request> requests = models.Request.find().findList();
    return ok(requests.isEmpty() ? "No requests" : requests.toString());
  }

  public static Result details(String requestId) {
    models.Request request = models.Request.find().where().eq("requestId", requestId).findUnique();
    return (request == null) ? notFound("No request found") : ok(request.toString());
  }

  public static Result create() {
    Form<models.Request> requestForm = form(models.Request.class);
    List<models.Book> books = models.Book.find().all();
    return ok(requestCreate.render(requestForm, books));
  }

  public static Result save() {
    Form<models.Request> requestForm = form(models.Request.class).bindFromRequest();
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();

    if (requestForm.hasErrors()) {
      return ok(error.render("Error with request fields", requestForm.errors().toString()));
    }
    
    if(bookForm.hasErrors()) {
      return ok(error.render("Error with book fields", bookForm.errors().toString()));
    }

    models.Request request = requestForm.get();
    models.Book book = bookForm.get();
    request.getStudent().getRequests().add(request);
    request.getStudent().save();
    
    request.setBook(book);
    book.setRequest(request);
    
    request.save();
    book.save();
    
    return redirect(routes.RequestsAndOffers.index());
  }
  
  public static Result edit(Long primaryKey) {
    models.Request request = models.Request.find().byId(primaryKey);
    Form<models.Request> requestForm = form(models.Request.class).fill(request);
    Form<models.Book> bookForm = form(models.Book.class).fill(request.getBook());
    return ok(requestEdit.render(primaryKey, requestForm, bookForm));
  }
  
  public static Result update(Long primaryKey) {
    Form<models.Request> requestForm = form(models.Request.class).bindFromRequest();
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();
    
    if (requestForm.hasErrors()) {
      return ok(error.render("Error with request fields", requestForm.errors().toString()));
    }
    
    if(bookForm.hasErrors()) {
      return ok(error.render("Error with book fields", bookForm.errors().toString()));
    }
    
    requestForm.get().update(primaryKey);
    bookForm.get().update(models.Request.find().byId(primaryKey).getBook().getPrimaryKey());
    
    return redirect(routes.RequestsAndOffers.index());
  }

  public static Result delete(Long primaryKey) {
    models.Request.find().byId(primaryKey).delete();
    return redirect(routes.RequestsAndOffers.index());
  }
}
