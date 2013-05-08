package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Request extends Model {
  private static final long serialVersionUID = -287973969048754947L;

  @Id
  private Long primaryKey;
  
  @Required
  private String requestId;
  
  @Required
  @ManyToOne(cascade = CascadeType.PERSIST)
  private Student student;
  
  @OneToOne(cascade = CascadeType.ALL)
  private Book book;
  
  public Request(String requestId, Book book) {
    this.requestId = requestId;
    this.book = book;
  }
  
  public static Finder<Long, Request> find() {
    return new Finder<>(Long.class, Request.class);
  }
  
  @Override
  public String toString() {
    return String.format("[Request %s]", this.requestId);
  }

  public Long getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(Long primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }
  
}
