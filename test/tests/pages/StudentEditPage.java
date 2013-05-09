package tests.pages;

import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class StudentEditPage extends FluentPage {
  private String url;

  public StudentEditPage(WebDriver webDriver, int port, Long primaryKey) {
    super(webDriver);
    this.url = "http://localhost:" + port + "/student/" + primaryKey.toString();
  }

  public String getUrl() {
    return this.url;
  }

  public void isAt() {
    assert (title().equals("Edit Student Information"));
  }
  
  public void editStudent(String studentId, String firstName, String lastName, String email,
      String password) {
    fill("#studentId").with(studentId);
    fill("#firstName").with(firstName);
    fill("#lastName").with(lastName);
    fill("#email").with(email);
    fill("#password").with(password);
    submit("#editStudent");
  }
}
