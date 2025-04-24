package model;


import exceptions.WrongValuesException;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public class Chapter implements Serializable {

    private String name; //Поле не может быть null, Строка не может быть пустой

    private long marinesCount; //Значение поля должно быть больше 0, Максимальное значение поля: 1000

    public Chapter(String name, long marinesCount) {
        this.name = name;
        this.marinesCount = marinesCount;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMarinesCount() {
        return this.marinesCount;
    }


    public void setMarinesCount(long marinesCount) {
        this.marinesCount = marinesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return Objects.equals(name, chapter.name) && Objects.equals(marinesCount, chapter.marinesCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, marinesCount);
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "name='" + name + '\'' +
                ", marinesCount=" + marinesCount +
                '}';
    }

    public static class ChapterValidation {
        public static void validate(Chapter chp) throws WrongValuesException {
            validateName(chp.getName());
            validateMarinesCount(chp.getMarinesCount());
        }

        public static void validateName(String name) throws WrongValuesException {
            if (name == null || name.equals("")) {
                throw new WrongValuesException("Поле chapter_name не может быть null ил пустым");
            }
        }

        public static void validateMarinesCount(long mrcount) throws WrongValuesException {
            if (mrcount < 0 || mrcount > 1000) {
                throw new WrongValuesException("Поле chapter_marines_count не может быть больше 1000 или быть меньше 0");
            }
        }
    }
}