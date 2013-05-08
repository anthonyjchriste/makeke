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

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * Controller for the default home page of Makeke.
 * 
 * @author Anthony Christe
 */
public class Application extends Controller {
  /**
   * Renders the home page.
   * 
   * @return The rendered home page.
   */
  public static Result index() {
    return ok(index.render());
  }
}
