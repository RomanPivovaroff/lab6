package server.command;

import common.command.ExecutionResponse;
import common.entity.Worker;
import common.utility.Console;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import server.utility.CollectionManager;

/** Команда 'remove_greater'. Удаляет из коллекции все элементы, превышающие заданный. */
public class RemoveGreater extends AbstractCommand {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveGreater(Console console, CollectionManager collectionManager) {
        super(
                "remove_greater {element}",
                "удалить из коллекции все элементы, превышающие заданный");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse execute(common.command.AbstractCommand abstractCommand) {
        common.command.RemoveGreater command = (common.command.RemoveGreater) abstractCommand;
        Worker targetWorker = command.getWorker();

        if (targetWorker == null || !targetWorker.validate()) {
            return new ExecutionResponse(false, "Поля worker не валидны! Worker не создан!");
        }

        // Добавляем нового worker в коллекцию
        collectionManager.add(targetWorker);

        // Получаем список элементов с большей зарплатой
        List<Worker> toRemove =
                collectionManager.getSortCollection().stream()
                        .filter(
                                w ->
                                        !Objects.equals(w, targetWorker)
                                                && w.getSalary() > targetWorker.getSalary())
                        .collect(Collectors.toList());

        // Удаляем их
        toRemove.forEach(w -> collectionManager.remove(w.getId()));

        return new ExecutionResponse(
                true, "Worker успешно добавлен, все рабочие с большей зарплатой удалены");
    }
}
