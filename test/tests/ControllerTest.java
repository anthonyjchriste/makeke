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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.status;
import static play.test.Helpers.stop;
import java.util.HashMap;
import java.util.Map;
import models.Book;
import models.Offer;
import models.Request;
import models.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;

public class ControllerTest {
  private FakeApplication application;

  @Before
  public void startApp() {
    application = fakeApplication(inMemoryDatabase());
    start(application);
  }

  @After
  public void stopApp() {
    stop(application);
  }

  /**
   * Books are managed in this system through requests and through offers. Therefore, we first need
   * to set up some fake requests or offers to associate with the books.
   */
  @Test
  public void testBookController() {

    // Test GET /books on an empty database
    Result result =
        callAction(controllers.routes.ref.Book.index(),
            fakeRequest().withSession("connected", "_tester"));
    assertTrue("Empty books", contentAsString(result).contains("No Books"));

    // Test GET /books on a database containing a single book.
    String isbn = "12345";
    Book book = new Book("Book-01", "name", "edition", "condition", isbn, 1L);
    book.save();
    // Associate the book with a request
    Request request = new Request("Request-01", book);
    book.setRequest(request);
    book.save();
    request.setBook(book);
    request.save();

    result =
        callAction(controllers.routes.ref.Request.edit(request.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertTrue("One book", contentAsString(result).contains(isbn));

    // Test that the created book can be retrieved.
    result =
        callAction(controllers.routes.ref.Request.edit(request.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertTrue("Book detail", contentAsString(result).contains(isbn));

    // Test for a request to a book that doesn't exist.
    result =
        callAction(controllers.routes.ref.Request.edit(999),
            fakeRequest().withSession("connected", "_tester"));
    assertEquals("Book detail (bad)", NOT_FOUND, status(result));

    // Test POST /request (with simulated, valid from data).
    Map<String, String> bookData = new HashMap<>();
    Map<String, String> requestData = new HashMap<>();
    bookData.put("bookId", "Book-02");
    bookData.put("name", "name");
    bookData.put("condition", "condition");
    bookData.put("isbn", "isbn");
    bookData.put("price", "1");
    requestData.put("student", "Student-01");
    requestData.put("requestId", "Request-01");
    requestData.put("book", "Book-01");
    FakeRequest fakeRequest = fakeRequest();
    fakeRequest.withFormUrlEncodedBody(bookData).withFormUrlEncodedBody(requestData)
        .withSession("connected", "_tester");
    result = callAction(controllers.routes.ref.Request.create(), fakeRequest);
    assertEquals("Create new book", OK, status(result));

    // Test POST /request (with simulated, invalid form data).
    fakeRequest = fakeRequest().withSession("connected", "_tester");
    result = callAction(controllers.routes.ref.Request.save(), fakeRequest);
    assertTrue("Create bad book fails", contentAsString(result).contains("Error"));

    // Test deleting a book.
    result =
        callAction(controllers.routes.ref.Request.delete(request.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Delete current Book OK", SEE_OTHER, status(result));
    result =
        callAction(controllers.routes.ref.Request.edit(request.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Deleted Book gone", NOT_FOUND, status(result));
    result =
        callAction(controllers.routes.ref.Request.delete(request.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Delete missing Book also OK", SEE_OTHER, status(result));
  }

  @Test
  public void testOfferController() {
    // Test finding an offer on an empty database.
    Result result =
        callAction(controllers.routes.ref.RequestsAndOffers.index(),
            fakeRequest().withSession("connected", "_tester"));
    assertTrue("Empty offers", contentAsString(result).contains("No Offers"));

    // Save an offer and make sure it shows up in results.
    String offerId = "Offer-01";
    String isbn = "1234";
    Student student = new Student("Student-01", "firstName", "lastName", "email@email.com");
    Book book = new Book("Book-01", "name", "edition", "condition", isbn, 1L);
    Offer offer = new Offer(offerId, book);
    book.setOffer(offer);
    offer.setBook(book);
    offer.setStudent(student);
    student.getOffers().add(offer);
    student.save();
    offer.save();
    book.save();
    result =
        callAction(controllers.routes.ref.Book.index(),
            fakeRequest().withSession("connected", "_tester"));
    assertTrue("One offer", contentAsString(result).contains(isbn));

    // Test viewing a single offer.
    result =
        callAction(controllers.routes.ref.Offer.edit(offer.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertTrue("Offer detail", contentAsString(result).contains(offerId));

    // Test viewing an invalid offer.
    result =
        callAction(controllers.routes.ref.Offer.edit(999),
            fakeRequest().withSession("connected", "_tester"));
    assertEquals("Offer detail (bad)", NOT_FOUND, status(result));

    // Test POST /offers (with simulated, valid from data).
    Map<String, String> offerData = new HashMap<>();
    Map<String, String> bookData = new HashMap<>();

    bookData.put("bookId", "Book-02");
    bookData.put("name", "name");
    bookData.put("condition", "condition");
    bookData.put("isbn", "isbn");
    bookData.put("price", "1");
    offerData.put("student", "Student-01");
    offerData.put("requestId", "Request-01");
    offerData.put("book", "Book-02");
    FakeRequest request = fakeRequest().withSession("connected", "_tester");
    request.withFormUrlEncodedBody(offerData).withFormUrlEncodedBody(bookData);
    result = callAction(controllers.routes.ref.Offer.create(), request);
    assertEquals("Create new offer", OK, status(result));

    // Test creating an invalid offer
    request = fakeRequest().withSession("connected", "_tester");
    result = callAction(controllers.routes.ref.Offer.save(), request);
    assertTrue("Create bad offer fails", contentAsString(result).contains("Error"));

    // Test deleting a valid offer.
    result =
        callAction(controllers.routes.ref.Offer.delete(offer.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Delete current Offer OK", SEE_OTHER, status(result));
    result =
        callAction(controllers.routes.ref.Offer.edit(offer.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Deleted Offer gone", NOT_FOUND, status(result));
    result =
        callAction(controllers.routes.ref.Offer.delete(offer.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Delete missing Offer also OK", SEE_OTHER, status(result));
  }

  @Test
  public void testRequestController() {
    // Test finding an request on an empty database.
    Result result =
        callAction(controllers.routes.ref.RequestsAndOffers.index(),
            fakeRequest().withSession("connected", "_tester"));
    assertTrue("Empty requests", contentAsString(result).contains("No Requests"));

    // Save an request and make sure it shows up in results.
    String requestId = "Request-01";
    String isbn = "1234";
    Student student = new Student("Student-01", "firstName", "lastName", "email@email.com");
    Book book = new Book("Book-01", "name", "edition", "condition", isbn, 1L);
    Request request = new Request(requestId, book);
    book.setRequest(request);
    request.setBook(book);
    request.setStudent(student);
    student.getRequests().add(request);
    student.save();
    request.save();
    book.save();
    result =
        callAction(controllers.routes.ref.Book.index(),
            fakeRequest().withSession("connected", "_tester"));
    assertTrue("One request", contentAsString(result).contains(isbn));

    // Test viewing a single request.
    result =
        callAction(controllers.routes.ref.Request.edit(request.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertTrue("Request detail", contentAsString(result).contains(requestId));

    // Test viewing an invalid request.
    result =
        callAction(controllers.routes.ref.Request.edit(999),
            fakeRequest().withSession("connected", "_tester"));
    assertEquals("Request detail (bad)", NOT_FOUND, status(result));

    // Test POST /requests (with simulated, valid from data).
    Map<String, String> requestData = new HashMap<>();
    Map<String, String> bookData = new HashMap<>();

    bookData.put("bookId", "Book-02");
    bookData.put("name", "name");
    bookData.put("condition", "condition");
    bookData.put("isbn", "isbn");
    bookData.put("price", "1");
    requestData.put("student", "Student-01");
    requestData.put("requestId", "Request-01");
    requestData.put("book", "Book-02");
    FakeRequest fakeRequest = fakeRequest().withSession("connected", "_tester");
    fakeRequest.withFormUrlEncodedBody(requestData).withFormUrlEncodedBody(bookData);
    result = callAction(controllers.routes.ref.Request.create(), fakeRequest);
    assertEquals("Create new request", OK, status(result));

    // Test creating an invalid request
    fakeRequest = fakeRequest().withSession("connected", "_tester");
    result = callAction(controllers.routes.ref.Request.save(), fakeRequest);
    assertTrue("Create bad request fails", contentAsString(result).contains("Error"));

    // Test deleting a valid request.
    result =
        callAction(controllers.routes.ref.Request.delete(request.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Delete current Request OK", SEE_OTHER, status(result));
    result =
        callAction(controllers.routes.ref.Request.edit(request.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Deleted Request gone", NOT_FOUND, status(result));
    result =
        callAction(controllers.routes.ref.Request.delete(request.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Delete missing Request also OK", SEE_OTHER, status(result));
  }

  @Test
  public void testStudentController() {
    // Test getting the details of a single student
    String studentId = "Student-01";
    Student student = new Student(studentId, "firstName", "lastName", "email@email.com");
    student.save();
    Result result =
        callAction(controllers.routes.ref.Student.edit(student.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertTrue("One student", contentAsString(result).contains(studentId));

    // Test that invalid students return a not found status.
    result =
        callAction(controllers.routes.ref.Student.edit(999),
            fakeRequest().withSession("connected", "_tester"));
    assertEquals("Student detail (bad)", NOT_FOUND, status(result));

    // Test POST /students (with simulated, valid from data).
    Map<String, String> studentData = new HashMap<>();
    studentData.put("studentId", "Student-02");
    studentData.put("firstName", "firstName");
    studentData.put("lastName", "lastName");
    studentData.put("email", "email@email.com");
    studentData.put("password", "password");
    FakeRequest fakeRequest = fakeRequest();
    fakeRequest.withFormUrlEncodedBody(studentData);
    result = callAction(controllers.routes.ref.Student.save(), fakeRequest);
    assertEquals("Create new student", SEE_OTHER, status(result));

    // Test POST /studets (with simulated, invalid form data).
    fakeRequest = fakeRequest();
    result = callAction(controllers.routes.ref.Student.save(), fakeRequest);
    assertTrue("Create bad student fails", contentAsString(result).contains("Error"));

    // Test deleting a valid student
    result =
        callAction(controllers.routes.ref.Student.delete(student.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Delete current student SEE_OTHER", SEE_OTHER, status(result));
    result =
        callAction(controllers.routes.ref.Student.edit(student.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Deleted student gone", NOT_FOUND, status(result));
    result =
        callAction(controllers.routes.ref.Student.delete(student.getPrimaryKey()), fakeRequest()
            .withSession("connected", "_tester"));
    assertEquals("Delete missing student also SEE_OTHER", SEE_OTHER, status(result));
  }
}
