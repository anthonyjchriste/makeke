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
import java.util.Arrays;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Password;
import utils.User;
import views.html.error;
import views.html.studentCreate;
import views.html.studentEdit;

/**
 * Manages the creation of new student accounts.
 * 
 * @author Anthony Christe
 */
public class Student extends Controller {
  /**
   * Renders a new student registration form.
   * 
   * @return A rendered student registration form.
   */
  public static Result create() {
    Form<models.Student> studentForm = form(models.Student.class);
    return ok(studentCreate.render(studentForm));
  }

  /**
   * Saves a newly created student. Note that once a student creates an account, they are
   * automaticly logged in.
   * 
   * @return A redirect to the Book Explorer.
   */
  public static Result save() {
    Form<models.Student> studentForm = form(models.Student.class).bindFromRequest();

    if (studentForm.hasErrors()) {
      return ok(error.render("Student form has errors", studentForm.errors().toString()));
    }

    models.Student student = studentForm.get();
    student.save();

    // Store this student's studentId to the session.
    session("connected", student.getStudentId());
    return redirect(routes.Book.index());
  }

  /**
   * Attempts to log a student into the system by comparing hashed passwords.
   * 
   * @return A redirect to the Book Manager once logged in.
   */
  public static Result login() {
    DynamicForm loginForm = form().bindFromRequest();
    String email = loginForm.get("email");
    byte[] hash = Password.hash(loginForm.get("password"));

    models.Student student = models.Student.find().where().eq("email", email).findUnique();
    if (student == null) {
      return ok(error.render("E-mail not found.", "Please try logging in again."));
    }

    if (Arrays.equals(hash, student.getPasswordHash())) {
      // Store this student's studentId to the session.
      session("connected", student.getStudentId());
      return redirect(routes.Book.index());
    }

    return ok(error.render("Password does not match email.", "Please try logging in again."));
  }

  /**
   * Clear all cookies and return user to home page.
   * 
   * @return A redirect to the home page.
   */
  public static Result logout() {
    session().clear();
    return redirect(routes.Application.index());
  }

  /**
   * Displays a form to allow editing of an existing account.
   * 
   * @param primaryKey The primary key associated with the currently logged in student.
   * @return A rendered form to edit student information.
   */
  public static Result edit(Long primaryKey) {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    models.Student student = models.Student.find().byId(primaryKey);

    if (student == null) {
      return notFound("Invalid primary key");
    }

    Form<models.Student> studentForm = form(models.Student.class).fill(student);
    return ok(studentEdit.render(primaryKey, studentForm));
  }

  /**
   * Update current student information.
   * 
   * @param primaryKey The primary key associated with the currently logged in student.
   * @return A redirect back to the home page.
   */
  public static Result update(Long primaryKey) {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    Form<models.Student> studentForm = form(models.Student.class).bindFromRequest();
    studentForm.get().update(primaryKey);

    return redirect(routes.Application.index());
  }

  public static Result delete(Long primaryKey) {
    models.Student student = models.Student.find().byId(primaryKey);

    if (student != null) {
      student.delete();
    }

    session().clear();
    return redirect(routes.Application.index());
  }

  /**
   * Redirects the browser to the proper student account page based on the student that is logged
   * in.
   * 
   * @return A redirect to the correct account information page.
   */
  public static Result account() {
    models.Student student = User.getStudent();
    if (student == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    return redirect(routes.Student.edit(student.getPrimaryKey()));
  }
}
