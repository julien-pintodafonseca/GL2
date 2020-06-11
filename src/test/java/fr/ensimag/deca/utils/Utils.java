package fr.ensimag.deca.utils;

import java.util.Arrays;
import java.util.List;

/**
 *
 *  @author Equipe GL2
 *  @date 2020
 */
public class Utils {
    public static List<String> normalizeDisplay(String str) {
        str = str.replaceAll("\t","")
                .replaceAll("\\r\\n", "\n") // for windows
                .replaceAll("\\r", "\n");   // for linux
        return Arrays.asList(str.split("\n"));

    }
}


