package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author @AUTHOR@
 * @date @DATE@
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
        if (args.length == 0) {
            displayUsage();
        } else {
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
                    // String path = Paths.get(".").toAbsolutePath().normalize().toString();
                    // System.out.println(path);
                    // System.out.println(arg);
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
        throw new UnsupportedOperationException("not yet implemented");
    }
}
