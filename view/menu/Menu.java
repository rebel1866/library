package by.epamtc.stanislavmelnikov.view.menu;

import by.epamtc.stanislavmelnikov.view.input.DataScanner;
import by.epamtc.stanislavmelnikov.view.output.ConsoleOutput;
import by.epamtc.stanislavmelnikov.view.requestgenerator.RequestGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu implements IMenu {
    private List<String> menuList = new ArrayList<>();
    private RequestGenerator requestGenerator;
    private ConsoleOutput consoleOutput;
    private DataScanner dataScanner;
    private Map<String, MenuAction> menuChoice;
    private final MenuAction viewAll = () -> requestGenerator.doViewAllBooksRequest();
    private final MenuAction searchID = () -> requestGenerator.doSearchByIdReq();
    private final MenuAction searchTitle = () -> requestGenerator.doSearchByTitleReq();
    private final MenuAction searchAuthor = () -> requestGenerator.doSearchByAuthorReq();
    private final MenuAction addBook = () -> requestGenerator.doAddBookRequest();
    private final MenuAction updateBook = () -> requestGenerator.doUpdateBookByIdReq();
    private final MenuAction deleteBook = () -> requestGenerator.doDeleteBookByIdReq();
    private final MenuAction addUser = () -> requestGenerator.doAddUserRequest();
    private final MenuAction removeUser = () -> requestGenerator.doDeleteUserRequest();
    private final MenuAction updateUser = () -> requestGenerator.doUpdateUserRequest();
    private final MenuAction signOut = () -> requestGenerator.doSignOutRequest();


    public Menu(RequestGenerator requestGenerator, ConsoleOutput consoleOutput, DataScanner dataScanner) {
        this.requestGenerator = requestGenerator;
        this.dataScanner = dataScanner;
        this.consoleOutput = consoleOutput;
        this.menuChoice = new HashMap<>();
        menuChoice.put("1", viewAll);
        menuChoice.put("2", searchID);
        menuChoice.put("3", searchTitle);
        menuChoice.put("4", searchAuthor);
        menuChoice.put("5", addBook);
        menuChoice.put("6", updateBook);
        menuChoice.put("7", deleteBook);
        menuChoice.put("8", addUser);
        menuChoice.put("9", updateUser);
        menuChoice.put("10", removeUser);
        menuChoice.put("11", signOut);
    }

    public void setMenuList(String item) {
        menuList.add(item);
    }

    public void setMenuChoice(String key, MenuAction action) {
        menuChoice.put(key, action);
    }


    @Override
    public void createMenu() {
        menuList.add("1.Просмотреть весь каталог");
        menuList.add("2.Поиск по id");
        menuList.add("3.Поиск по названию");
        menuList.add("4.Поиск по автору");
        menuList.add("5.Добавить книгу");
        menuList.add("6.Изменить книгу");
        menuList.add("7.Удалить книгу");
        menuList.add("8.Добавить пользователя");
        menuList.add("9.Изменить пользователя");
        menuList.add("10.Удалить пользователя");
        menuList.add("11.Выйти из аккаунта");
    }

    @Override
    public void runMenu() {
        consoleOutput.printMenu(menuList);
        String input = dataScanner.inputString();
        if (input.equalsIgnoreCase("E")) System.exit(0);
        try {
            menuChoice.get(input).doAction();
        } catch (NullPointerException e) {
            consoleOutput.printMessage("Action does not exist");
            runMenu();
        }
        runMenu();
    }
}
