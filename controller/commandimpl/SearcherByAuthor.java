package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.runner.Runner;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.factory.ServiceFactory;
import by.epamtc.stanislavmelnikov.service.serviceinterface.LibraryService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SearcherByAuthor implements Command {
    @Override
    public String execute(String request) {
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        LibraryService libraryService = serviceFactory.getLibraryService();
        int splitIndex = request.indexOf(splitSymbol);
        request = request.substring(++splitIndex);
        Logger logger = Runner.getLogger();
        try {
            response = operationSuccessCode + "Search result:\n" + libraryService.searchByCriteria("author", request);
        } catch (ServiceException e) {
            logger.log(Level.INFO, "Searching by author failed", e);
            response = operationFailCode + e.getMessage();
        }
        return response;

    }
}
