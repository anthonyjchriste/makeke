package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import play.db.ebean.Model;

@Entity
public class Offer extends Model {
  private static final long serialVersionUID = 5043739071919179503L;

  @Id
  public Long id;
  
  @ManyToOne(cascade = CascadeType.ALL)
  public Student student;
  
  @OneToOne(cascade = CascadeType.ALL)
  public Book book;
  
  public String name;
  public String condition;
  public Long price;
  
  public Offer(String name, String condition, Long price) {
    this.name = name;
    this.condition = condition;
    this.price = price;
  }
  
  public static Finder<Long, Offer> find() {
    return new Finder<>(Long.class, Offer.class);
  }
  
}
