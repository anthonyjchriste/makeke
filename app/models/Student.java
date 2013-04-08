package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import play.db.ebean.Model;

@Entity
public class Student extends Model {
  private static final long serialVersionUID = 3881518174354436477L;

  @Id
  public Long id;
  
  public String firstName;
  public String lastName;
  public String email;
  
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  List<Offer> offers = new ArrayList<>();
  
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  List<Request> requests = new ArrayList<>();
  
  public Student(String firstName, String lastName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }
  
  public static Finder<Long, Student> find() {
    return new Finder<>(Long.class, Student.class);
  }
  
}
