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

package controllers;

import static play.data.Form.form;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import utils.User;
import views.html.bookexplorer;
import views.html.error;

/**
 * Manages the views and models of a Book entity. This class also aims to provide filtering
 * capabilities.
 * 
 * It should be noted that books are created and deleted through their corresponding requests and
 * offers.
 * 
 * @author Anthony Christe
 */
public class Book extends Controller {
  /**
   * Renders the Book Explorer view. The Book Explorer will display all known books.
   * 
   * @return The rendered book explorer.
   */
  public static Result index() {
    if (User.getStudent() == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    List<models.Book> books = models.Book.find().findList();
    return ok(bookexplorer.render(books));
  }

  /**
   * Filters the current set of books displayed based on search parameters. This currently only
   * supports filtering books by words in their titles.
   * 
   * @return A rendered Book Explorer page only displaying filtered books.
   */
  public static Result filter() {
    Set<models.Book> bookSet = new HashSet<>();
    DynamicForm searchForm = form().bindFromRequest();

    // Find the books that contain all of the words in the title field.
    for (String nameWord : searchForm.get("name").split(" ")) {
      bookSet.addAll(models.Book.find().where().contains("name", nameWord).findSet());
    }

    return ok(bookexplorer.render(new ArrayList<>(bookSet)));
  }
}
