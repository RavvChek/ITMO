package supervisor;

import model.SpaceMarine;
import reader.DataReader;
import supervisor.DatabaseManager;

import java.util.*;

public class LinkedListCollection {
    private DataReader dataReader;
    private final String type;
    private final Date date;
    private LinkedList<SpaceMarine> data;
    private int count;

    public LinkedListCollection(DatabaseManager databaseManager) throws Exception {
        data = databaseManager.readAll();
        if (!isEmptyData()) {
            sortData();
        }
        type = SpaceMarine.class.getName();
        date = new Date();
    }


    public LinkedList<SpaceMarine> getData() {
        return data;
    }

    public void setData(LinkedList<SpaceMarine> data) {
        this.data = data;
    }

    public Date getDate() {
        return date;
    }

    public int getCount() {
        count = data.size();
        return count;
    }

    public SpaceMarine getById(int id) {
        for (SpaceMarine sp : getData()) {
            if (sp.getId() == id) {
                return sp;
            }
        }
        return null;
    }

    public boolean isEmptyData() {
        if (data == null) {
            return true;
        } else {
            return false;
        }
    }

    public String getType() {
        return type;
    }

    public void clearData(List<Integer> deleteIds) {
        if (!isEmptyData()) {
            deleteIds.forEach((id) -> this.getData().remove(this.getById(id)));
        }
    }

    public void saveData() throws Exception {
        dataReader.saveData(data);
    }

    public void show() {
        if (!isEmptyData()) {
            for (SpaceMarine sp : data) {
                System.out.println(sp.toString());
            }
        } else {
            System.out.println("Коллекция пуста - нечего выводить");
        }
    }

    public void infoData() {
        if (!isEmptyData()) {
            System.out.println("Тип: " + this.getType());
            System.out.println("Дата инициализации: " + this.getDate().toString());
            System.out.println("Количество элементов: " + this.getCount());
        } else {
            System.out.println("Тип: " + "неизвестен");
            System.out.println("Дата инициализации: " + "неизвестна");
            System.out.println("Количество элементов: 0");
        }
    }

    public void removeFirstItem() {
        if (!isEmptyData()) {
            data.removeFirst();
            System.out.println("Объект удалён");
        } else {
            System.out.println("Коллекция пуста - нечего удалять");
        }
    }

    public void removeByIdItem(int id) {
        if (!isEmptyData()) {
            for (SpaceMarine sp : data) {
                if (sp.getId() == id) {
                    data.remove(sp);
                    System.out.println("Объект удалён");
                    break;

                }
            }
        } else {
            System.out.println("Коллекция пуста - нечего удалять");
        }
    }

    public void addItem(SpaceMarine sp) {
        data.add(sp);
    }

    public void addFirstItem(SpaceMarine sp) {
        data.addFirst(sp);
    }

    public void updateItem(int id, SpaceMarine newSp) {
        if (!isEmptyData()) {
            for (SpaceMarine sp : data) {
                if (sp.getId() == id) {
                    sp.setName(newSp.getName());
                    sp.setCoordinatesX(newSp.getCoordinates().getX());
                    sp.setCoordinatesY(newSp.getCoordinates().getY());
                    sp.setHealth(newSp.getHealth());
                    sp.setHeartCount(newSp.getHeartCount());
                    sp.setAchievements(newSp.getAchievements());
                    sp.setCategory(sp.getCategory());
                    sp.setChapterName(sp.getChapter().getName());
                    sp.setChapterMarinesCount(newSp.getChapter().getMarinesCount());
                }
            }
        } else {
            System.out.println("Коллекция пуста - нечего обновлять");
        }
    }

    public void groupCountingByName() {
        if (!isEmptyData()) {
            HashSet<String> setName = new HashSet<>();
            for (SpaceMarine sp : data) {
                setName.add(sp.getName());
            }
            HashMap<String, List> hashMap = new HashMap<>();
            for (String name : setName) {
                List<SpaceMarine> list = new ArrayList<>();
                for (SpaceMarine sp : data) {
                    if (sp.getName().equals(name)) {
                        list.add(sp);
                    }
                }
                hashMap.put(name, list);
            }
            for (String name : setName) {
                System.out.println(name + " " + hashMap.get(name).size());
            }
        } else {
            System.out.println("Коллекция пуста - нечего группировать");
        }
    }

    public void averageOfHeartCount() {
        if (!isEmptyData()) {
            try {
                int result = 0;
                for (SpaceMarine sp : data) {
                    result += sp.getHeartCount();
                }
                result /= data.size();
                System.out.println("Среднее значение:" + result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Коллекция пуста - среднее значение: 0");
        }
    }

    public void minById() {
        if (!isEmptyData()) {
            int minId = 100000;
            for (SpaceMarine sp : data) {
                if (sp.getId() < minId) {
                    minId = sp.getId();
                }
            }
            for (SpaceMarine sp : data) {
                if (sp.getId() == minId) {
                    System.out.println(sp.toString());
                }
            }
        } else {
            System.out.println("Коллекция пуста - такого объекта нет");
        }
    }

    public void sortData() {
        Collections.sort(data);
    }
}

