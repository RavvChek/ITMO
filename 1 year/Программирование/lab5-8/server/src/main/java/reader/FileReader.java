package reader;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import exceptions.WrongValuesException;
import model.AstartesCategory;
import model.Chapter;
import model.Coordinates;
import model.SpaceMarine;
import forms.SpaceMarineStringForm;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FileReader extends DataReader {

    public FileReader(String filePath) {
        this.filePath = filePath;
        file = new File(filePath);
    }

    public void listValidateFileId(LinkedList<SpaceMarine> list) throws WrongValuesException {
        for (SpaceMarine sp1 : list) {
            for (SpaceMarine sp2 : list) {
                if (sp1.getId() == sp2.getId() && !sp1.equals(sp2)) {
                    throw new WrongValuesException("Одинаковые id в файле");
                }
            }
        }
    }

    @Override
    public void saveData(LinkedList<SpaceMarine> values) throws Exception {
        if (!file.exists()) {
            try {
                file = new File("new_file");
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Невозможно создать файл");
            }
        }
        try {
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filePath)), ',', CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            String[] header = {"id", "name", "coordinates_x", "coordinates_y", "creation_date", "health", "heart_count", "achievements", "category", "chapter_name", "chapter_marines_count"};
            writer.writeNext(header);
            List<String[]> data = new ArrayList<>();
            for (SpaceMarine spaceMarine : values) {
                String[] line = {
                        SpaceMarineStringForm.getIdStr(spaceMarine),
                        SpaceMarineStringForm.getName(spaceMarine),
                        SpaceMarineStringForm.getCoordinateXStr(spaceMarine),
                        SpaceMarineStringForm.getCoordinatesYStr(spaceMarine),
                        SpaceMarineStringForm.getCreationDateStr(spaceMarine),
                        SpaceMarineStringForm.getHealthStr(spaceMarine),
                        SpaceMarineStringForm.getHeartCountStr(spaceMarine),
                        SpaceMarineStringForm.getAchievements(spaceMarine),
                        SpaceMarineStringForm.getCategoryStr(spaceMarine),
                        SpaceMarineStringForm.getChapterStrName(spaceMarine),
                        SpaceMarineStringForm.getChapterMarinesCountStr(spaceMarine)
                };
                data.add(line);
            }
            writer.writeAll(data);
            System.out.println("Коллекция сохранена");
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файла не существует");
        } catch (IOException e) {
            System.out.println("Непонятная ошибка, коллекция не сохранена");
        }
    }

    public LinkedList<SpaceMarine> getData() {
        try {
            LinkedList<SpaceMarine> list = new LinkedList<>();
            String[] header = {"id", "name", "coordinates_x", "coordinates_y", "creation_date", "health", "heart_count", "achievements", "category", "chapter_name", "chapter_marines_count"};
            CSVReader reader = new CSVReader(new java.io.FileReader(file));
            String[] headerFile = reader.readNext();
            Map<String, Integer> headerMap = new HashMap<>();
            if (headerFile == null) {
                throw new IOException();
            } else {
                for (int i = 0; i < headerFile.length; i++) {
                    headerMap.put(headerFile[i], i);
                }
            }
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                SpaceMarine.SpaceMarineValidation.validateUniqueId(list, Integer.parseInt(nextLine[headerMap.get("id")]));
                SpaceMarine spaceMarine = new SpaceMarine();
                spaceMarine.setId(Integer.parseInt(nextLine[headerMap.get("id")]));
                spaceMarine.setName(nextLine[headerMap.get("name")]);
                String X = nextLine[headerMap.get("coordinates_x")];
                String Y = nextLine[headerMap.get("coordinates_y")];
                if (X.equals("NaN")) {
                    spaceMarine.setCoordinates(new Coordinates(Float.NaN, Double.parseDouble(Y)));
                } else {

                    spaceMarine.setCoordinates(new Coordinates(Float.parseFloat(X), Double.parseDouble(Y)));
                }
                spaceMarine.setCreationDate(ZonedDateTime.parse(nextLine[headerMap.get("creation_date")]));
                String health = nextLine[headerMap.get("health")];
                if (health.equals("null")) {
                    spaceMarine.setHealth(null);
                } else {
                    spaceMarine.setHealth(Integer.parseInt(health));
                }
                spaceMarine.setHeartCount(Integer.parseInt(nextLine[headerMap.get("heart_count")]));
                spaceMarine.setAchievements(nextLine[headerMap.get("achievements")]);
                String category = nextLine[headerMap.get("category")];
                if (category.equals("null")) {
                    spaceMarine.setCategory(null);
                } else {
                    spaceMarine.setCategory(AstartesCategory.valueOf(category));
                }
                spaceMarine.setChapter(new Chapter(nextLine[headerMap.get("chapter_name")], Long.parseLong(nextLine[headerMap.get("chapter_marines_count")])));
                list.add(spaceMarine);
            }
            reader.close();
            SpaceMarine.SpaceMarineValidation.listValidate(list);
            //listValidateFileId(list);
            return list;
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка в дате инициализации объектов");
            System.exit(0);
        } catch (NullPointerException e) {
            System.out.println("Неверные данные в файле");
            System.exit(0);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            return new LinkedList<>();
        } catch (CsvValidationException | IOException e) {
            System.out.println(("Ошибка чтения файла!"));
            System.exit(0);
        } catch (NumberFormatException e) {
            System.out.println(("Данные в файле неверны!\n" + e.getMessage()));
            System.exit(0);
        } catch (WrongValuesException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return new LinkedList<>();
    }
}
