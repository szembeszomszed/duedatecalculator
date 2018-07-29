/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.retsagimate.duedatecalculator;

import java.util.Date;

/**
 *
 * @author retsagimate
 */
public class Main {
    public static void main(String[] args) {
        DueDateCalculator dueDateCalculator = new DueDateCalculator("01-08-2018 5:00PM", 20);
        Date dueDate = dueDateCalculator.calculateDueDate();
        System.out.println("Due date: " + dueDate);
    }
}
