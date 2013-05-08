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
 
package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides utilities for working with password hashes.
 * 
 * @author Anthony Christe
 */
public class Password {
  /**
   * Hashes a password using SHA-256.
   * @param password The plain-text ofthe password.
   * @return The hashed bytes of the password.
   */
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
