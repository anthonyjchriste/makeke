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
import models.Request;
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
   * Books are managed in this system through requests and through offers. Therefore, we first
   * need to set up some fake requests or offers to associate with the books.
   */
  @Test
  public void testBookController() {
    
    // Test GET /books on an empty database
    Result result = callAction(controllers.routes.ref.Book.index(), fakeRequest().withSession("connected", "_tester"));
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
    
    result = callAction(controllers.routes.ref.Request.edit(request.getPrimaryKey()), fakeRequest().withSession("connected", "_tester"));
    //System.out.println(contentAsString(result));
    assertTrue("One book", contentAsString(result).contains(isbn));
    
    
    // Test that the created book can be retrieved.
    result = callAction(controllers.routes.ref.Request.edit(request.getPrimaryKey()), fakeRequest().withSession("connected", "_tester"));
    assertTrue("Book detail", contentAsString(result).contains(isbn));
    
    // Test for a request to a book that doesn't exist.
    result = callAction(controllers.routes.ref.Request.edit(999), fakeRequest().withSession("connected", "_tester"));
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
    fakeRequest.withFormUrlEncodedBody(bookData).withFormUrlEncodedBody(requestData).withSession("connected", "_tester");
    result = callAction(controllers.routes.ref.Request.create(), fakeRequest);
    assertEquals("Create new book", OK, status(result));
    
    // Test POST /request (with simulated, invalid form data).
    fakeRequest = fakeRequest().withSession("connected", "_tester");
    result = callAction(controllers.routes.ref.Request.save(), fakeRequest);
    assertTrue("Create bad book fails", contentAsString(result).contains("Error"));
    
    // Test DELETE /books/book-01 (a valid BookId).
    result = callAction(controllers.routes.ref.Request.delete(request.getPrimaryKey()), fakeRequest().withSession("connected", "_tester"));
    assertEquals("Delete current Book OK", SEE_OTHER, status(result));
    result = callAction(controllers.routes.ref.Request.edit(request.getPrimaryKey()), fakeRequest().withSession("connected", "_tester"));
    assertEquals("Deleted Book gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Request.delete(request.getPrimaryKey()), fakeRequest().withSession("connected", "_tester"));
    assertEquals("Delete missing Book also OK", SEE_OTHER, status(result));
  }
  
  /*
  @Test
  public void testOfferController() {
    // Test GET /offers on an empty database
    Result result = callAction(controllers.routes.ref.Offer.index());
    assertTrue("Empty offers", contentAsString(result).contains("No offers"));
    
    // Test GET /offers on a database containing a single offer.
    String offerId = "Offer-01";
    Offer offer = new Offer(offerId, new Book("Book-01", "name", "condition", "isbn", 1L));
    offer.save();
    result = callAction(controllers.routes.ref.Offer.index());
    assertTrue("One offer", contentAsString(result).contains(offerId));
    
    // Test GET /offers/Offer-01
    result = callAction(controllers.routes.ref.Offer.details(offerId));
    assertTrue("Offer detail", contentAsString(result).contains(offerId));
    
    // Test GET /offers/BadOfferId and make sure we get a 404
    result = callAction(controllers.routes.ref.Offer.details("BadOfferId"));
    assertEquals("Offer detail (bad)", NOT_FOUND, status(result));
    
    // Test POST /offers (with simulated, valid from data).
    Map<String, String> offerData = new HashMap<>();
    Book book = new Book("Book-02", "name", "condition", "isbn", 1L);
    Student student = new Student("Student-01", "firstName", "lastName", "email@email.com");
    book.save();
    student.save();
    offerData.put("offerId", "Offer-02");
    offerData.put("book", "Book-02");
    offerData.put("student", "Student-01");
    FakeRequest request = fakeRequest();
    request.withFormUrlEncodedBody(offerData);
    result = callAction(controllers.routes.ref.Offer.newOffer(), request);
    assertEquals("Create new offer", OK, status(result));
    
    // Test POST /offers (with simulated, invalid form data).
    request = fakeRequest();
    result = callAction(controllers.routes.ref.Offer.newOffer(), request);
    assertEquals("Create bad offer fails", BAD_REQUEST, status(result));
    
    // Test DELETE /offers/offer-01 (a valid OfferId).
    result = callAction(controllers.routes.ref.Offer.delete(offerId));
    assertEquals("Delete current Offer OK", OK, status(result));
    result = callAction(controllers.routes.ref.Offer.details(offerId));
    assertEquals("Deleted Offer gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Offer.delete(offerId));
    assertEquals("Delete missing Offer also OK", OK, status(result));
  }
  
  @Test
  public void testRequestController() {
    // Test GET /request on an empty database
    Result result = callAction(controllers.routes.ref.Request.index());
    assertTrue("Empty requests", contentAsString(result).contains("No requests"));
    
    // Test GET /requests on a database containing a single request.
    String requestId = "Request-01";
    Request request = new Request(requestId, new Book("Book-01", "name", "condition", "isbn", 1L));
    request.save();
    result = callAction(controllers.routes.ref.Request.index());
    assertTrue("One request", contentAsString(result).contains(requestId));
    
    // Test GET /requests/Request-01
    result = callAction(controllers.routes.ref.Request.details(requestId));
    assertTrue("Request detail", contentAsString(result).contains(requestId));
    
    // Test GET /requests/BadRequestId and make sure we get a 404
    result = callAction(controllers.routes.ref.Request.details("BadRequestId"));
    assertEquals("Request detail (bad)", NOT_FOUND, status(result));
    
    // Test POST /requests (with simulated, valid from data).
    Map<String, String> requestData = new HashMap<>();
    Book book = new Book("Book-02", "name", "condition", "isbn", 1L);
    Student student = new Student("Student-01", "firstName", "lastName", "email@email.com");
    book.save();
    student.save();
    requestData.put("requestId", "Request-02");
    requestData.put("book", "Book-02");
    requestData.put("student", "Student-01");
    FakeRequest fakeRequest = fakeRequest();
    fakeRequest.withFormUrlEncodedBody(requestData);
    result = callAction(controllers.routes.ref.Request.newRequest(), fakeRequest);
    assertEquals("Create new request", OK, status(result));
    
    // Test POST /requests (with simulated, invalid form data).
    fakeRequest = fakeRequest();
    result = callAction(controllers.routes.ref.Request.newRequest(), fakeRequest);
    assertEquals("Create bad request fails", BAD_REQUEST, status(result));
    
    // Test DELETE /requests/Request-01 (a valid RequestId).
    result = callAction(controllers.routes.ref.Request.delete(requestId));
    assertEquals("Delete current request OK", OK, status(result));
    result = callAction(controllers.routes.ref.Request.details(requestId));
    assertEquals("Deleted request gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Request.delete(requestId));
    assertEquals("Delete missing request also OK", OK, status(result));
  }
  
  @Test
  public void testStudentController() {
    ReverseStudent reverseStudent = controllers.routes.ref.Student;
    // Test GET /students on an empty database
    Result result = callAction(reverseStudent.index());
    assertTrue("Empty students", contentAsString(result).contains("No students"));
    
    // Test GET /students on a database containing a single student.
    String studentId = "Student-01";
    Student student = new Student(studentId, "firstName","lastName", "email@email.com");
    student.save();
    result = callAction(reverseStudent.index());
    assertTrue("One student", contentAsString(result).contains(studentId));
    
    // Test GET /students/Student-01
    result = callAction(reverseStudent.details(studentId));
    assertTrue("Student detail", contentAsString(result).contains(studentId));
    
    // Test GET /students/BadStudentId and make sure we get a 404
    result = callAction(reverseStudent.details("BadStudentId"));
    assertEquals("Student detail (bad)", NOT_FOUND, status(result));
    
    // Test POST /students (with simulated, valid from data).
    Map<String, String> studentData = new HashMap<>();
    studentData.put("studentId", "Student-02");
    studentData.put("firstName", "firstName");
    studentData.put("lastName", "lastName");
    studentData.put("email", "email@email.com");
    FakeRequest fakeRequest = fakeRequest();
    fakeRequest.withFormUrlEncodedBody(studentData);
    result = callAction(reverseStudent.newStudent(), fakeRequest);
    assertEquals("Create new student", OK, status(result));
    
    // Test POST /studets (with simulated, invalid form data).
    fakeRequest = fakeRequest();
    result = callAction(reverseStudent.newStudent(), fakeRequest);
    assertEquals("Create bad student fails", BAD_REQUEST, status(result));
    
    // Test DELETE /students/Student-01 (a valid RequestId).
    result = callAction(reverseStudent.delete(studentId));
    assertEquals("Delete current student OK", OK, status(result));
    result = callAction(reverseStudent.details(studentId));
    assertEquals("Deleted student gone", NOT_FOUND, status(result));
    result = callAction(reverseStudent.delete(studentId));
    assertEquals("Delete missing student also OK", OK, status(result));
  }
  */
}
