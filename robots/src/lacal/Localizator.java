package lacal;

import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class allow to get string from properties in every place of program.
 */
public class Localizator {
    private static Locale locale = extractLocale();

    private Localizator() {}

    public static ResourceBundle getLangBundle() {
        return ResourceBundle.getBundle("lang", locale);
    }

    public static void switchLocale() {
        if (locale.toString().equals("ru_RU")) {
            writeLocale("tr_RU");
        } else {
            writeLocale("ru_RU");
        }
    }

    private static void writeLocale(String locale) {
        try (FileWriter writer = new FileWriter(new File(System.getProperty("user.home"), "config.conf"))) {
            writer.write(locale);
        } catch (IOException ignored) {}
    }

    private static Locale extractLocale() {
        File file = new File(System.getProperty("user.home"), "config.conf");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String[] locale = reader.readLine().split("_");
            return new Locale(locale[0], locale[1]);
        } catch (IOException ignored) {
            return new Locale("ru", "RU");
        }
    }
}
