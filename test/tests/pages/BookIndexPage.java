package tests.pages;

import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.WebDriver;

public class BookIndexPage extends FluentPage {
  private String url;

  public BookIndexPage(WebDriver webDriver, int port) {
    super(webDriver);
    this.url = "http://localhost:" + port + "/books";
  }

  public String getUrl() {
    return this.url;
  }

  public void isAt() {
    assert (title().equals("Book Explorer"));
  }
}
