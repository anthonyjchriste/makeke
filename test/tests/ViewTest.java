package tests;

import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;
import org.junit.Test;
//import pages.ProductCreatePage;
//import pages.ProductEditPage;
//import pages.StockItemCreatePage;
//import pages.StockItemEditPage;
//import pages.WarehouseEditPage;
import tests.pages.BookIndexPage;
import tests.pages.IndexPage;
import tests.pages.StudentCreatePage;
import tests.pages.StudentEditPage;
//import pages.WarehouseCreatePage;
import play.libs.F.Callback;
import play.test.TestBrowser;
import static org.fest.assertions.Assertions.assertThat;

public class ViewTest {
  @Test
  public void testIndexPage() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT,
        new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
            IndexPage homePage = new IndexPage(browser.getDriver(), 3333);
            browser.goTo(homePage);
            homePage.isAt();
            homePage.gotoCreateStudent();
          }
        });
  }

  @Test
  public void testStudentCreatePage() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT,
        new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
            StudentCreatePage studentCreatePage = new StudentCreatePage(browser.getDriver(), 3333);
            BookIndexPage bookIndexPage = new BookIndexPage(browser.getDriver(), 3333);
            StudentEditPage studentEditPage = new StudentEditPage(browser.getDriver(), 3333, 1L);
            browser.goTo(studentCreatePage);
            studentCreatePage.isAt();
            studentCreatePage.makeNewStudent("Student-01", "Papa", "Smurf", "psmurf@gmail.com",
                "abc");
            bookIndexPage.isAt();
            browser.goTo(studentEditPage);
            studentEditPage.isAt();
            assertThat(studentEditPage.pageSource().contains("Student-01"));
            
            
            // Testing the edit page here as well so we don't need create create the above student
            studentEditPage.editStudent("Student-02", "Papa", "Smurf", "psmurf@gmail.com",
                "abc");
            bookIndexPage.isAt();
            browser.goTo(studentEditPage);
            
            assertThat(studentEditPage.pageSource().contains("Student-02"));         
          }
        });
  }
}
