package supervisor;

public class SQLRequests {
    public static final String ADD_USER = """
            INSERT INTO Users (login, password) VALUES (?, ?);
            """;
    public static final String CREATE_TABLES = """
            CREATE TABLE Users
                (
                    id       SERIAL PRIMARY KEY,
                    login    VARCHAR(255),
                    password VARCHAR(255)
                );
                CREATE TYPE Astartes_category AS ENUM (
                    'SCOUT',
                    'ASSAULT',
                    'APOTHECARY',
                    'SUPPRESSOR'
                    );
                CREATE TABLE IF NOT EXISTS Space_marine
                (
                    id                    SERIAL PRIMARY KEY,
                    name                  TEXT             NOT NULL,
                    coordinates_x         REAL,
                    coordinates_y         DOUBLE PRECISION NOT NULL CHECK (coordinates_y > -903),
                    creation_date         TIMESTAMP        NOT NULL,
                    health                INT CHECK (health > 0),
                    heart_count           INT CHECK (heart_count > 0 AND heart_count <= 3),
                    achievements          TEXT             NOT NULL,
                    category              Astartes_category,
                    chapter_name          TEXT             NOT NULL,
                    chapter_marines_count BIGINT CHECK (chapter_marines_count > 0 AND chapter_marines_count <= 1000),
                    owner TEXT NOT NULL
                );
            """;
    public static final String GET_USER = """
            SELECT * FROM Users WHERE login = ?;
            """;
    public static final String ADD_OBJECT = """
            INSERT INTO Space_marine (name, coordinates_x, coordinates_y, creation_date, health, heart_count, achievements, category, chapter_name, chapter_marines_count, owner)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id;
            """;
    public static final String UPDATE_OBJECT = """
            UPDATE Space_marine
            SET (name, coordinates_x, coordinates_y, health, heart_count, achievements, category, chapter_name, chapter_marines_count)
            = (?, ?, ?, ?, ?, ?, ?, ?, ?)
            WHERE id = ?
            RETURNING id;
            """;
    public static final String DELETE_OBJECT = """
            DELETE FROM Space_marine
            WHERE id = ?
            RETURNING id;
            """;
    public static final String GET_ALL_OBJECTS = """
            SELECT * FROM Space_marine; 
            """;
}
