package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.offerCreate;
import views.html.offerEdit;
import views.html.error;

public class Offer extends Controller {

  public static Result index() {
    List<models.Offer> offers = models.Offer.find().findList();
    return ok(offers.isEmpty() ? "No offers" : offers.toString());
  }

  public static Result details(String offerId) {
    models.Offer offer = models.Offer.find().where().eq("offerId", offerId).findUnique();
    return (offer == null) ? notFound("No offer found") : ok(offer.toString());
  }

  public static Result create() {
    Form<models.Offer> offerForm = form(models.Offer.class);
    List<models.Book> books = models.Book.find().all();
    return ok(offerCreate.render(offerForm, books));
  }

  public static Result save() {
    Form<models.Offer> offerForm = form(models.Offer.class).bindFromRequest();
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();

    if (offerForm.hasErrors()) {
      return ok(error.render("Error with offer fields", offerForm.errors().toString()));
    }
    
    if(bookForm.hasErrors()) {
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
  
  public static Result edit(Long primaryKey) {
    models.Offer offer = models.Offer.find().byId(primaryKey);
    Form<models.Offer> offerForm = form(models.Offer.class).fill(offer);
    Form<models.Book> bookForm = form(models.Book.class).fill(offer.getBook());
    return ok(offerEdit.render(primaryKey, offerForm, bookForm));
  }
  
  public static Result update(Long primaryKey) {
    Form<models.Offer> offerForm = form(models.Offer.class).bindFromRequest();
    Form<models.Book> bookForm = form(models.Book.class).bindFromRequest();
    
    if (offerForm.hasErrors()) {
      return ok(error.render("Error with offer fields", offerForm.errors().toString()));
    }
    
    if(bookForm.hasErrors()) {
      return ok(error.render("Error with book fields", bookForm.errors().toString()));
    }
    
    offerForm.get().update(primaryKey);
    bookForm.get().update(models.Offer.find().byId(primaryKey).getBook().getPrimaryKey());
    
    return redirect(routes.RequestsAndOffers.index());
  }

  public static Result delete(Long primaryKey) {
    models.Offer.find().byId(primaryKey).delete();
    return redirect(routes.RequestsAndOffers.index());
  }
}
