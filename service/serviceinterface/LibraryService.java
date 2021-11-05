package by.epamtc.stanislavmelnikov.service.serviceinterface;

import by.epamtc.stanislavmelnikov.service.exception.ServiceException;

import java.util.Map;

public interface LibraryService {
    void addBook(String[] params) throws ServiceException;

    String viewAllBooks() throws ServiceException;

    String searchByCriteria(String criteria, String request) throws ServiceException;

    void removeById(String request) throws ServiceException;

    void updateById(Map<String, String> request) throws ServiceException;
}
