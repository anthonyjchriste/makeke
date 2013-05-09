/*
 * This file is part of Makeke.
 *
 *  Makeke is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Makeke is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Makeke.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Copyright (C) Anthony Christe 2013 
 */

package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.User;
import views.html.error;
import views.html.requestCreate;
import views.html.requestEdit;

/**
 * Manages the views and models of Requests.
 * 
 * @author Anthony Christe
 * 
 */
public class Request extends Controller {
  /**
   * Displays form to create a new Request.
   * 
   * Also allows user to select from already defined book.
   * 
   * @return Rendered create request form.
   */
  public static Result create() {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    Form<models.Request> requestForm = form(models.Request.class);
    List<models.Book> books = models.Book.find().all();
    return ok(requestCreate.render(requestForm, books));
  }

  /**
   * Saves a new request and redirects back to manager. This will also create a new book mapped to
   * this request.
   * 
   * @return a redirect back to the manager after this request has been saved.
   */
  public static Result save() {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    Form<models.Request> requestForm = form(models.Request.class).bindFromRequest();
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();

    if (requestForm.hasErrors()) {
      return ok(error.render("Error with request fields", requestForm.errors().toString()));
    }

    if (bookForm.hasErrors()) {
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

  /**
   * Displays a form to edit a current request.
   * 
   * @param primaryKey The primaryKey of this request.
   * @return A rendered form to edit this request.
   */
  public static Result edit(Long primaryKey) {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    models.Request request = models.Request.find().byId(primaryKey);
    
    if(request == null) {
      return notFound("Invalid primary key");
    }
    
    Form<models.Request> requestForm = form(models.Request.class).fill(request);
    Form<models.Book> bookForm = form(models.Book.class).fill(request.getBook());
    return ok(requestEdit.render(primaryKey, requestForm, bookForm));
  }

  /**
   * Updates an edited request.
   * 
   * @param primaryKey The primaryKey of this request.
   * @return A redirect back to the manager after this request has been updated.
   */
  public static Result update(Long primaryKey) {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    Form<models.Request> requestForm = form(models.Request.class).bindFromRequest();
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();

    if (requestForm.hasErrors()) {
      return ok(error.render("Error with request fields", requestForm.errors().toString()));
    }

    if (bookForm.hasErrors()) {
      return ok(error.render("Error with book fields", bookForm.errors().toString()));
    }

    requestForm.get().update(primaryKey);
    // Need to find the primaryKey of the book mapped by this request.
    bookForm.get().update(models.Request.find().byId(primaryKey).getBook().getPrimaryKey());

    return redirect(routes.RequestsAndOffers.index());
  }

  /**
   * Deletes a request.
   * 
   * @param primaryKey The primaryKey of the request to be deleted.
   * @return A redirect back to the manager after this request has been deleted.
   */
  public static Result delete(Long primaryKey) {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }
    
    models.Request request = models.Request.find().byId(primaryKey);
    
    if(request != null) {
      request.delete();
    }
    
    return redirect(routes.RequestsAndOffers.index());
  }
}
