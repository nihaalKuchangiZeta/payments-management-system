package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Validates if the given date string is in YYYY-MM-DD format and represents a valid date
     * @param date The date string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidDateFormat(String date) {
        if (date == null || date.trim().isEmpty()) {
            return false;
        }

        if (date.length() != 10) {
            return false;
        }

        String[] parts = date.split("-");
        if (parts.length != 3) {
            return false;
        }

        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            // Basic range validation
            if (year < 1900 || year > 3000) return false;
            if (month < 1 || month > 12) return false;
            if (day < 1 || day > 31) return false;

            // Additional validation using LocalDate to check if the date actually exists
            LocalDate.parse(date, DATE_FORMAT);
            return true;

        } catch (NumberFormatException | DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates if the start date is before or equal to the end date
     * @param startDate Start date in YYYY-MM-DD format
     * @param endDate End date in YYYY-MM-DD format
     * @return true if start date <= end date, false otherwise
     */
    public static boolean isValidDateRange(String startDate, String endDate) {
        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
            return false;
        }

        try {
            LocalDate start = LocalDate.parse(startDate, DATE_FORMAT);
            LocalDate end = LocalDate.parse(endDate, DATE_FORMAT);

            return !start.isAfter(end); // start <= end
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Gets today's date in YYYY-MM-DD format
     * @return Today's date as string
     */
    public static String getTodayDate() {
        return LocalDate.now().format(DATE_FORMAT);
    }

    /**
     * Formats a LocalDate to YYYY-MM-DD string
     * @param date LocalDate to format
     * @return Formatted date string
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMAT);
    }

    /**
     * Parses a date string to LocalDate
     * @param dateStr Date string in YYYY-MM-DD format
     * @return LocalDate object
     * @throws DateTimeParseException if the string cannot be parsed
     */
    public static LocalDate parseDate(String dateStr) throws DateTimeParseException {
        return LocalDate.parse(dateStr, DATE_FORMAT);
    }
}