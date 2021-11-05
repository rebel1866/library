package by.epamtc.stanislavmelnikov.controller.commandimpl;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.entity.User;
import by.epamtc.stanislavmelnikov.runner.Runner;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.factory.ServiceFactory;
import by.epamtc.stanislavmelnikov.service.serviceinterface.ClientService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminRightsVerifier implements Command {
    @Override
    public String execute(String request) {
        String response;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        ClientService clientService = serviceFactory.getClientService();
        Logger logger = Runner.getLogger();
        try {
            clientService.verifyUserRights();
            response = operationSuccessCode;
        } catch (ServiceException e) {
            logger.log(Level.INFO, "Admin rights check is not passed \n Current admin status: {0}",
                    User.getCurrentUser().isAdmin());
            response = operationFailCode + e.getMessage();
        }
        return response;
    }
}

