/*
 * This file is part of Makeke.
 *
 *  Makeke is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Makeke is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Makeke.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Copyright (C) Anthony Christe 2013 
 */
 
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
import utils.Password;

/**
 * Models a Student which can create many Offers and Requests.
 * 
 * @author Anthony Christe
 */
@Entity
public class Student extends Model {
  private static final long serialVersionUID = 3881518174354436477L;

  @Id
  private Long primaryKey;
  
  @Required
  private String studentId;
  
  @Required
  private String firstName;
  
  @Required
  private String lastName;
  
  @Email
  private String email;
  
  
  private String password;
  
  @Required
  private byte[] passwordHash;
  
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  private List<Offer> offers = new ArrayList<>();
  
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
  private List<Request> requests = new ArrayList<>();
  
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
    return String.format("%s %s (%s)", this.getFirstName(), this.getLastName(), this.getEmail());
  }

  public Long getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(Long primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
  
  public void setPassword(String password) {
    this.passwordHash = Password.hash(password);
    this.password = "";
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public byte[] getPasswordHash() {
    return this.passwordHash;
  }

  public List<Offer> getOffers() {
    return offers;
  }

  public void setOffers(List<Offer> offers) {
    this.offers = offers;
  }

  public List<Request> getRequests() {
    return requests;
  }

  public void setRequests(List<Request> requests) {
    this.requests = requests;
  }
}
