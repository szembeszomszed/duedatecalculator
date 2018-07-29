/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.retsagimate.duedatecalculator;

/**
 *
 * @author retsagimate
 */
public class DateConstants {
    
    public static final String INPUT_DATE_FORMAT = "dd-MM-yyyy KK:mma";
    
    public static final int WORKING_DAY_START_HOUR = 9;
    
    public static final int WORKING_DAY_END_HOUR = 17;
    
    public static final int NUMBER_OF_WEEKEND_HOURS = 48;
    
    public static final int NUMBER_OF_WORKING_HOURS_PER_DAY = 
            WORKING_DAY_END_HOUR - WORKING_DAY_START_HOUR;
    
    public static final int NUMBER_OF_NON_WORKING_HOURS_PER_DAY = 
            24 - NUMBER_OF_WORKING_HOURS_PER_DAY;
    
    public static final long ONE_HOUR_IN_MILLISECONDS = 3600 * 1000;    
    
}
