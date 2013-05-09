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
import javax.persistence.OneToOne;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Models a book that can be requested or offered.
 * 
 * @author Anthony Christe
 */
@Entity
public class Book extends Model {
  private static final long serialVersionUID = 4980072665954738235L;

  @Id
  private Long primaryKey;

  @Required
  private String bookId;

  @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
  private Request request;

  @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
  private Offer offer;

  private String name;
  private String condition;
  private String isbn;
  private String edition;
  private Long price;

  public Book(String bookId, String name, String edition, String condition, String isbn, Long price) {
    this.bookId = bookId;
    this.name = name;
    this.edition = edition;
    this.condition = condition;
    this.isbn = isbn;
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

  public Long getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(Long primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  public Offer getOffer() {
    return offer;
  }

  public void setOffer(Offer offer) {
    this.offer = offer;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEdition() {
    return edition;
  }

  public void setEdition(String edition) {
    this.edition = edition;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }
}
