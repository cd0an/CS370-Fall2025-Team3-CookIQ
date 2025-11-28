/** 
 * PasswordUtils.java
 * 
 * Provides utility methods for handling passwords securely.
 * Includes methods for hashing passwords using SHA-256 and safe comparison of hashed strings.
 * ChatGPT was used to help implement this class.
 * 
 */

package cookiq.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtils {
    // Private constructor to prevent instantiation
    private PasswordUtils() {}

    // Computes the SHA-256 hash of the given password
    public static String sha256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // Get SHA-256 MessageDigest instance
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8)); // Compute the hash as a byte array 
            StringBuilder sb = new StringBuilder(bytes.length * 2); // Convert byte array to hex string
            for (byte b : bytes) {
                sb.append(String.format("%02x", b)); // Formate each byte as two hex digits
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    // Compares two strings in a way that mitigates timing attacks
    public static boolean slowEquals(String a, String b) {
        if (a == null || b == null) return false;
        int diff = a.length() ^ b.length(); // Start with difference in lengths
        // Compare each character; accumulate differences
        for (int i = 0; i < Math.min(a.length(), b.length()); i++) {
            diff |= a.charAt(i) ^ b.charAt(i);
        }
        return diff == 0; // If diff is zero, strings are equal
    }
}
