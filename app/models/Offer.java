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
  public Long privateKey;
  
  @Required
  public String offerId;
  
  @Required
  @ManyToOne(cascade = CascadeType.ALL)
  public Student student;
  
  @Required
  @OneToOne(cascade = CascadeType.ALL)
  public Book book;
  
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
  
}
