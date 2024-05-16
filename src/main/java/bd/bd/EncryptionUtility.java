package bd.bd;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.AES256TextEncryptor;

public class EncryptionUtility {
    private static final String SECRET_KEY = "scr";

    public static String encrypt(String data) {
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(SECRET_KEY);
        return textEncryptor.encrypt(data);
    }

    public static String decrypt(String encryptedData) {
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(SECRET_KEY);
        try {
            return textEncryptor.decrypt(encryptedData);
        } catch (EncryptionOperationNotPossibleException e) {
            return encryptedData;
        }
    }
}
