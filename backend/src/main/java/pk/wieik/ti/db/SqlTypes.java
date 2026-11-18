package pk.wieik.ti.db;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public final class SqlTypes {
    private SqlTypes() {}

    private static final DateTimeFormatter DATE_TIME_WRITE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Akceptuje też 'T' jako separator i ułamki sekund. */
    private static final DateTimeFormatter DATE_TIME_READ = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .optionalStart().appendLiteral(' ').optionalEnd()
            .optionalStart().appendLiteral('T').optionalEnd()
            .appendPattern("HH:mm")
            .optionalStart().appendPattern(":ss").optionalEnd()
            .optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd()
            .toFormatter();

    public static String toDb(LocalDateTime value) {
        return value == null ? null : DATE_TIME_WRITE.format(value);
    }

    public static String toDb(LocalDate value) {
        return value == null ? null : value.toString();
    }

    public static Double toDb(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    public static int toDb(boolean value) {
        return value ? 1 : 0;
    }

    public static String toDb(Enum<?> value) {
        return value == null ? null : value.name();
    }

    public static LocalDateTime getDateTime(ResultSet rs, String column) throws SQLException {
        String text = rs.getString(column);
        return text == null ? null : LocalDateTime.parse(text.trim(), DATE_TIME_READ);
    }

    public static LocalDate getDate(ResultSet rs, String column) throws SQLException {
        String text = rs.getString(column);
        return text == null || text.isBlank() ? null : LocalDate.parse(text.trim().substring(0, 10));
    }

    public static BigDecimal getMoney(ResultSet rs, String column) throws SQLException {
        double value = rs.getDouble(column);
        return rs.wasNull() ? null : BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    public static boolean getBoolean(ResultSet rs, String column) throws SQLException {
        return rs.getInt(column) != 0;
    }

    public static Long getNullableLong(ResultSet rs, String column) throws SQLException {
        long value = rs.getLong(column);
        return rs.wasNull() ? null : value;
    }

    public static <E extends Enum<E>> E getEnum(ResultSet rs, String column, Class<E> type)
            throws SQLException {
        String text = rs.getString(column);
        return text == null ? null : Enum.valueOf(type, text);
    }
}
