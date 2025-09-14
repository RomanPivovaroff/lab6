package server.utility;

import common.entity.SerializedWorkers;
import common.entity.Worker;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/** Клаас для Управления коллекцией. */
public class CollectionManager {
    private int currentId = 1;
    private Map<Integer, Worker> workers = new HashMap<>();
    private TreeSet<Worker> collection = new TreeSet<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final XMLReader xmlReader;
    private final XMLWriter xmlWriter;

    public CollectionManager(XMLReader xmlReader, XMLWriter xmlWriter) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.xmlReader = xmlReader;
        this.xmlWriter = xmlWriter;
    }

    /**
     * @return Последнее время инициализации.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Последнее время сохранения.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return коллекция.
     */
    public TreeSet<Worker> getCollection() {
        return collection;
    }

    /**
     * @return коллекция.
     */
    public ArrayList<Worker> getSortCollection() {
        ArrayList<Worker> newCollection = new ArrayList<>(collection);
        newCollection.sort(
                new Comparator<Worker>() {
                    public int compare(Worker s1, Worker s2) {
                        return s1.getSalary() - s2.getSalary();
                    }
                });
        return newCollection;
    }

    /** Получить Worker по ID */
    public Worker byId(int id) {
        return workers.get(id);
    }

    /** Содержит ли колекции Worker */
    public boolean isContain(Worker e) {
        return e == null || byId(e.getId()) != null;
    }

    /** Получить свободный ID */
    public int getFreeId() {
        while (byId(++currentId) != null) if (++currentId < 0) currentId = 1;
        return currentId;
    }

    /** Добавляет Worker */
    public boolean add(Worker a) {
        if (isContain(a)) return false;
        workers.put(a.getId(), a);
        collection.add(a);
        return true;
    }

    /** Обновляет Worker */
    public boolean update(Worker a) {
        if (!isContain(a)) return false;
        collection.remove(byId(a.getId()));
        workers.put(a.getId(), a);
        collection.add(a);
        return true;
    }

    /** Удаляет Worker по ID */
    public boolean remove(int id) {
        var a = byId(id);
        if (a == null) return false;
        workers.remove(a.getId());
        collection.remove(a);
        return true;
    }

    /** Фиксирует изменения коллекции */
    public boolean init() {
        collection.clear();
        workers.clear();
        xmlReader.read(collection);
        lastInitTime = LocalDateTime.now();
        for (var e : collection)
            if (byId(e.getId()) != null) {
                collection.clear();
                workers.clear();
                return false;
            } else {
                if (e.getId() > currentId) currentId = e.getId();
                workers.put(e.getId(), e);
            }
        return true;
    }

    /** Сохраняет коллекцию в файл */
    public boolean saveCollection() throws IOException {
        boolean saveSuccess = xmlWriter.write(new SerializedWorkers(new ArrayList<>(collection)));
        lastSaveTime = LocalDateTime.now();
        return saveSuccess;
    }

    /** очищает коллекцию */
    public void clear() {
        collection.clear();
        workers.clear();
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();
        for (var Worker : collection) {
            info.append(Worker + "\n\n");
        }
        return info.toString().trim();
    }
}
