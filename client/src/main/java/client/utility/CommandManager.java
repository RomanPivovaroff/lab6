package client.utility;

import client.command.Command;
import common.command.AbstractCommand;
import common.command.ExecutionResponse;
import common.command.RemoteCommand;
import common.utility.Console;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Управляет командами. */
public class CommandManager {
    private static final Map<String, AbstractCommand> commands = new LinkedHashMap<>();
    private final List<String> commandHistory = new ArrayList<>();
    private Console console;
    private UDPManager udpManager;

    public CommandManager(Console console, UDPManager udpManager) {
        this.udpManager = udpManager;
        this.console = console;
    }

    /**
     * Добавляет команду.
     *
     * @param commandName Название команды.
     * @param command Команда.
     */
    public void register(String commandName, AbstractCommand command) {
        commands.put(commandName, command);
    }

    /**
     * @return Словарь команд.
     */
    public Map<String, AbstractCommand> getCommands() {
        return commands;
    }

    /**
     * @return История команд.
     */
    public List<String> getCommandHistory() {
        return commandHistory;
    }

    /**
     * Добавляет команду в историю.
     *
     * @param command Команда.
     */
    public void addToHistory(String command) {
        commandHistory.add(command);
    }

    public ExecutionResponse invoke(RemoteCommand cmd) {
        udpManager.send((RemoteCommand) cmd);
        return udpManager.receive(10000);
    }

    public ExecutionResponse invoke(Command cmd) {
        return cmd.execute();
    }
}
