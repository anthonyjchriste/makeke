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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * 
 * This class models an Request by a Student for a specific Book.
 * 
 * @author Anthony Christe
 */
@Entity
public class Request extends Model {
  private static final long serialVersionUID = -287973969048754947L;

  @Id
  private Long primaryKey;
  
  @Required
  private String requestId;
  
  @Required
  @ManyToOne(cascade = CascadeType.PERSIST)
  private Student student;
  
  @OneToOne(cascade = CascadeType.ALL)
  private Book book;
  
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

  public Long getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(Long primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
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
