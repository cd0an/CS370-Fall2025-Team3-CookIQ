package cookiq.TestCode;

import cookiq.db.UserRepository;

public class AuthTest {
    public static void main(String[] args) {
        UserRepository repo = new UserRepository();

        // --- Register test users ---
        System.out.println("Register user1: " + repo.registerUser("miguel", "password123"));
        System.out.println("Register user2: " + repo.registerUser("ryan", "secure456"));

        // --- Test authentication ---
        System.out.println("Login (correct password): " + repo.authenticateUser("miguel", "password123"));
        System.out.println("Login (wrong password):   " + repo.authenticateUser("miguel", "wrong"));
        System.out.println("Login (nonexistent user): " + repo.authenticateUser("ghost", "anything"));
    }
}

