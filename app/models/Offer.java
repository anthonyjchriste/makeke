package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Offer extends Model {
  private static final long serialVersionUID = 5043739071919179503L;

  @Id
  private Long primaryKey;

  @Required
  private String offerId;
  
  @Required
  @ManyToOne(cascade = CascadeType.ALL)
  private Student student;
  
  @Required
  @OneToOne(cascade = CascadeType.ALL)
  private Book book;
  
  public Offer(String offerId, Book book) {
    this.offerId = offerId;
    this.book = book;
  }
  
  public static Finder<Long, Offer> find() {
    return new Finder<>(Long.class, Offer.class);
  }
  
  @Override
  public String toString() {
    return String.format("[Offer %s]", this.offerId);
  }
  
  public Long getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(Long primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getOfferId() {
    return offerId;
  }

  public void setOfferId(String offerId) {
    this.offerId = offerId;
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
