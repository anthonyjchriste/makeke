package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Book extends Model {
  private static final long serialVersionUID = 4980072665954738235L;

  @Id
  public Long primaryKey;
  
  
  @Required
  public String bookId;
  
  @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
  public Request request;
  
  @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
  public Offer offer;
  
  public String name;
  public String condition;
  public String isbn;
  public Long price;
  
  public Book(String bookId, String name, String condition, String isbn, Long price) {
    this.bookId = bookId;
    this.name = name;
    this.condition = condition;
    this.isbn= isbn;
    this.price = price;
  }
  
  public static Finder<Long, Book> find() {
    return new Finder<>(Long.class, Book.class);
  }
  
  @Override
  public String toString() {
    return String.format("[Book %s %s %s %s %d]", this.bookId, this.name, this.condition,
        this.isbn, this.price);
  }
}
