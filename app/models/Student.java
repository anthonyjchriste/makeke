package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Student extends Model {
  private static final long serialVersionUID = 3881518174354436477L;

  @Id
  public Long primaryKey;
  
  @Required
  public String studentId;
  
  @Required
  public String firstName;
  
  @Required
  public String lastName;
  
  @Email
  public String email;
  
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  public List<Offer> offers = new ArrayList<>();
  
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  public List<Request> requests = new ArrayList<>();
  
  public Student(String studentId, String firstName, String lastName, String email) {
    this.studentId = studentId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }
  
  public static Finder<Long, Student> find() {
    return new Finder<>(Long.class, Student.class);
  }
  
  @Override
  public String toString() {
    return String.format("[Student %s %s %s %s]", this.studentId, this.firstName, this.lastName,
        this.email);
  }
  
}
