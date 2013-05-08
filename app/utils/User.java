package utils;

import models.Student;
import play.mvc.Controller;

public class User {
  public static Student getStudent() {
    String studentId = Controller.session().get("connected"); 
    if(studentId == null) {
      return null;
    }
    return Student.find().where().eq("studentId", studentId).findUnique();
  }
}
