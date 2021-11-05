package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.entity.User;
import by.epamtc.stanislavmelnikov.runner.Runner;
import by.epamtc.stanislavmelnikov.service.serviceinterface.ClientService;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.factory.ServiceFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Authorization implements Command {
    @Override
    public String execute(String request) {
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        ClientService clientService = serviceFactory.getClientService();
        Logger logger = Runner.getLogger();
        String[] params = request.split(splitSymbol);
        String login = params[1];
        String password = params[2];
        try {
            User user = clientService.signIn(login, password);
            response = operationSuccessCode + user.getFirstName() + ", Welcome!";
        } catch (ServiceException e) {
            logger.log(Level.SEVERE, "authorization error, login: {0} \nException: {1}", new Object[]{login, e});
            response = operationFailCode + e.getMessage() + "\nTry again.";
        }
        return response;
    }
}
