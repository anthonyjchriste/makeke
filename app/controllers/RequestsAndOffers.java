package controllers;

import java.util.List;
import play.mvc.Controller;
import play.mvc.Result;
import utils.User;
import views.html.*;

public class RequestsAndOffers extends Controller {
  
  public static Result index() {
    models.Student student = User.getStudent();
    if(student == null) {
      return ok(error.render("You must be logged in to do that.", "Please login or create an account."));
    }
    
    List<models.Request> requests = student.getRequests();
    List<models.Offer> offers = student.getOffers();
    return ok(requestsAndOffersManage.render(requests, offers));
  }
}
