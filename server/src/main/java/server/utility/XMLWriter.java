package server.utility;

import common.utility.Console;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.*;

/** Класс записывающий данные из коллекции в формате XML */
public class XMLWriter {

    private final File file;
    private final Console console;

    /**
     * Конструктор врайтера.
     *
     * @param file Файл в который будет производиться запись
     */
    public XMLWriter(File file, Console console) {
        this.file = file;
        this.console = console;
    }

    /**
     * Метод сериализующий объект в XML формат и записывающий его в файл file.
     *
     * @param o Объект для сериализации.
     */
    public boolean write(Serializable o) throws IOException {
        BufferedOutputStream outputStream = null;
        boolean saveSuccess = false;
        try {
            JAXBContext context = JAXBContext.newInstance(o.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            marshaller.marshal(o, outputStream);
            console.println("Файл успешно сохранен: " + file.getAbsolutePath());
            saveSuccess = true;
        } catch (JAXBException e) {
            console.printError("Выбранный объект не сериализуем.");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    console.printError("Ошибка при закрытии файла: " + e.getMessage());
                }
            }
        }
        return saveSuccess;
    }
}
