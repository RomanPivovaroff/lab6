package server.command;

import common.command.ExecutionResponse;
import common.utility.Console;
import java.util.stream.Stream;
import server.utility.CollectionManager;

/** Команда 'clear'. Очищает коллекцию. */
public class Clear extends AbstractCommand {
    private final Console console;
    private final CollectionManager collectionManager;

    public Clear(Console console, CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public ExecutionResponse execute(common.command.AbstractCommand abstractCommand) {
        common.command.Clear command = (common.command.Clear) abstractCommand;

        return Stream.of(command)
                .peek(cmd -> collectionManager.clear())
                .findFirst()
                .map(cmd -> new ExecutionResponse(true, "Коллекция очищена!"))
                .orElseGet(() -> new ExecutionResponse(false, "Не удалось очистить коллекцию!"));
    }
}
