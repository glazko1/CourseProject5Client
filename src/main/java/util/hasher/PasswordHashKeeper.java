package util.hasher;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PasswordHashKeeper {

    private static final PasswordHashKeeper INSTANCE = new PasswordHashKeeper();

    public static PasswordHashKeeper getInstance() {
        return INSTANCE;
    }

    private PasswordHashKeeper() {}

    public String generateHash(String username, String password) {
        byte[] salt = username.getBytes();
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] encoded = factory.generateSecret(spec).getEncoded();
            return new String(encoded, UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException();
    }
}