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
        DateFormat dateFormat = new SimpleDateFormat(DateTimeConstants.DATE_FORMAT); 
        
        String reportIssueDate1 = "02-08-2018 1:30PM";
        int turnaroundTimeInHours1 = 2;
        DueDateCalculator ddc1 = new DueDateCalculator(reportIssueDate1, turnaroundTimeInHours1);
        Date dueDate1 = ddc1.calculateDueDate();
        System.out.println("Report issue date: " + reportIssueDate1);
        System.out.println("Turnaround time: " + ddc1.getTurnaroundTimeInHours() + " hours");
        System.out.println("\tDue date: " + dateFormat.format(dueDate1));
        
        String reportIssueDate2 = "01-08-2018 4:59PM";
        int turnaroundTimeInHours2 = 3;
        DueDateCalculator ddc2 = new DueDateCalculator(reportIssueDate2, turnaroundTimeInHours2);
        Date dueDate2 = ddc2.calculateDueDate();
        System.out.println("\nReport issue date: " + reportIssueDate2);
        System.out.println("Turnaround time: " + ddc2.getTurnaroundTimeInHours() + " hours");
        System.out.println("\tDue date: " + dateFormat.format(dueDate2)); 

        String reportIssueDate3 = "03-08-2018 10:30AM";
        int turnaroundTimeInHours3 = 16;
        DueDateCalculator ddc3 = new DueDateCalculator(reportIssueDate3, turnaroundTimeInHours3);
        Date dueDate3 = ddc3.calculateDueDate();
        System.out.println("\nReport issue date: " + reportIssueDate3);
        System.out.println("Turnaround time: " + ddc3.getTurnaroundTimeInHours() + " hours");
        System.out.println("\tDue date: " + dateFormat.format(dueDate3));
        
        String reportIssueDate4 = "03-08-2018 10:30AM";
        int turnaroundTimeInHours4 = 41;
        DueDateCalculator ddc4 = new DueDateCalculator(reportIssueDate4, turnaroundTimeInHours4);
        Date dueDate4 = ddc4.calculateDueDate();
        System.out.println("\nReport issue date: " + reportIssueDate4);
        System.out.println("Turnaround time: " + ddc4.getTurnaroundTimeInHours() + " hours");
        System.out.println("\tDue date: " + dateFormat.format(dueDate4));
    }
}
