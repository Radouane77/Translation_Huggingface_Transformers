/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NLPTranslation;

/**
 *
 * @author Mouad AITALI
 */
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Mouad AITALI
 */
public class Common {
    public static void initializeFirebase() throws FileNotFoundException, IOException {
        FileInputStream refreshToken = new FileInputStream("credentials.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .setDatabaseUrl("https://nlp-transformers-helper-default-rtdb.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }
}
