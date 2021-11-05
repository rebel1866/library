package by.epamtc.stanislavmelnikov.dao.factory;

import by.epamtc.stanislavmelnikov.dao.daointerface.BookDao;
import by.epamtc.stanislavmelnikov.dao.daointerface.UserDao;
import by.epamtc.stanislavmelnikov.dao.daoimpl.XmlBookDao;
import by.epamtc.stanislavmelnikov.dao.daoimpl.XmlUserDao;

public class DaoFactory {
    private static final DaoFactory instance = new DaoFactory();

    private UserDao userDao = new XmlUserDao();
    private BookDao bookDao = new XmlBookDao();

    private DaoFactory() {

    }

    public static DaoFactory getInstance() {
        return instance;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public BookDao getBookDao() {
        return bookDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }
}
