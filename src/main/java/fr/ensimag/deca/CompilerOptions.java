package fr.ensimag.deca;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User-specified options influencing the compilation.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<>();

    
    public void parseArgs(String[] args) throws CLIException {
        // A FAIRE : parcourir args pour positionner les options correctement.
        for (String arg : args) {
            if (arg.startsWith("-")) {
                // Gestion des options
                switch(arg) {
                    case "-b":
                    case "-p":
                    case "-v":
                    case "-n":
                    case "-r":
                    case "-d":
                    case "-P":
                        throw new UnsupportedOperationException("not yet implemented");
                    default:
                        throw new CLIException(ErrorMessages.DECAC_COMPILER_WRONG_OPTION + arg);
                }
            } else if (arg.endsWith(".deca")) {
                // Gestion du fichier d'entrée
                File file = new File(arg);
                if (file.exists()) {
                    if (!sourceFiles.contains(file)) {
                        sourceFiles.add(file);
                    }
                } else {
                    throw new CLIException(ErrorMessages.DECAC_COMPILER_WRONG_FILE + arg);
                }
            } else {
                throw new CLIException(ErrorMessages.DECAC_COMPILER_WRONG_ENTRY + arg);
            }
        }

        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
    }

    protected void displayUsage() {
        List<String> help = new ArrayList<>();
        help.add("============================================= DecacCompiler =============================================");
        help.add("La syntaxe d'utilisation de l'exécutable decac est la suivante :");
        help.add("decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]");
        help.add("=========================================================================================================");
        help.add("-b   (banner)       : affiche une banière indiquant le nom de l'équipe");
        help.add("-p   (parse)        : arrête decac après l'étape de construction de l'arbre, et affiche la décompilation");
        help.add("-v   (verification) : arrête decac après l'étape de vérification");
        help.add("-n   (no check)     : supprime les tests de débordement à l'exécution");
        help.add("                       - débordement arithmétique");
        help.add("                       - débordement mémoire");
        help.add("                       - déréférencement de null");
        help.add("-r X (registrers)   : limites les registres banalisés disponibles à R0 ... R{X-1}, avec 4 <= X <= 16");
        help.add("-d   (debug)        : active les traces de bug. Répéter l'option plusieurs fois pour avoir plus de traces");
        help.add("-P   (parallel)     : s'il y a plusieurs fichiers sources, lance la compilation des fichiers en parallèle");
        help.add("                      (pour accélérer la compilation)");

        for (String line : help) {
            System.out.println(line);
        }
    }
}
