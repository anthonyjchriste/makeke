package controllers;

import static play.data.Form.form;
import java.util.Arrays;
import java.util.List;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Password;
import views.html.studentCreate;
import views.html.login;

public class Student extends Controller {
  
  public static Result index() {
    List<models.Student> students = models.Student.find().findList();
    return ok(students.isEmpty() ? "No students" : students.toString());
  }
  
  public static Result details(String studentId) {
    models.Student student = models.Student.find().where().eq("studentId", studentId).findUnique();
    return (student == null) ? notFound("No student found") : ok(student.toString());
  }
  
  public static Result create() {
    Form<models.Student> studentForm = form(models.Student.class);
    return ok(studentCreate.render(studentForm));
  }
  
  public static Result save() {
    Form<models.Student> studentForm = form(models.Student.class).bindFromRequest();
    
    if(studentForm.hasErrors()) {
      return badRequest(studentForm.toString());
    }
    
    models.Student student = studentForm.get();
    student.save();
    return redirect(routes.Application.index());
  }
  
  public static Result login() {
    DynamicForm loginForm  = form().bindFromRequest();
    String email = loginForm.get("email");
    byte[] hash = Password.hash(loginForm.get("password"));
    
    Logger.info(email + " " + loginForm.get("password"));
    
    models.Student student = models.Student.find().where().eq("email", email).findUnique();
    if(student == null) {
      return ok(login.render("Error - E-mail not found. Please try logging in again."));
    }
    
    if(Arrays.equals(hash, student.getPasswordHash())) {
      session("connected", student.getStudentId());
      return redirect(routes.Application.index());
    }
    else {
      return ok(login.render("Error - Password does not match e-mail. Please try logging in again."));
    }
  }
  
  public static Result logout() {
    session().clear();
    return redirect(routes.Application.index());
  }
  
  public static Result delete(String studentId) {
    models.Student student = models.Student.find().where().eq("studentId", studentId).findUnique();
    
    if(student != null) {
      student.delete();
    }
    
    return ok();
  }
}
