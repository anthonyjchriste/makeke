package controllers;

import static play.data.Form.form;
import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Student extends Controller {
  
  public static Result index() {
    List<models.Student> students = models.Student.find().findList();
    return ok(students.isEmpty() ? "No students" : students.toString());
  }
  
  public static Result details(String studentId) {
    models.Student student = models.Student.find().where().eq("studentId", studentId).findUnique();
    return (student == null) ? notFound("No student found") : ok(student.toString());
  }
  
  public static Result newStudent() {
    Form<models.Student> studentForm = form(models.Student.class).bindFromRequest();
    
    if(studentForm.hasErrors()) {
      return badRequest("Bad request");
    }
    
    models.Student student = studentForm.get();
    student.save();
    return ok(student.toString());
  }
  
  public static Result delete(String studentId) {
    models.Student student = models.Student.find().where().eq("studentId", studentId).findUnique();
    
    if(student != null) {
      student.delete();
    }
    
    return ok();
  }
}
