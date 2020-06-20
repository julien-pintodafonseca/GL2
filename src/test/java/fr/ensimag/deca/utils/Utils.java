package fr.ensimag.deca.utils;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Utils {
    public static List<String> normalizeDisplay(String str) {
        str = normalizeString(str);
        return Arrays.asList(str.split("\n"));
    }

    public static String normalizeString(String str) {
        str = str.replaceAll("\t","")
                .replaceAll("\\r\\n", "\n") // fix carriage return
                .replaceAll("\\r", "\n");   // for windows & linux
        return str;
    }
}
