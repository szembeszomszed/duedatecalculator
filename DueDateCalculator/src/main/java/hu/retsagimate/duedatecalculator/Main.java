package hu.retsagimate.duedatecalculator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author retsagimate
 */
public class Main {
    public static void main(String[] args) {
        DateFormat dateFormat = new SimpleDateFormat(DateConstants.DATE_FORMAT); 
        
        String reportIsseDate1 = "02-08-2018 1:30PM";
        int turnaroundTimeInHours1 = 2;
        DueDateCalculator ddc1 = new DueDateCalculator(reportIsseDate1, turnaroundTimeInHours1);
        Date dueDate1 = ddc1.calculateDueDate();
        System.out.println("Report issue date: " + reportIsseDate1);
        System.out.println("Turnaround time: " + turnaroundTimeInHours1 + " hours");
        System.out.println("\tDue date: " + dateFormat.format(dueDate1));
        
        String reportIsseDate2 = "01-08-2018 4:59PM";
        int turnaroundTimeInHours2 = 3;
        DueDateCalculator ddc2 = new DueDateCalculator(reportIsseDate2, turnaroundTimeInHours2);
        Date dueDate2 = ddc2.calculateDueDate();
        System.out.println("\nReport issue date: " + reportIsseDate2);
        System.out.println("Turnaround time: " + turnaroundTimeInHours2 + " hours");
        System.out.println("\tDue date: " + dateFormat.format(dueDate2)); 

        String reportIsseDate3 = "03-08-2018 10:30AM";
        int turnaroundTimeInHours3 = 16;
        DueDateCalculator ddc3 = new DueDateCalculator(reportIsseDate3, turnaroundTimeInHours3);
        Date dueDate3 = ddc3.calculateDueDate();
        System.out.println("\nReport issue date: " + reportIsseDate3);
        System.out.println("Turnaround time: " + turnaroundTimeInHours3 + " hours");
        System.out.println("\tDue date: " + dateFormat.format(dueDate3));
        
        String reportIsseDate4 = "03-08-2018 10:30AM";
        int turnaroundTimeInHours4 = 41;
        DueDateCalculator ddc4 = new DueDateCalculator(reportIsseDate4, turnaroundTimeInHours4);
        Date dueDate4 = ddc4.calculateDueDate();
        System.out.println("\nReport issue date: " + reportIsseDate4);
        System.out.println("Turnaround time: " + turnaroundTimeInHours4 + " hours");
        System.out.println("\tDue date: " + dateFormat.format(dueDate4));
    }
}
