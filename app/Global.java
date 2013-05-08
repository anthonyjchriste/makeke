

import java.text.ParseException;
import java.util.Locale;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.data.format.Formatters;

public class Global extends GlobalSettings {
  @Override
  public void onStart(Application app) {
    Logger.info("Performing onStart operations");
    
    Formatters.register(models.Student.class, new Formatters.SimpleFormatter<models.Student>() {
      @Override
      public models.Student parse(String text, Locale locale) throws ParseException {
        models.Student student = models.Student.find().where().eq("studentId", text).findUnique();
        if (student == null) {
          Logger.warn("Could not find matching Student");
        }
        return student;
      }

      @Override
      public String print(models.Student t, Locale locale) {
        return t.getStudentId();
      }
    });
    
    Formatters.register(models.Book.class, new Formatters.SimpleFormatter<models.Book>() {
      @Override
      public models.Book parse(String text, Locale locale) throws ParseException {
        models.Book book = models.Book.find().where().eq("bookId", text).findUnique();
        if (book == null) {
          Logger.warn("Could not find matching Warehouse");
        }
        return book;
      }

      @Override
      public String print(models.Book t, Locale locale) {
        return t.getBookId();
      }
    });
  }
}
