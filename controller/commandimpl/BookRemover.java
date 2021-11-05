package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.runner.Runner;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.factory.ServiceFactory;
import by.epamtc.stanislavmelnikov.service.serviceinterface.LibraryService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BookRemover implements Command {
    @Override
    public String execute(String request) {
        Logger logger = Runner.getLogger();
        logger.log(Level.INFO, "request: {0}", request);
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        LibraryService libraryService = serviceFactory.getLibraryService();
        int splitIndex = request.indexOf(splitSymbol);
        request = request.substring(++splitIndex);
        try {
            libraryService.removeById(request);
            response = operationSuccessCode + "Operation is done";
        } catch (ServiceException e) {
            logger.log(Level.INFO, "Book deleting failure", e);
            response = operationFailCode + e.getMessage();
        }
        return response;
    }
}
