package common.command;

import java.io.Serializable;

/** Абстрактный класс от которого наследуются все команды, используемые пользователем. */
public abstract class AbstractCommand implements Serializable, RemoteCommand {
    private final String description;
    private final String name;

    /**
     * Конструктор
     *
     * @param name имя команды
     * @param description описание работы команды и входных данных
     */
    public AbstractCommand(String name, String description) {
        this.description = description;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
