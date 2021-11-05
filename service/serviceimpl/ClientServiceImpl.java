package by.epamtc.stanislavmelnikov.service.serviceimpl;

import by.epamtc.stanislavmelnikov.dao.exception.DaoException;
import by.epamtc.stanislavmelnikov.dao.factory.DaoFactory;
import by.epamtc.stanislavmelnikov.dao.daointerface.UserDao;
import by.epamtc.stanislavmelnikov.entity.User;
import by.epamtc.stanislavmelnikov.service.exception.ServiceException;
import by.epamtc.stanislavmelnikov.service.serviceinterface.ClientService;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientServiceImpl implements ClientService {
    @Override
    public User signIn(String login, String password) throws ServiceException {
        try {
            DaoFactory factory = DaoFactory.getInstance();
            UserDao userDao = factory.getUserDao();
            User user = (User) userDao.getUser(login);
            String encryptedPassword = encryptPassword(password);
            if (!user.getPassword().equals(encryptedPassword)) {
                throw new ServiceException("Wrong password");
            }
            User.setCurrentUser(user);
            return user;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void addUser(String[] params) throws ServiceException {
        String login = params[0];
        String password = params[1];
        String firstName = params[2];
        String lastName = params[3];
        String ageStr = params[4];
        String email = params[5];
        String userRole = params[6];
        if (isLoginExists(login)) throw new ServiceException("Login already exists");
        if (!isCorrectPassword(password)) throw new ServiceException("Incorrect password");
        int age = checkIntegerCorrectness(ageStr, "Incorrect age");
        if (!isCorrectEmail(email)) throw new ServiceException("Incorrect email");
        if (!isCorrectUserRole(userRole)) throw new ServiceException("Incorrect user role");
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        String encryptedPassword = encryptPassword(password);
        User user = new User(login, encryptedPassword, firstName, lastName, age, email, Boolean.parseBoolean(userRole));
        try {
            userDao.writeUserInFile(user);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }


    @Override
    public void verifyUserRights() throws ServiceException {
        boolean isAdmin = User.getCurrentUser().isAdmin();
        if (!isAdmin) throw new ServiceException("You're not allowed to do this action.");
    }

    @Override
    public void removeByLogin(String login) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        try {
            userDao.removeByLogin(login);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void updateByLogin(Map<String, String> request) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        String login = request.get("login");
        if (login == null) throw new ServiceException("You haven't entered login.");
        String newLogin = request.get("newlogin");
        String password = request.get("password");
        String firstName = request.get("firstname");
        String lastname = request.get("lastname");
        String ageStr = request.get("age");
        String email = request.get("email");
        String isAdmin = request.get("admin");
        try {
            User user = (User) userDao.getUser(login);
            setUserFields(user, newLogin, password, firstName, lastname, email, ageStr, isAdmin);
            userDao.removeByLogin(login);
            userDao.writeUserInFile(user);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void signOut() {
        User empty = new User();
        User.setCurrentUser(empty);
    }

    public String encryptPassword(String source) {
        String encrypted = DigestUtils.md5Hex(source);
        return encrypted;
    }

    public boolean isCorrectPassword(String password) {
        Pattern pattern = Pattern.compile("[A-Za-z]");
        Pattern pattern1 = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(password);
        Matcher matcher1 = pattern1.matcher(password);
        if (!matcher.find()) return false;
        if (!matcher1.find()) return false;
        return true;
    }

    public boolean isCorrectEmail(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+@[a-z]+\\.[a-z]+");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find()) return false;
        return true;
    }

    public boolean isCorrectUserRole(String userRole) {
        if (userRole.equals("true") || userRole.equals("false")) return true;
        return false;
    }

    public int checkIntegerCorrectness(String str, String message) throws ServiceException {
        int value;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new ServiceException(message);
        }
        return value;
    }

    public void setUserFields(User user, String newLogin, String password, String firstName, String lastname,
                              String email, String ageStr, String isAdmin) throws ServiceException {
        if (firstName != null) user.setFirstName(firstName);
        if (lastname != null) user.setLastName(lastname);
        if (password != null) {
            if (!isCorrectPassword(password)) throw new ServiceException("Incorrect password");
            user.setPassword(encryptPassword(password));
        }
        if (newLogin != null) {
            if (isLoginExists(newLogin)) throw new ServiceException("Login already exists");
            user.setLogin(newLogin);
        }
        if (ageStr != null) {
            int age = checkIntegerCorrectness(ageStr, "Incorrect age");
            user.setAge(age);
        }
        if (email != null) {
            if (!isCorrectEmail(email)) throw new ServiceException("Incorrect email");
            user.setEmail(email);
        }
        if (isAdmin != null) {
            if (!isCorrectUserRole(isAdmin)) throw new ServiceException("Incorrect user role");
            user.setAdmin(Boolean.parseBoolean(isAdmin));
        }
    }

    public boolean isLoginExists(String login) throws ServiceException {
        DaoFactory factory = DaoFactory.getInstance();
        UserDao userDao = factory.getUserDao();
        List<String> logins;
        try {
            logins = userDao.getLogins();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        if (logins.contains(login)) return true;
        return false;
    }
}
