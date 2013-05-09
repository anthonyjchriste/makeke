package tests.pages;

import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class IndexPage extends FluentPage {
  private String url;

  public IndexPage(WebDriver webDriver, int port) {
    super(webDriver);
    this.url = "http://localhost:" + port;
  }

  public String getUrl() {
    return this.url;
  }

  public void isAt() {
    assert (title().equals("Welcome to Makeke"));
  }

  public void gotoCreateStudent() {
    click("createStudent");
    assert (title().equals("Create New Student"));
  }
}
