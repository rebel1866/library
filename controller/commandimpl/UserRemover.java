package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.runner.Runner;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.factory.ServiceFactory;
import by.epamtc.stanislavmelnikov.service.serviceinterface.ClientService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRemover implements Command {
    @Override
    public String execute(String request) {
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        ClientService clientService = serviceFactory.getClientService();
        int splitIndex = request.indexOf(splitSymbol);
        request = request.substring(++splitIndex);
        Logger logger = Runner.getLogger();
        try {
            clientService.removeByLogin(request);
            response = operationSuccessCode + "User has been removed";
        } catch (ServiceException e) {
            response = operationFailCode + e.getMessage() + "\nTry again";
            logger.log(Level.INFO, "User removing failure: {0}\n" +
                    "request: {1} response: {2}", new Object[]{e, request, response});
        }
        return response;
    }
}
