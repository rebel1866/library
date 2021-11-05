package by.epamtc.stanislavmelnikov.controller.factory;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.controller.commandimpl.*;
import by.epamtc.stanislavmelnikov.controller.commandenum.CommandName;
import by.epamtc.stanislavmelnikov.controller.commandimpl.SearcherByTitle;
import by.epamtc.stanislavmelnikov.runner.Runner;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandProvider {
    private Map<CommandName, Command> repository = new HashMap<>();

    public CommandProvider() {
        repository.put(CommandName.SIGN_IN, new Authorization());
        repository.put(CommandName.ADD_USER, new UserAdder());
        repository.put(CommandName.VERIFY_ADMIN, new AdminRightsVerifier());
        repository.put(CommandName.ADD_BOOK, new BookAdder());
        repository.put(CommandName.VIEW_ALL, new BookViewer());
        repository.put(CommandName.SEARCH_TITLE, new SearcherByTitle());
        repository.put(CommandName.SEARCH_ID, new SearcherById());
        repository.put(CommandName.SEARCH_AUTHOR, new SearcherByAuthor());
        repository.put(CommandName.DELETE_BY_ID, new BookRemover());
        repository.put(CommandName.UPDATE_BOOK_BY_ID, new BookUpdater());
        repository.put(CommandName.DELETE_BY_LOGIN, new UserRemover());
        repository.put(CommandName.UPDATE_USER_BY_LOGIN, new UserUpdater());
        repository.put(CommandName.SIGN_OUT, new SigningOut());
    }

    public void setCommand(CommandName commandName, Command command) {
        repository.put(commandName, command);
    }

    public Command getCommand(String name) {
        CommandName commandName;
        Command command = null;
        try {
            commandName = CommandName.valueOf(name);
            command = repository.get(commandName);
        } catch (IllegalArgumentException | NullPointerException e) {
            Logger logger = Runner.getLogger();
            logger.log(Level.INFO, "Provider exception", e);
        }
        return command;
    }
}
