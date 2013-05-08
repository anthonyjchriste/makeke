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

import java.util.List;
import play.mvc.Controller;
import play.mvc.Result;
import utils.User;
import views.html.*;

/**
 * Renders views which display all current requests and offers for the logged in user.
 * 
 * @author Anthony Christe
 */
public class RequestsAndOffers extends Controller {

  /**
   * Render the manager so that it displays all requests and offers for the current logged in user.
   * 
   * @return A rendered manager page.
   */
  public static Result index() {
    models.Student student = User.getStudent();
    if (student == null) {
      return ok(error.render("You must be logged in to do that.",
          "Please login or create an account."));
    }

    List<models.Request> requests = student.getRequests();
    List<models.Offer> offers = student.getOffers();
    return ok(requestsAndOffersManage.render(requests, offers));
  }
}
