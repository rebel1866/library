package by.epamtc.stanislavmelnikov.service.serviceinterface;

import by.epamtc.stanislavmelnikov.entity.User;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;

import java.util.Map;

public interface ClientService {
    User signIn(String login, String password) throws ServiceException;

    void addUser(String... params) throws ServiceException;

    void verifyUserRights() throws ServiceException;

    void removeByLogin(String request) throws ServiceException;

    void updateByLogin(Map<String, String> args) throws ServiceException;

    void signOut();
}
