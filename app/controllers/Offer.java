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
import views.html.offerCreate;
import views.html.offerEdit;
import views.html.error;

/**
 * Manages the views and models of Offers.
 * 
 * @author Anthony Christe
 * 
 */
public class Offer extends Controller {

  /**
   * Displays form to create a new Offer.
   * 
   * Also allows user to select from already defined book.
   * 
   * @return Rendered create offer form.
   */
  public static Result create() {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    Form<models.Offer> offerForm = form(models.Offer.class);
    List<models.Book> books = models.Book.find().all();
    return ok(offerCreate.render(offerForm, books));
  }

  /**
   * Saves a new offer and redirects back to manager. This will also create a new book mapped to
   * this offer.
   * 
   * @return a redirect back to the manager after this Offer has been saved.
   */
  public static Result save() {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    Form<models.Offer> offerForm = form(models.Offer.class).bindFromRequest();
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();

    if (offerForm.hasErrors()) {
      return ok(error.render("Error with offer fields", offerForm.errors().toString()));
    }

    if (bookForm.hasErrors()) {
      return ok(error.render("Error with book fields", bookForm.errors().toString()));
    }

    models.Offer offer = offerForm.get();
    models.Book book = bookForm.get();
    offer.getStudent().getOffers().add(offer);
    offer.getStudent().save();

    offer.setBook(book);
    book.setOffer(offer);

    offer.save();
    book.save();

    return redirect(routes.RequestsAndOffers.index());
  }

  /**
   * Displays a form to edit a current offer.
   * 
   * @param primaryKey The primaryKey of this offer.
   * @return A rendered form to edit this Offer.
   */
  public static Result edit(Long primaryKey) {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }
    
    models.Offer offer = models.Offer.find().byId(primaryKey);
    
    if(offer == null) {
      return notFound("Invalid primary key");
    }
    
    Form<models.Offer> offerForm = form(models.Offer.class).fill(offer);
    Form<models.Book> bookForm = form(models.Book.class).fill(offer.getBook());
    return ok(offerEdit.render(primaryKey, offerForm, bookForm));
  }

  /**
   * Updates an edited offer.
   * 
   * @param primaryKey The primaryKey of this offer.
   * @return A redirect back to the manager after this offer has been updated.
   */
  public static Result update(Long primaryKey) {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    Form<models.Offer> offerForm = form(models.Offer.class).bindFromRequest();
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();

    if (offerForm.hasErrors()) {
      return ok(error.render("Error with offer fields", offerForm.errors().toString()));
    }

    if (bookForm.hasErrors()) {
      return ok(error.render("Error with book fields", bookForm.errors().toString()));
    }

    offerForm.get().update(primaryKey);
    // Need to find the primaryKey of the book mapped by this offer.
    bookForm.get().update(models.Offer.find().byId(primaryKey).getBook().getPrimaryKey());

    return redirect(routes.RequestsAndOffers.index());
  }

  /**
   * Deletes an offer.
   * 
   * @param primaryKey The primaryKey of the offer to be deleted.
   * @return A redirect back to the manager after this offer has been deleted.
   */
  public static Result delete(Long primaryKey) {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    models.Offer offer = models.Offer.find().byId(primaryKey);
    
    if(offer != null) {
      offer.delete();
    }
    
    return redirect(routes.RequestsAndOffers.index());
  }
}
