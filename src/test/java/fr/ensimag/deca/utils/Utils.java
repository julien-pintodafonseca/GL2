package fr.ensimag.deca.utils;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Utils {
    public static String normalizeDisplay(String str) {
        return str.replace("\t","")
                .replace("\r", "")
                .replace("\n", "");
    }
}
