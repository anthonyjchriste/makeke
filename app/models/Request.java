package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import play.db.ebean.Model;

@Entity
public class Request extends Model {
  private static final long serialVersionUID = -287973969048754947L;

  @Id
  public Long id;
  
  @ManyToOne(cascade = CascadeType.ALL)
  public Student student;
  
  @ManyToMany(cascade = CascadeType.ALL)
  public List<Book> books = new ArrayList<>();
  
  public String name;
  public String condition;
  public Long price;
  
  public Request(String name, String condition, Long price) {
    this.name = name;
    this.condition = condition;
    this.price = price;
  }
  
  public static Finder<Long, Request> find() {
    return new Finder<>(Long.class, Request.class);
  }
  
}
