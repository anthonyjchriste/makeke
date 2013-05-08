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

import java.text.ParseException;
import java.util.Locale;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.data.format.Formatters;

/**
 * Runs custom formatter registers on application start.
 * 
 * @author Anthony Christe
 */
public class Global extends GlobalSettings {
  /**
   * Registers a custom formatter for a Student entity.
   */
  @Override
  public void onStart(Application app) {
    Logger.info("Performing onStart operations");
    
    Formatters.register(models.Student.class, new Formatters.SimpleFormatter<models.Student>() {
      /**
       * Matches a student based on the studentId.
       */
      @Override
      public models.Student parse(String text, Locale locale) throws ParseException {
        models.Student student = models.Student.find().where().eq("studentId", text).findUnique();
        if (student == null) {
          Logger.warn("Could not find matching Student");
        }
        return student;
      }
      
      /**
       * Prints a student object as the studentId.
       */
      @Override
      public String print(models.Student t, Locale locale) {
        return t.getStudentId();
      }
    });
    
    Formatters.register(models.Book.class, new Formatters.SimpleFormatter<models.Book>() {
      /**
       * Matches a book on the bookId.
       */
      @Override
      public models.Book parse(String text, Locale locale) throws ParseException {
        models.Book book = models.Book.find().where().eq("bookId", text).findUnique();
        if (book == null) {
          Logger.warn("Could not find matching Warehouse");
        }
        return book;
      }
      
      /**
       * Prints a book object as the bookId.
       */
      @Override
      public String print(models.Book t, Locale locale) {
        return t.getBookId();
      }
    });
  }
}
