package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Request extends Controller {
  
  public static Result index() {
    List<models.Request> requests = models.Request.find().findList();
    return ok(requests.isEmpty() ? "No requests" : requests.toString());
  }
  
  public static Result details(String requestId) {
    models.Request request = models.Request.find().where().eq("requestId", requestId).findUnique();
    return (request == null) ? notFound("No request found") : ok(request.toString());
  }
  
  public static Result newRequest() {
    Form<models.Request> requestForm = form(models.Request.class).bindFromRequest();
    
    if(requestForm.hasErrors()) {
      return badRequest("Bad request");
    }
    
    models.Request request = requestForm.get();
    request.save();
    return ok(request.toString());
  }
  
  public static Result delete(String requestId) {
    models.Request request = models.Request.find().where().eq("requestId", requestId).findUnique();
    
    if(request != null) {
      request.delete();
    }
    
    return ok();
  }
}
