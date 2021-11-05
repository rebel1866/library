package by.epamtc.stanislavmelnikov.dao.daoimpl;

import by.epamtc.stanislavmelnikov.dao.daointerface.BookDao;
import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlBookDao implements BookDao<Book> {
    private Connection connection;
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/books";
    private static String user = "root";
    private static String password = "password";
    private static final String APOSTROPHE = "'";
    private static final String COMMA = ",";
    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";
    private static final String SEMICOLON = ";";


    public static void setUrl(String url) {
        SqlBookDao.url = url;
    }

    public static void setUser(String user) {
        SqlBookDao.user = user;
    }

    public static void setPassword(String password) {
        SqlBookDao.password = password;
    }

    public static void setDriverName(String driverName) {
        SqlBookDao.driverName = driverName;
    }

    public void connectToDatabase() throws DaoException {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DaoException("Exception while trying to connect database", e);
        }
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            throw new DaoException("Exception. No JDBC driver");
        }
    }

    @Override
    public void writeBookInSource(Book book) throws DaoException {
        String sql = "INSERT book (id, bookname, author, bookyear, pages)" +
                "VALUES" + OPEN_BRACKET + book.getId() + COMMA + APOSTROPHE + book.getName() + APOSTROPHE + COMMA +
                APOSTROPHE + book.getAuthor() + APOSTROPHE + COMMA + book.getYear() + COMMA + book.getAmountPages()
                + CLOSE_BRACKET + SEMICOLON;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DaoException("Exception trying write book in database", e);
        }
    }

    @Override
    public List<Book> getBooks(String criteria, String request) throws DaoException {
        String query = generateSqlQuery(criteria, request);
        List<Book> bookList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            try (ResultSet result = statement.executeQuery(query)) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int year = result.getInt("bookyear");
                    int pages = result.getInt("pages");
                    String name = result.getString("bookname");
                    String author = result.getString("author");
                    Book book = new Book(name, author, year, pages, id);
                    bookList.add(book);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Exception while trying to get books from database", e);
        }
        return bookList;
    }

    @Override
    public void removeById(String id) throws DaoException {
        String query = "DELETE FROM book WHERE id=" + id;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new DaoException("Exception in trying to remove book from database", e);
        }
    }

    @Override
    public List<Integer> getAllID() throws DaoException {
        String query = "SELECT id FROM book";
        List<Integer> listID = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            try (ResultSet result = statement.executeQuery(query)) {
                while (result.next()) {
                    int id = result.getInt("id");
                    listID.add(id);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Exception while trying to get IDs from database", e);
        }
        return listID;
    }

    public String generateSqlQuery(String criteria, String request) throws DaoException {
        String archetype = "SELECT * FROM book WHERE %s %s" + request + "%s;";
        switch (criteria) {
            case "id":
                return String.format(archetype, "id=", "", "");
            case "name":
                return String.format(archetype, "bookname=", APOSTROPHE, APOSTROPHE);
            case "author":
                return String.format(archetype, "author=", APOSTROPHE, APOSTROPHE);
            case "ALL":
                return "SELECT * FROM book";
            default:
                throw new DaoException("Can't generate sql query");
        }
    }
}
