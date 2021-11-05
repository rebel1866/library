package by.epamtc.stanislavmelnikov.service.serviceimpl;

import by.epamtc.stanislavmelnikov.dao.daointerface.BookDao;
import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.dao.factory.DaoFactory;
import by.epamtc.stanislavmelnikov.entity.Book;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.serviceinterface.LibraryService;

import java.util.List;
import java.util.Map;

public class LibraryServiceImpl implements LibraryService {
    @Override
    public void addBook(String[] params) throws ServiceException {
        String name = params[0];
        String author = params[1];
        String yearStr = params[2];
        String amountPagesStr = params[3];
        int year = checkIntegerCorrectness(yearStr, "Incorrect year");
        int amountPages = checkIntegerCorrectness(amountPagesStr, "Incorrect amount pages");
        int id = generateId();
        Book book = new Book(name, author, year, amountPages, id);
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        try {
            bookDao.writeBookInSource(book);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public String viewAllBooks() throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        List<Book> bookList;
        try {
            bookList = bookDao.getBooks("ALL", "");
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        if (bookList.size() == 0) throw new ServiceException("No books in the library");
        return formatBooks(bookList);
    }

    @Override
    public String searchByCriteria(String criteria, String request) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        List<Book> bookList;
        try {
            bookList = bookDao.getBooks(criteria, request);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        if (bookList.size() == 0) throw new ServiceException("No books found");
        return formatBooks(bookList);
    }


    @Override
    public void removeById(String id) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        try {
            bookDao.removeById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void updateById(Map<String, String> request) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        String idStr = request.get("id");
        if (idStr == null) throw new ServiceException("You haven't entered id.");
        String name = request.get("name");
        String author = request.get("author");
        String yearStr = request.get("year");
        String pagesStr = request.get("pages");
        try {
            Book book = (Book) bookDao.getBooks("id", idStr).get(0);
            setBookFields(book, author, name, yearStr, pagesStr);
            bookDao.removeById(idStr);
            bookDao.writeBookInSource(book);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void setBookFields(Book book, String author,
                              String name, String yearStr, String pagesStr) throws ServiceException {

        if (yearStr != null) {
            int year = checkIntegerCorrectness(yearStr, "Incorrect year");
            book.setYear(year);
        }
        if (pagesStr != null) {
            int pages = checkIntegerCorrectness(pagesStr, "Incorrect input of pages amount");
            book.setAmountPages(pages);
        }
        if (name != null) {
            book.setName(name);
        }
        if (author != null) {
            book.setAuthor(author);
        }
    }

    public String formatBooks(List<Book> bookList) {
        StringBuilder books = new StringBuilder();
        for (Book book : bookList) {
            books.append("ID: " + book.getId() + "\n");
            books.append("Book name: " + book.getName() + "\n");
            books.append("Book author: " + book.getAuthor() + "\n");
            books.append("Book year issue: " + book.getYear() + "\n");
            books.append("Book amount of pages: " + book.getAmountPages() + "\n\n");
        }
        return books.toString();
    }

    public int checkIntegerCorrectness(String str, String exceptionMessage) throws ServiceException {
        int value;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new ServiceException(exceptionMessage, e);
        }
        return value;
    }

    public int generateId() throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        BookDao bookDao = factory.getBookDao();
        List<Integer> listID;
        try {
            listID = bookDao.getAllID();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        int maxIDValue = Integer.MAX_VALUE;
        for (int id = 1; id < maxIDValue; id++) {
            if (listID == null) return id;
            if (!listID.contains(id)) {
                return id;
            }
        }
        throw new ServiceException("Fail in ID generating");
    }
}
