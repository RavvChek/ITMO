package forms;

import model.SpaceMarine;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SpaceMarineStringForm {
    public static List<String> getListAllAttr(SpaceMarine sp) {
        List<String> result = new ArrayList<>();
        result.add(getIdStr(sp));
        result.add(sp.getName());
        result.add(getCoordinateXStr(sp));
        result.add(getCoordinatesYStr(sp));
        result.add(getCreationDateStr(sp));
        result.add(getHealthStr(sp));
        result.add(getHeartCountStr(sp));
        result.add(getAchievements(sp));
        result.add(getCategoryStr(sp));
        result.add(getChapterStrName(sp));
        result.add(getChapterMarinesCountStr(sp));
        return result;
    }

    public static String getIdStr(SpaceMarine sp) {
        return String.valueOf(sp.getId());
    }

    public static String getName(SpaceMarine sp) {
        return sp.getName();
    }

    public static String getCoordinateXStr(SpaceMarine sp) {
        return String.valueOf(sp.getCoordinates().getX());
    }

    public static String getCoordinatesYStr(SpaceMarine sp) {
        return String.valueOf(sp.getCoordinates().getY());
    }

    public static String getCreationDateStr(SpaceMarine sp) {
        return sp.getCreationDate().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static String getHealthStr(SpaceMarine sp) {
        return String.valueOf(sp.getHealth());
    }

    public static String getHeartCountStr(SpaceMarine sp) {
        return String.valueOf(sp.getHeartCount());
    }

    public static String getAchievements(SpaceMarine sp) {
        return sp.getAchievements();
    }

    public static String getCategoryStr(SpaceMarine sp) {
        if (sp.getCategory() == null) {
            return "null";
        } else {
            return sp.getCategory().toString();
        }
    }

    public static String getChapterStrName(SpaceMarine sp) {
        return sp.getChapter().getName();
    }

    public static String getChapterMarinesCountStr(SpaceMarine sp) {
        return String.valueOf(sp.getChapter().getMarinesCount());
    }
}
