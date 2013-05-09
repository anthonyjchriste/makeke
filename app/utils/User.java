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

package utils;

import models.Student;
import play.mvc.Controller;

/**
 * Provides utilities for determining if a student is logged into the application or not.
 * 
 * @author Anthony Christe
 */
public class User {
  /**
   * Get the Student that is currently logged in.
   * 
   * @return The currently logged in student or null if no one is logged in.
   */
  public static Student getStudent() {
    String studentId = Controller.session().get("connected");
    if (studentId == null) {
      return null;
    }

    // Note this is an ugly hack to get around needing to be logged in during testing.
    // In the tests, a session variable is passed with the value test, this should return
    // a valid student so that controllers believe that a student is logged in.
    if (studentId.equals("_tester")) {
      return new Student("_tester", "firstName", "lastName", "email@email.com");
    }

    return Student.find().where().eq("studentId", studentId).findUnique();
  }
}
