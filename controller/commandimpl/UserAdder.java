package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.runner.Runner;
import by.epamtc.stanislavmelnikov.service.serviceinterface.ClientService;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.factory.ServiceFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserAdder implements Command {
    @Override
    public String execute(String request) {
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        ClientService clientService = serviceFactory.getClientService();
        int splitIndex = request.indexOf(splitSymbol);
        request = request.substring(++splitIndex);
        Logger logger = Runner.getLogger();
        String[] params = request.split(splitSymbol);
        try {
            clientService.addUser(params);
            response = operationSuccessCode + "User has been added";
        } catch (ServiceException e) {
            response = operationFailCode + e.getMessage() + "\nTry again";
            logger.log(Level.INFO, "User adding failure: {0}\n" +
                    "request: {1} response: {2}", new Object[]{e, request, response});
        }
        return response;
    }
}
