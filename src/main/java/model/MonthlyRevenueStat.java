package model;

import java.io.Serializable;

public class MonthlyRevenueStat implements Serializable {
    private int month;
    private int year;
    private int totalRevenue;

    public MonthlyRevenueStat() {
        super();
    }

    public MonthlyRevenueStat(int month, int year, int totalRevenue) {
        this.month = month;
        this.year = year;
        this.totalRevenue = totalRevenue;
    }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(int totalRevenue) { this.totalRevenue = totalRevenue; }
}
