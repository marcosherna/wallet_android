package com.example.wallet.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatHelper {
    /**
     * Formatea una fecha a una cadena según el patrón especificado.
     *
     * @param date    La fecha a formatear.
     * @param pattern El patrón de formato.
     * @return La cadena formateada.
     */
    public static String format(Date date, String pattern) {
        if (date == null || pattern == null || pattern.isEmpty()) {
            return "";
        }
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
    }

    /**
     * Parsea una cadena de texto a un objeto Date según el formato predefinido.
     *
     * @param dateString La cadena de fecha a parsear.
     * @return El objeto Date correspondiente.
     * @throws java.text.ParseException Si la cadena no coincide con el formato esperado.
     */
    public static Date parseDate(String dateString) throws java.text.ParseException {
        // Define el formato que esperas en 'dateString'
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d 'de' MMMM 'del' yyyy", Locale.getDefault());
        return sdf.parse(dateString);
    }
}
