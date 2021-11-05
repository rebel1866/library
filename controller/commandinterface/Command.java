package by.epamtc.stanislavmelnikov.controller.commandinterface;

public interface Command {
    String splitSymbol = "~";
    String operationFailCode = "0";
    String operationSuccessCode = "1";

    String execute(String request);
}
