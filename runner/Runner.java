package by.epamtc.stanislavmelnikov.runner;

import by.epamtc.stanislavmelnikov.controller.executor.Executor;
import by.epamtc.stanislavmelnikov.dao.daoimpl.SqlBookDao;
import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.dao.factory.DaoFactory;
import by.epamtc.stanislavmelnikov.entity.Book;
import by.epamtc.stanislavmelnikov.entity.User;
import by.epamtc.stanislavmelnikov.view.menu.IMenu;
import by.epamtc.stanislavmelnikov.view.menu.Menu;
import by.epamtc.stanislavmelnikov.view.requestgenerator.RequestGenerator;
import by.epamtc.stanislavmelnikov.view.input.DataScanner;
import by.epamtc.stanislavmelnikov.view.output.ConsoleOutput;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Runner {
    private static final Logger logger =
            Logger.getLogger(Runner.class.getName());

    public static Logger getLogger() {
        return logger;
    }

    public static void main(String[] args) {
        ConsoleOutput consoleOutput = new ConsoleOutput();
        DataScanner dataScanner = new DataScanner();
        Executor executor = new Executor();
        RequestGenerator requestGenerator = new RequestGenerator(consoleOutput, dataScanner, executor);
        try (FileInputStream input = new FileInputStream("log.config")) {
            LogManager.getLogManager().readConfiguration(input);
        } catch (IOException e) {
            consoleOutput.printMessage(e.getMessage());
        }
        requestGenerator.doSignInRequest();
        IMenu menu = new Menu(requestGenerator, consoleOutput, dataScanner);
        menu.createMenu();
        menu.runMenu();
    }
}
