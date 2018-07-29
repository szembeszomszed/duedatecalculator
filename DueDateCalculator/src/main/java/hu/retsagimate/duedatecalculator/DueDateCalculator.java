/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private int remainingTurnaroundTimeInHours;
    private int turnaroundTimeInHours;

    public DueDateCalculator(String reportIssueDateInput, int turnaroundTimeInHours) {
        setReportIssueDate(reportIssueDateInput);
        setReportIssueDateTime();
        setTurnaroundTime(turnaroundTimeInHours);        
    }

    private void setReportIssueDate(String reportIssueDateInput) {
        reportIssueDate = new Date();

        try {
            reportIssueDate = getDateFromInputString(reportIssueDateInput);
        } catch (ParseException e) {
            System.out.println("Unable to parse date from: " + reportIssueDateInput + ".");
        }
    }

    private Date getDateFromInputString(String dateTimeReported) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(DateConstants.INPUT_DATE_FORMAT);
        
        return dateFormat.parse(dateTimeReported);        
    }

    private void setTurnaroundTime(int turnaroundTimeInHours) {
        this.turnaroundTimeInHours = 
            turnaroundTimeInHours > 0 ? turnaroundTimeInHours : 0;

        this.remainingTurnaroundTimeInHours = turnaroundTimeInHours;       
    }

    private void setReportIssueDateTime() {
        reportIssueDateTime = Calendar.getInstance();
        reportIssueDateTime.setTime(reportIssueDate);  

        if (!isReportIssuedDuringWorkingHours()) {
            throw new IllegalArgumentException("Report must be issued during working hours.");
        }
    }

    private boolean isReportIssuedDuringWorkingHours() {
        Calendar workingHourStart = 
            getTimeOf(reportIssueDateTime, DateConstants.WORKING_DAY_START_HOUR);
        workingHourStart.add(Calendar.MINUTE, -1);

        Calendar workingHourEnd = 
            getTimeOf(reportIssueDateTime, DateConstants.WORKING_DAY_END_HOUR);

        return workingHourStart.before(reportIssueDateTime) && 
            workingHourEnd.after(reportIssueDateTime) && 
            !isWeekend(reportIssueDateTime);            
    }

    public Date calculateDueDate() {
        Calendar dueDateTime = reportIssueDateTime;

        if (turnAroundTimeFitsIn(reportIssueDateTime)) {
            dueDateTime.add(Calendar.HOUR_OF_DAY, turnaroundTimeInHours);            
        } else {
            int remainingWorkingHours = getRemainingWorkingHours(dueDateTime);           

            remainingTurnaroundTimeInHours = turnaroundTimeInHours - remainingWorkingHours;
            dueDateTime.add(Calendar.HOUR_OF_DAY, remainingWorkingHours);

            adjustDueDateTime(dueDateTime);            
        }

        return dueDateTime.getTime();
    }

    private int getRemainingWorkingHours(Calendar dueDateTime) {
        return (int) (getRemainingWorkingTimeInMillisecondsOn(dueDateTime) / 
            DateConstants.ONE_HOUR_IN_MILLISECONDS);
    }

    private boolean turnAroundTimeFitsIn(Calendar actualDay) {
        return getRemainingWorkingTimeInMillisecondsOn(actualDay) >= 
            remainingTurnaroundTimeInHours * DateConstants.ONE_HOUR_IN_MILLISECONDS;
    }

    private long getRemainingWorkingTimeInMillisecondsOn(Calendar actualDay) {
        Calendar workingHourEndTime = getTimeOf(actualDay, DateConstants.WORKING_DAY_END_HOUR);
        
        return workingHourEndTime.getTimeInMillis() - actualDay.getTimeInMillis();        
    }

    private void adjustDueDateTime(Calendar dueDateTime) {
        do {
            addNonWorkingHours(dueDateTime);

            if (isWeekend(dueDateTime)) {
                addWeekendHours(dueDateTime);
            }

            dueDateTime.add(
                Calendar.HOUR_OF_DAY,
                remainingTurnaroundTimeInHours > DateConstants.NUMBER_OF_WORKING_HOURS_PER_DAY ?
                    DateConstants.NUMBER_OF_WORKING_HOURS_PER_DAY :
                    remainingTurnaroundTimeInHours
                );

            updateRemainingTurnaroundTime();

        } while (!turnAroundTimeFitsIn(dueDateTime));       
    }

    private void updateRemainingTurnaroundTime() {
        if (remainingTurnaroundTimeInHours > DateConstants.NUMBER_OF_WORKING_HOURS_PER_DAY) {
            remainingTurnaroundTimeInHours -= DateConstants.NUMBER_OF_WORKING_HOURS_PER_DAY;
        } else {
            remainingTurnaroundTimeInHours = 0;
        }
    }

    private void addNonWorkingHours(Calendar dueDateTime) {
        dueDateTime.add(
            Calendar.HOUR_OF_DAY, 
            DateConstants.NUMBER_OF_NON_WORKING_HOURS_PER_DAY);
    }

    private void addWeekendHours(Calendar dueDateTime) {
        dueDateTime.add(
            Calendar.HOUR_OF_DAY, 
            DateConstants.NUMBER_OF_WEEKEND_HOURS);
    }

    public boolean isWeekend(Calendar dueDateTime) {
        return (dueDateTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
            dueDateTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }
    
    private Calendar getTimeOf(Calendar actualDay, int hour) {
        Calendar time = Calendar.getInstance();
        int minute = 0;
        int second = 0;

        time.set(
            actualDay.get(Calendar.YEAR),
            actualDay.get(Calendar.MONTH),
            actualDay.get(Calendar.DAY_OF_MONTH),
            hour,
            minute,
            second);       
        
        return time;
    }
}
