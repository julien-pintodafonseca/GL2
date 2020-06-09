package fr.ensimag.deca;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author Equipe GL2
 * @date 2020
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getBanner()) {
            List<String> banner = new ArrayList<>();
            banner.add("============================================= DecacCompiler =============================================");
            banner.add("                      _______            _                 ______ _       ______                         ");
            banner.add("                     | ______)          (_)               / _____) |     (_____ \\                       ");
            banner.add("                     | |___   ____ _   _ _ ____   ____   | /  ___| |       ____) )                       ");
            banner.add("                     |  ___) / _  | | | | |  _ \\ / _  )  | | (___) |      /_____/                       ");
            banner.add("                     | |____| | | | |_| | | | | ( (/ /   | \\____/| |_____| (____                        ");
            banner.add("                     |_______)_|| |\\____|_| ||_/ \\____)   \\_____/|_______|_______)                    ");
            banner.add("                                |_|       |_|                                                            ");
            banner.add("=========================================================================================================");

            for (String line : banner) {
                System.out.println(line);
            }
        }
        if (options.getSourceFiles().isEmpty() && !options.getBanner()) {
            options.displayUsage();
        }
        if (options.getParallel()) {
            List<Future<Boolean>> resultList = new ArrayList<Future<Boolean>>();
            int nbProcessors = Runtime.getRuntime().availableProcessors();
            ExecutorService executorService = Executors.newFixedThreadPool(nbProcessors);
            for (File source : options.getSourceFiles()){
                Future<Boolean> future = executorService.submit(new DecacCompiler(options,source));
                resultList.add(future);
            }
            for (Future<Boolean> fb: resultList){
                try{
                    if (fb.get()) {
                        error = true ;
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (ExecutionException e){
                    e.printStackTrace();
                }finally {
                    executorService.shutdown();
                }
            }

        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                LOG.debug("Begin of a task");
                if (compiler.compile()) {
                    error = true;
                }
                LOG.debug("End of a task");
            }
        }
        System.exit(error ? 1 : 0);
    }
}


