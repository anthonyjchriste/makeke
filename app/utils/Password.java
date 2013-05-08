package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
  public static byte[] hash(String password) {
      try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes());
        return digest.digest();
      }
      catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return new byte[0];
      }
    
  }
}
