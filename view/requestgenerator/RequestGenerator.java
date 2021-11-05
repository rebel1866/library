package by.epamtc.stanislavmelnikov.view.requestgenerator;

import by.epamtc.stanislavmelnikov.controller.executor.Executor;
import by.epamtc.stanislavmelnikov.view.input.DataScanner;
import by.epamtc.stanislavmelnikov.view.output.ConsoleOutput;

public class RequestGenerator {
    private ConsoleOutput consoleOutput;
    private DataScanner dataScanner;
    private Executor executor;
    private static final String DELIMITER = "~";
    private static final String OPERATION_FAIL_CODE = "0";
    private static final String OPERATION_SUCCESS_CODE = "1";

    public RequestGenerator(ConsoleOutput consoleOutput, DataScanner dataScanner, Executor executor) {
        this.consoleOutput = consoleOutput;
        this.dataScanner = dataScanner;
        this.executor = executor;
    }

    public void doSignInRequest() {
        String requestName = "SIGN_IN";
        consoleOutput.printLoginRequest();
        String login = dataScanner.inputString();
        consoleOutput.printPasswordRequest();
        String password = dataScanner.inputString();
        String request = requestName + DELIMITER + login + DELIMITER + password;
        String response = executor.executeTask(request);
        String responseBody = getResponseBody(response);
        String responseCode = getResponseCode(response);
        consoleOutput.printMessage(responseBody);
        if (responseCode.equals(OPERATION_FAIL_CODE)) doSignInRequest();
    }


    public void doAddUserRequest() {
        if (!isAdminRequest()) return;
        consoleOutput.printLoginRequest();
        String login = dataScanner.inputString();
        consoleOutput.printPasswordRequest();
        String password = dataScanner.inputString();
        consoleOutput.printMessage("Enter first name:");
        String firstName = dataScanner.inputString();
        consoleOutput.printMessage("Enter last name:");
        String lastName = dataScanner.inputString();
        consoleOutput.printMessage("Enter age:");
        String age = dataScanner.inputString();
        consoleOutput.printMessage("Enter email:");
        String email = dataScanner.inputString();
        consoleOutput.printMessage("Enter user role (is admin?):");
        String userRole = dataScanner.inputString();
        String requestName = "ADD_USER";
        String request = requestName + DELIMITER + login + DELIMITER + password + DELIMITER + firstName +
                DELIMITER + lastName + DELIMITER + age + DELIMITER + email + DELIMITER + userRole;
        String response = executor.executeTask(request);
        String responseBody = getResponseBody(response);
        String responseCode = getResponseCode(response);
        consoleOutput.printMessage(responseBody);
        if (responseCode.equals(OPERATION_FAIL_CODE)) doAddUserRequest();
    }

    public void doAddBookRequest() {
        if (!isAdminRequest()) return;
        consoleOutput.printMessage("Enter book name:");
        String name = dataScanner.inputString();
        consoleOutput.printMessage("Enter book author:");
        String author = dataScanner.inputString();
        consoleOutput.printMessage("Enter book year of issue: ");
        String year = dataScanner.inputString();
        consoleOutput.printMessage("Enter amount of pages:");
        String amountPages = dataScanner.inputString();
        String requestName = "ADD_BOOK";
        String request = requestName + DELIMITER + name + DELIMITER + author + DELIMITER + year + DELIMITER + amountPages;
        String response = executor.executeTask(request);
        String responseBody = getResponseBody(response);
        String responseCode = getResponseCode(response);
        consoleOutput.printMessage(responseBody);
        if (responseCode.equals(OPERATION_FAIL_CODE)) doAddBookRequest();
    }

    public void doViewAllBooksRequest() {
        String request = "VIEW_ALL" + DELIMITER;
        String response = executor.executeTask(request);
        String responseBody = getResponseBody(response);
        consoleOutput.printMessage(responseBody);
    }

    public void doSearchByTitleReq() {
        consoleOutput.printMessage("Enter title of book to search:");
        String title = dataScanner.inputString();
        String requestName = "SEARCH_TITLE";
        String request = requestName + DELIMITER + title;
        String response = executor.executeTask(request);
        String responseBody = getResponseBody(response);
        consoleOutput.printMessage(responseBody);
    }

    public void doSearchByIdReq() {
        consoleOutput.printMessage("Enter id of book to search:");
        String id = dataScanner.inputString();
        String requestName = "SEARCH_ID";
        String request = requestName + DELIMITER + id;
        String response = executor.executeTask(request);
        String responseBody = getResponseBody(response);
        consoleOutput.printMessage(responseBody);
    }

    public void doSearchByAuthorReq() {
        consoleOutput.printMessage("Enter author of book to search:");
        String author = dataScanner.inputString();
        String requestName = "SEARCH_AUTHOR";
        String request = requestName + DELIMITER + author;
        String response = executor.executeTask(request);
        String responseBody = getResponseBody(response);
        consoleOutput.printMessage(responseBody);
    }

    public void doDeleteBookByIdReq() {
        if (!isAdminRequest()) return;
        consoleOutput.printMessage("Enter id of book to remove:");
        String id = dataScanner.inputString();
        String requestName = "DELETE_BY_ID";
        String request = requestName + DELIMITER + id;
        String response = executor.executeTask(request);
        String responseBody = getResponseBody(response);
        consoleOutput.printMessage(responseBody);
    }

    public void doUpdateBookByIdReq() {
        if (!isAdminRequest()) return;
        consoleOutput.printMessage("Enter id of book, you want to update, and fields to change" +
                " (for example \"name=new book name\")");
        consoleOutput.printMessage("Press E to exit");
        StringBuilder request = new StringBuilder("UPDATE_BOOK_BY_ID");
        request.append(dataScanner.inputMultipleStrings());
        String response = executor.executeTask(request.toString());
        String responseCode = getResponseCode(response);
        String responseBody = getResponseBody(response);
        consoleOutput.printMessage(responseBody);
        if (responseCode.equals(OPERATION_FAIL_CODE)) doUpdateBookByIdReq();
    }

    public void doDeleteUserRequest() {
        if (!isAdminRequest()) return;
        consoleOutput.printMessage("Enter login of user to remove:");
        String login = dataScanner.inputString();
        String requestName = "DELETE_BY_LOGIN";
        String request = requestName + DELIMITER + login;
        String response = executor.executeTask(request);
        String responseBody = getResponseBody(response);
        consoleOutput.printMessage(responseBody);
    }

    public void doUpdateUserRequest() {
        if (!isAdminRequest()) return;
        consoleOutput.printMessage("Enter login of user, you want to update, and fields to change" +
                " (for example \"username=Ivan\")");
        consoleOutput.printMessage("Press E to exit");
        StringBuilder request = new StringBuilder("UPDATE_USER_BY_LOGIN");
        request.append(dataScanner.inputMultipleStrings());
        String response = executor.executeTask(request.toString());
        String responseCode = getResponseCode(response);
        String responseBody = getResponseBody(response);
        consoleOutput.printMessage(responseBody);
        if (responseCode.equals(OPERATION_FAIL_CODE)) doUpdateUserRequest();
    }

    public void doSignOutRequest() {
        String requestName = "SIGN_OUT";
        String request = requestName + DELIMITER;
        String response = executor.executeTask(request);
        String responseCode = getResponseCode(response);
        String responseBody = getResponseBody(response);
        consoleOutput.printMessage(responseBody);
        if (responseCode.equals(OPERATION_SUCCESS_CODE)) doSignInRequest();
    }


    public boolean isAdminRequest() {
        String adminRightsResp = executor.executeTask("VERIFY_ADMIN" + DELIMITER);
        String adminResponseCode = getResponseCode(adminRightsResp);
        if (adminResponseCode.equals(OPERATION_FAIL_CODE)) {
            String responseBody = getResponseBody(adminRightsResp);
            consoleOutput.printMessage(responseBody);
            return false;
        }
        return true;
    }

    public String getResponseBody(String response) {
        return response.substring(1);
    }

    public String getResponseCode(String response) {
        return response.substring(0, 1);
    }
}
