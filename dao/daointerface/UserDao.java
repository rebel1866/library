package by.epamtc.stanislavmelnikov.dao.daointerface;

import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.entity.User;

import java.util.List;

public interface UserDao<T> {
    T getUser(String login) throws DaoException;

    void writeUserInFile(T user) throws DaoException;

    void removeByLogin(String login) throws DaoException;
    List<String> getLogins() throws DaoException;

}
