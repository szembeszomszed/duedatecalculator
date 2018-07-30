package hu.retsagimate.duedatecalculator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author retsagimate
 */
public class DueDateCalculator {

    private Date reportIssueDate;
    private Calendar reportIssueDateTime;
    private Calendar dueDateTime;
    private int remainingTurnaroundTimeInHours;
    private int turnaroundTimeInHours;

    public DueDateCalculator(String reportIssueDateInput, int turnaroundTimeInHours) {
        setReportIssueDate(reportIssueDateInput);
        setReportIssueDateTime();
        setTurnaroundTimeInHours(turnaroundTimeInHours);        
    }

    private void setReportIssueDate(String reportIssueDateInput) {
        reportIssueDate = new Date();

        try {
            reportIssueDate = getDateFromInputString(reportIssueDateInput);
        } catch (ParseException e) {
            System.out.println("Unable to parse date from: " + reportIssueDateInput + ".");
        }
    }

    private void setReportIssueDateTime() {
        reportIssueDateTime = Calendar.getInstance();
        reportIssueDateTime.setTime(reportIssueDate);  

        if (!isReportIssuedDuringWorkingHours()) {
            throw new IllegalArgumentException("Report must be issued during working hours.");
        }
    }

    private Date getDateFromInputString(String dateTimeReported) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(DateTimeConstants.DATE_FORMAT);
        
        return dateFormat.parse(dateTimeReported);        
    }
    
    public int getTurnaroundTimeInHours() {
        return this.turnaroundTimeInHours;
    }

    private void setTurnaroundTimeInHours(int turnaroundTimeInHours) {
        this.turnaroundTimeInHours = 
            turnaroundTimeInHours > DateTimeConstants.MINIMUM_ACCEPTED_TURNAROUND_TIME_IN_HOURS ? 
            turnaroundTimeInHours : 
            DateTimeConstants.MINIMUM_ACCEPTED_TURNAROUND_TIME_IN_HOURS;

        this.remainingTurnaroundTimeInHours = this.turnaroundTimeInHours;       
    }

    private boolean isReportIssuedDuringWorkingHours() {
        Calendar workingHourStart = 
            getActualTimeOf(reportIssueDateTime, DateTimeConstants.WORKING_DAY_START_HOUR);
        workingHourStart.add(Calendar.MINUTE, -1);

        Calendar workingHourEnd = 
            getActualTimeOf(reportIssueDateTime, DateTimeConstants.WORKING_DAY_END_HOUR);

        return workingHourStart.before(reportIssueDateTime) && 
            workingHourEnd.after(reportIssueDateTime) && 
            !isWeekend(reportIssueDateTime);            
    }

    public Date calculateDueDate() {
        dueDateTime = reportIssueDateTime;

        if (turnAroundTimeFitsIn(reportIssueDateTime)) {
            dueDateTime.add(Calendar.HOUR_OF_DAY, turnaroundTimeInHours);            
        } else {
            int remainingWorkingHours = getRemainingWorkingHours(dueDateTime); 
            dueDateTime.add(Calendar.HOUR_OF_DAY, remainingWorkingHours);
            
            remainingTurnaroundTimeInHours = turnaroundTimeInHours - remainingWorkingHours;

            adjustDueDateTime();            
        }

        return dueDateTime.getTime();
    }

    private int getRemainingWorkingHours(Calendar dueDateTime) {
        return (int) (getRemainingWorkingTimeInMillisecondsOn(dueDateTime) / 
            DateTimeConstants.ONE_HOUR_IN_MILLISECONDS);
    }

    private long getRemainingWorkingTimeInMillisecondsOn(Calendar actualDay) {
        Calendar workingHourEndTime = getActualTimeOf(actualDay, DateTimeConstants.WORKING_DAY_END_HOUR);
        
        return workingHourEndTime.getTimeInMillis() - actualDay.getTimeInMillis();        
    }

    private void adjustDueDateTime() {
        do {
            addNonWorkingHours();

            if (isWeekend(dueDateTime)) {
                addWeekendHours();
            }
            
            addWorkingHours();

            updateRemainingTurnaroundTime();

        } while (!turnAroundTimeFitsIn(dueDateTime));       
    }
    
    private boolean turnAroundTimeFitsIn(Calendar actualDay) {
        return getRemainingWorkingTimeInMillisecondsOn(actualDay) >= 
            remainingTurnaroundTimeInHours * DateTimeConstants.ONE_HOUR_IN_MILLISECONDS;
    }
    
    private void addNonWorkingHours() {
        dueDateTime.add(
            Calendar.HOUR_OF_DAY, 
            DateTimeConstants.NUMBER_OF_NON_WORKING_HOURS_PER_DAY);
    }
    
    private boolean isWeekend(Calendar actualDay) {
        return (actualDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
            actualDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }
    
    private void addWeekendHours() {
        dueDateTime.add(
            Calendar.HOUR_OF_DAY, 
            DateTimeConstants.NUMBER_OF_WEEKEND_HOURS);
    }
    
    private void addWorkingHours() {        
        int hoursToAdd = 
            remainingTurnaroundTimeInHours > DateTimeConstants.NUMBER_OF_WORKING_HOURS_PER_DAY ?
            DateTimeConstants.NUMBER_OF_WORKING_HOURS_PER_DAY :
            remainingTurnaroundTimeInHours;

        dueDateTime.add(
            Calendar.HOUR_OF_DAY,
            hoursToAdd
            );        
    }

    private void updateRemainingTurnaroundTime() {
        if (remainingTurnaroundTimeInHours > DateTimeConstants.NUMBER_OF_WORKING_HOURS_PER_DAY) {
            remainingTurnaroundTimeInHours -= DateTimeConstants.NUMBER_OF_WORKING_HOURS_PER_DAY;
        } else {
            remainingTurnaroundTimeInHours = 0;
        }
    }
    
    private Calendar getActualTimeOf(Calendar actualDay, int hour) {
        Calendar actualTime = Calendar.getInstance();
        int minute = 0;
        int second = 0;

        actualTime.set(
            actualDay.get(Calendar.YEAR),
            actualDay.get(Calendar.MONTH),
            actualDay.get(Calendar.DAY_OF_MONTH),
            hour,
            minute,
            second);       
        
        return actualTime;
    }
}
