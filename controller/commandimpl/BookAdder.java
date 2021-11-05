package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.runner.Runner;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.factory.ServiceFactory;
import by.epamtc.stanislavmelnikov.service.serviceinterface.LibraryService;
import org.w3c.dom.Element;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BookAdder implements Command {
    @Override
    public String execute(String request) {
        Logger logger = Runner.getLogger();
        logger.log(Level.INFO, "request: {0}", request);
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        LibraryService libraryService = serviceFactory.getLibraryService();
        int splitIndex = request.indexOf(splitSymbol);
        request = request.substring(++splitIndex);
        String[] params = request.split(splitSymbol);
        try {
            libraryService.addBook(params);
            response = operationSuccessCode + "Book has been added";
        } catch (ServiceException e) {
            response = operationFailCode + e.getMessage() + "\nTry again";
            logger.log(Level.INFO, "Book adding exception \n Exception: {0}" +
                    "Request: {1} Response: {2}", new Object[]{e, request, response});
        }
        return response;
    }
}

