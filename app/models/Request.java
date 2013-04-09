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
  public Long primaryKey;
  
  @Required
  public String requestId;
  
  @Required
  @ManyToOne(cascade = CascadeType.ALL)
  public Student student;
  
  @Required
  @OneToOne(cascade = CascadeType.ALL)
  public Book book;
  
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
  
}
