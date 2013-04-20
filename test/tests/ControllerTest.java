package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
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
import controllers.ref.ReverseStudent;

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
  
  @Test
  public void testBookController() {
    // Test GET /books on an empty database
    Result result = callAction(controllers.routes.ref.Book.index());
    assertTrue("Empty books", contentAsString(result).contains("No books"));
    
    // Test GET /books on a database containing a single book.
    String bookId = "Book-01";
    Book book = new Book(bookId, "name", "condition", "isbn", 1L);
    book.save();
    result = callAction(controllers.routes.ref.Book.index());
    assertTrue("One book", contentAsString(result).contains(bookId));
    
    // Test GET /books/Book-01
    result = callAction(controllers.routes.ref.Book.details(bookId));
    assertTrue("Book detail", contentAsString(result).contains(bookId));
    
    // Test GET /books/BadBookId and make sure we get a 404
    result = callAction(controllers.routes.ref.Book.details("BadBookId"));
    assertEquals("Book detail (bad)", NOT_FOUND, status(result));
    
    // Test POST /books (with simulated, valid from data).
    Map<String, String> bookData = new HashMap<>();
    bookData.put("bookId", "Book-02");
    bookData.put("name", "name");
    bookData.put("condition", "condition");
    bookData.put("isbn", "isbn");
    bookData.put("price", "1");
    FakeRequest request = fakeRequest();
    request.withFormUrlEncodedBody(bookData);
    result = callAction(controllers.routes.ref.Book.newBook(), request);
    assertEquals("Create new book", OK, status(result));
    
    // Test POST /books (with simulated, invalid form data).
    request = fakeRequest();
    result = callAction(controllers.routes.ref.Book.newBook(), request);
    assertEquals("Create bad book fails", BAD_REQUEST, status(result));
    
    // Test DELETE /books/book-01 (a valid BookId).
    result = callAction(controllers.routes.ref.Book.delete(bookId));
    assertEquals("Delete current Book OK", OK, status(result));
    result = callAction(controllers.routes.ref.Book.details(bookId));
    assertEquals("Deleted Book gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Book.delete(bookId));
    assertEquals("Delete missing Book also OK", OK, status(result));
  }
  
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
  
}
