package com.rm12.myapplication;

import android.util.Log;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyStarsClass {

    public static void runStarsLogic(double raValuenum, double decValue, String radiusValue, String Start_time, String end_time) {
        // Logic for finding stars goes here
        if (end_time.contains("Set Time") || end_time.contains("Set Date")) {
            end_time = "01:00 AM JAN 01 2050"; // Set to "hi" if either condition is met
        }

        try {
            double decValuenum = decValue;
            double radiusValuenum = Double.parseDouble(radiusValue);
            List<Integer> indices = findIndexMax(radiusValuenum, raValuenum, decValuenum, Start_time, end_time);
        } catch (NumberFormatException e) {
        }
    }

    // Convert degrees to radians
    public static double degToRad(double degrees) {
        return degrees * Math.PI / 180;
    }

    // Calculate angular distance between two points in RA and DEC
    public static double angularDistance(double dataRA, double dataDEC, double inputRA, double inputDEC) {
        // Convert degrees to radians
        double RA_rad = degToRad(dataRA);
        double DEC_rad = degToRad(dataDEC);
        double Input_RA_rad = degToRad(inputRA);
        double Input_DEC_rad = degToRad(inputDEC);

        // Calculate angular distance using spherical law of cosines
        double angularDistance = Math.acos(Math.sin(DEC_rad) * Math.sin(Input_DEC_rad) +
                Math.cos(DEC_rad) * Math.cos(Input_DEC_rad) * Math.cos(RA_rad - Input_RA_rad));

        // Convert angular distance to degrees
        return angularDistance * 180 / Math.PI;
    }

    // Find the indices of stars within the radius
    public static List<Integer> findIndexMax(double inputRadius, double inputRA, double inputDEC, String Start_time, String end_time) {
        List<Integer> matchingIndices = new ArrayList<>();
        List<Double> primaryMaxValues = new ArrayList<>();
        double jdDatestart = getJulianDate(Start_time); // In UTC
        double jdDateend = getJulianDate(end_time); // In UTC

        // Here, we assume you have a Stars class or data source that holds arrays of star data
        List<String> arrayNames = Stars.getArrayNames();  // Assuming this method returns a list of star names
        List<String> RA = Stars.getRA();  // RA values for each star in the dataset
        List<String> DEC = Stars.getDEC();  // DEC values for each star in the dataset
        List<String> First_var = Stars.getFIRST_VAR();
        List<String> Second_var = Stars.getSECOND_VAR();

        // Loop over the array values
        for (int i = 0; i < arrayNames.size(); i++) {
            try {
                // Retrieve the RA and DEC for this star
                double dataRA = Double.parseDouble(RA.get(i));
                double dataDEC = Double.parseDouble(DEC.get(i));
                double data_First_var = Double.parseDouble(First_var.get(i));
                double data_Second_var = Double.parseDouble(Second_var.get(i));
                double distance = angularDistance(dataRA, dataDEC, inputRA, inputDEC); // Calculate angular distance
                double primaryMax = data_First_var + (Math.ceil((jdDatestart - data_First_var) / data_Second_var)) * data_Second_var;

                // If the distance is within the radius, add the index to the result list
                if (distance <= inputRadius && primaryMax <= jdDateend) {
                    matchingIndices.add(i);
                    primaryMaxValues.add(primaryMax);
                }
            } catch (NumberFormatException e) {
            }
        }
        List<Map.Entry<Integer, Double>> indexValuePairs = new ArrayList<>();
        for (int i = 0; i < matchingIndices.size(); i++) {
            indexValuePairs.add(new AbstractMap.SimpleEntry<>(matchingIndices.get(i), primaryMaxValues.get(i)));
        }

        // Sort the list of pairs by the primaryMax value (second element of the pair)
        indexValuePairs.sort(Map.Entry.comparingByValue());

        // Clear the original matchingIndices list and add the sorted indices
        matchingIndices.clear();
        for (Map.Entry<Integer, Double> entry : indexValuePairs) {
            matchingIndices.add(entry.getKey());
        }
        return matchingIndices;
    }
    private static int getMonthNumber(String monthAbbreviation) {
        switch (monthAbbreviation) {
            case "JAN": return 1;
            case "FEB": return 2;
            case "MAR": return 3;
            case "APR": return 4;
            case "MAY": return 5;
            case "JUN": return 6;
            case "JUL": return 7;
            case "AUG": return 8;
            case "SEP": return 9;
            case "OCT": return 10;
            case "NOV": return 11;
            case "DEC": return 12;
            default: throw new IllegalArgumentException("Invalid month abbreviation: " + monthAbbreviation);
        }
    }
    public static double getJulianDate(String inputDate) {

        // Manually split the string by space to get the parts
        String[] parts = inputDate.split(" ");

        // Convert the month abbreviation to a numeric month (1-12)
        String monthAbbreviation = parts[2].toUpperCase();
        int monthNumber = getMonthNumber(monthAbbreviation);

        // Get the day and year from the parts
        int day = Integer.parseInt(parts[3]);
        int year = Integer.parseInt(parts[4]);

        LocalDate date = LocalDate.of(year, monthNumber, day); // Create the LocalDate object from the input date
        String formattedDate = date.toString();  // LocalDate's toString() gives yyyy-MM-dd format
        LocalDate startDate = LocalDate.parse("2000-01-01");

        // Calculate the number of days between the start date and the input date (formattedDate)
        long daysSince = ChronoUnit.DAYS.between(startDate, date);
        double hour, minute;
        String timeString = parts[0] + " " + parts[1];
        String[] timeParts = timeString.replaceAll(" (AM|PM)", "").split(":");
        hour = Integer.parseInt(timeParts[0]);
        minute = Integer.parseInt(timeParts[1]);
        boolean isPM = timeString.contains("PM");

        hour = (isPM && hour != 12) ? hour + 12 : (isPM || hour != 12) ? hour : 0;

        ZonedDateTime localTime = ZonedDateTime.now();
        ZonedDateTime utcTime = ZonedDateTime.now(ZoneOffset.UTC);

        // Get the difference in hours between local time and UTC
        long offsetInSeconds = localTime.getOffset().getTotalSeconds();
        double hoursDifference = -offsetInSeconds / 3600.0;  // Convert seconds to hours
        // Convert the time to decimal format: hour + (minute/60)
        double decimalDay = hour/24 + (minute / 1440.0)- (double) 12 /24 + hoursDifference/24;
        double jdDateUTC = 2451545.0 + daysSince + decimalDay; // In UTC time Julian Date
        return jdDateUTC;
    }
}