package server;

import common.utility.StandardAppConsole;
import java.io.File;
import java.io.IOException;
import server.command.*;
import server.utility.*;

public class Main {
    public static void main(String[] args) {
        StandardAppConsole console = new StandardAppConsole();
        if (args.length == 0) {
            console.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }
        UDPManager udpManager = null;
        int i = 1200;
        while (i <= 65000)
            try {
                udpManager = new UDPManager(i, new SendingManager(), new ReceivingManager());
                console.print("Сервер открыт на порту: " + i);
                break;
            } catch (IOException e) {
                i++;
            }
        XMLReader reader = new XMLReader(new File(args[0]), console);
        XMLWriter writer = new XMLWriter(new File(args[0]), console);
        CollectionManager collectionManager = new CollectionManager(reader, writer);
        // Регистрируем хук для экстренного завершения программы
        Terminate terminateHook = new Terminate(console, collectionManager, udpManager);
        Runtime.getRuntime().addShutdownHook(terminateHook);
        // продолжаем логику программы
        if (!collectionManager.init()) {
            System.exit(1);
        }
        var commandManager =
                new CommandManager() {
                    {
                        register("info", new Info(console, collectionManager));
                        register("show", new Show(console, collectionManager));
                        register("add", new Add(console, collectionManager));
                        register("update", new Update(console, collectionManager));
                        register("remove_by_id", new RemoveById(console, collectionManager));
                        register("clear", new Clear(console, collectionManager));
                        register("add_if_max", new AddIfMax(console, collectionManager));
                        register("remove_greater", new RemoveGreater(console, collectionManager));
                        register(
                                "filter_by_organization",
                                new FilterByOrganization(console, collectionManager));
                        register("print_ascending", new PrintAscending(console, collectionManager));
                        register(
                                "print_unique_status",
                                new PrintUniqueStatus(console, collectionManager));
                    }
                };
        try {
            new ServerCommandProcessor(console, collectionManager, udpManager, commandManager)
                    .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    ;
}
