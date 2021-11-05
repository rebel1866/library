package by.epamtc.stanislavmelnikov.controller.executor;

import by.epamtc.stanislavmelnikov.controller.commandinterface.Command;
import by.epamtc.stanislavmelnikov.controller.factory.CommandProvider;

public class Executor {
    private CommandProvider provider = new CommandProvider();
    private final String splitSymbol = "~";

    public String executeTask(String request) {
        String commandName;
        Command executionCommand;
        String response;
        commandName = request.substring(0, request.indexOf(splitSymbol));
        executionCommand = provider.getCommand(commandName);
        response = executionCommand.execute(request);
        return response;
    }

    public CommandProvider getProvider() {
        return provider;
    }
}
