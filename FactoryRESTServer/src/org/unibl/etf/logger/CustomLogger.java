package org.unibl.etf.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CustomLogger {
	
	private static final String PATH_LOG_KORISNIK="C:\\Users\\HP\\eclipse-workspace\\ClientApp\\src\\org\\unibl\\etf\\logger\\logs\\KorisnikApp.log";
	private static final String PATH_LOG_FABRIKA="C:\\Users\\HP\\eclipse-workspace\\FactoryRESTServer\\src\\org\\unibl\\etf\\logger\\logs\\FabrikaApp.log";
	private static final String PATH_LOG_DISTRIBUTER="C:\\Users\\HP\\eclipse-workspace\\DistributerApp\\src\\org\\unibl\\etf\\logger\\logs\\DistributerApp.log";
    private static final Logger logger = Logger.getLogger(CustomLogger.class.getName());
    
    private static FileHandler fabrikaFileHandler;
    private static FileHandler korisnikFileHandler;
    private static FileHandler distributerFileHandler;

    static {
        try {
           
        	 fabrikaFileHandler = new FileHandler(PATH_LOG_FABRIKA);
             fabrikaFileHandler.setFormatter(new SimpleFormatter());

             korisnikFileHandler = new FileHandler(PATH_LOG_KORISNIK);
             korisnikFileHandler.setFormatter(new SimpleFormatter());

             distributerFileHandler = new FileHandler(PATH_LOG_DISTRIBUTER);
             distributerFileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setFabrikaLogging() {
        logger.addHandler(fabrikaFileHandler);
    }
    
    public static void setKorisnikLogging() {
        logger.addHandler(korisnikFileHandler);
    }
    
    public static void setDistributerLogging() {
        logger.addHandler(distributerFileHandler);
    }
    public static void logInfo(String message) {
        logger.log(Level.INFO, message);
    }

    public static void logWarning(String message) {
        logger.log(Level.WARNING, message);
    }

    public static void logError(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }
}