package tests;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;
import java.util.List;
import models.Book;
import models.Offer;
import models.Request;
import models.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.FakeApplication;

public class ModelTest {
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
  public void testModel() {
    // Create a book to be used in subsequent requests
    Book book = new Book("Book-01", "name", "condition", "isbn", 1L);
    
    // Associate the book with an Offer and a Request
    Offer offer = new Offer("Offer-01", book);
    Request request = new Request("Request-01", book);
    book.offer = offer;
    book.request = request;
    
    // Associate the Request and the Offer with a Student
    Student student = new Student("Student-01", "firstName", "lastName", "email");
    student.offers.add(offer);
    student.requests.add(request);
    offer.student = student;
    request.student = student;
    
    // Persist everything to the db
    student.save();
    offer.save();
    request.save();
    book.save();
    
    // Grab the items back from the db
    List<Student> students = Student.find().findList();
    List<Offer> offers = Offer.find().findList();
    List<Request> requests = Request.find().findList();
    List<Book> books = Book.find().findList();
    
    // Check to make sure all lists contain 1 item
    assertEquals("Checking students", students.size(), 1);
    assertEquals("Checkling offers", offers.size(), 1);
    assertEquals("Checking reuests", requests.size(), 1);
    assertEquals("Checking books", books.size(), 1);

    // Check to make sure we've recovered all relationships
    assertEquals("Student-Offer", students.get(0).offers.get(0), offers.get(0));
    assertEquals("Offer-Student", offers.get(0).student, students.get(0));
    assertEquals("Student-Request", students.get(0).requests.get(0), requests.get(0));
    assertEquals("Request-Student", requests.get(0).student, students.get(0));
    assertEquals("Offer-Book", offers.get(0).book, books.get(0));
    assertEquals("Book-Offer", books.get(0).offer, offers.get(0));
    assertEquals("Request-Book", requests.get(0).book, books.get(0));
    assertEquals("Book-Request", books.get(0).request, requests.get(0));
  }

}
