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

package tests;

import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;
import org.junit.Test;
import tests.pages.BookIndexPage;
import tests.pages.IndexPage;
import tests.pages.RequestCreatePage;
import tests.pages.RequestEditPage;
import tests.pages.RequestPage;
import tests.pages.StudentCreatePage;
import tests.pages.StudentEditPage;
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
  
  @Test
  public void testRequestPage() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT,
        new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
            RequestPage requestPage = new RequestPage(browser.getDriver(), 3333);
            requestPage.isAt();
          }
        });
  }
  
  @Test
  public void testRequestCreatePage() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT,
        new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
            // Make sure a student is logged in (hack, but it's late)
            StudentCreatePage studentCreatePage = new StudentCreatePage(browser.getDriver(), 3333);
            browser.goTo(studentCreatePage);
            studentCreatePage.isAt();
            studentCreatePage.makeNewStudent("Student-01", "Papa", "Smurf", "psmurf@gmail.com",
                "abc");
            
            RequestPage requestPage = new RequestPage(browser.getDriver(), 3333);
            RequestCreatePage requestCreatePage = new RequestCreatePage(browser.getDriver(), 3333);
            browser.goTo(requestCreatePage);
            requestCreatePage.isAt();
            requestCreatePage.makeNewRequest("Request-01", "Book-01", "Book", "Edition", "1234", "20", "Good");
            requestPage.isAt();
            assertThat(requestPage.pageSource().contains("Book"));
          }
        });
  }
  
  @Test
  public void testRequestEditPage() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT,
        new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
            StudentCreatePage studentCreatePage = new StudentCreatePage(browser.getDriver(), 3333);
            browser.goTo(studentCreatePage);
            studentCreatePage.isAt();
            studentCreatePage.makeNewStudent("Student-01", "Papa", "Smurf", "psmurf@gmail.com",
                "abc");
            
            RequestPage requestPage = new RequestPage(browser.getDriver(), 3333);
            RequestCreatePage requestCreatePage = new RequestCreatePage(browser.getDriver(), 3333);
            browser.goTo(requestCreatePage);
            requestCreatePage.isAt();
            requestCreatePage.makeNewRequest("Request-01", "Book-01", "Book", "Edition", "1234", "20", "Good");
            requestPage.isAt();
            assertThat(requestPage.pageSource().contains("Book"));
           
            RequestEditPage requestEditPage = new RequestEditPage(browser.getDriver(), 3333, 1L);
            browser.goTo(requestEditPage);
            requestEditPage.isAt();
            requestEditPage.editRequest("Request-01", "Book-01", "NewBook", "Edition", "1234", "20", "Good");
            requestPage.isAt();
            assertThat(requestPage.pageSource().contains("NewBook"));
            
            // Lets try deleting the request
            browser.goTo(requestEditPage);
            requestEditPage.deleteRequest();
            
            assertThat(requestPage.pageSource().contains("No Requests"));
          }
        });
  }
  
  
}
