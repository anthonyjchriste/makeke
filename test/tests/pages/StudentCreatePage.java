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
