package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.runner.Runner;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.factory.ServiceFactory;
import by.epamtc.stanislavmelnikov.service.serviceinterface.ClientService;
import by.epamtc.stanislavmelnikov.service.serviceinterface.LibraryService;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserUpdater implements Command {
    @Override
    public String execute(String request) {
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        ClientService clientService = serviceFactory.getClientService();
        int splitIndex = request.indexOf(splitSymbol);
        request = request.substring(++splitIndex);
        Logger logger = Runner.getLogger();
        String[] params = request.split(splitSymbol);
        Map<String, String> args = new HashMap<>();
        try {
            for (int i = 0; i < params.length; i++) {
                String[] keyValue = params[i].split("=");
                args.put(keyValue[0], keyValue[1]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.log(Level.INFO, "index out of bound error", e);
            response = operationFailCode + "Wrong input";
            return response;
        }
        try {
            clientService.updateByLogin(args);
            response = operationSuccessCode + "Operation is done";
        } catch (ServiceException e) {
            logger.log(Level.INFO, "User updating failure", e);
            response = operationFailCode + e.getMessage();
        }
        return response;
    }
}
