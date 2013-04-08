package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import play.db.ebean.Model;

@Entity
public class Book extends Model {
  private static final long serialVersionUID = 4980072665954738235L;

  @Id
  public Long id;
  
  @ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
  public List<Request> requests = new ArrayList<>();
  
  @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
  public Offer offer;
  
  public String name;
  public String condition;
  public String isbn;
  public Long price;
  
  public Book(String name, String condition, String isbn, Long price) {
    this.name = name;
    this.condition = condition;
    this.isbn= isbn;
    this.price = price;
  }
  
  public static Finder<Long, Book> find() {
    return new Finder<>(Long.class, Book.class);
  }
}
