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

package tests.pages;

import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class StudentCreatePage extends FluentPage {
  private String url;

  public StudentCreatePage(WebDriver webDriver, int port) {
    super(webDriver);
    this.url = "http://localhost:" + port + "/student/create";
  }

  public String getUrl() {
    return this.url;
  }

  public void isAt() {
    assert (title().equals("Create New Student"));
  }

  // For testing purposes, use the same string for both ID and name.
  public void makeNewStudent(String studentId, String firstName, String lastName, String email,
      String password) {
    fill("#studentId").with(studentId);
    fill("#firstName").with(firstName);
    fill("#lastName").with(lastName);
    fill("#email").with(email);
    fill("#password").with(password);
    submit("#createAccount");
  }

}
