package bank.entity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    private final String username;
    private final String passwordHash;
    private final String role;
    private final String customerId; // null for staff users who are not customers

    /** Creates a staff user (no customer record) from a plain password, which is hashed straight away. */
    public User(String username, String plainPassword, String role) {
        this(username, plainPassword, role, null);
    }

    /** Creates a user linked to a customer. The password is hashed straight away. */
    public User(String username, String plainPassword, String role, String customerId) {
        this(username, hash(plainPassword), role, customerId, true);
    }

    private User(String username, String passwordHash, String role, String customerId, boolean alreadyHashed) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        this.username = username.trim();
        this.passwordHash = passwordHash;
        this.role = role;
        this.customerId = customerId;
    }

    /** Rebuilds a user from a hash that was saved earlier (used when loading from the database). */
    public static User withHash(String username, String passwordHash, String role, String customerId) {
        return new User(username, passwordHash, role, customerId, true);
    }

    public boolean verifyPassword(String password) {
        return password != null && !password.isEmpty() && passwordHash.equals(hash(password));
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    private static String hash(String plain) {
        if (plain == null || plain.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(plain.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
