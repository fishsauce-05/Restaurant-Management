package model;

import java.io.Serializable;

public class GuestStat implements Serializable {
    private String timeFrm;
    private int avgCustomers;
    private double avgRevenuePerHead;
    private int totalRevenue;

    public GuestStat() {
        super();
    }

    public GuestStat(String timeFrm, int avgCustomers, double avgRevenuePerHead, int totalRevenue) {
        this.timeFrm = timeFrm;
        this.avgCustomers = avgCustomers;
        this.avgRevenuePerHead = avgRevenuePerHead;
        this.totalRevenue = totalRevenue;
    }

    public String getTimeFrm() { return timeFrm; }
    public void setTimeFrm(String timeFrm) { this.timeFrm = timeFrm; }

    public int getAvgCustomers() { return avgCustomers; }
    public void setAvgCustomers(int avgCustomers) { this.avgCustomers = avgCustomers; }

    public double getAvgRevenuePerHead() { return avgRevenuePerHead; }
    public void setAvgRevenuePerHead(double avgRevenuePerHead) { this.avgRevenuePerHead = avgRevenuePerHead; }

    public int getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(int totalRevenue) { this.totalRevenue = totalRevenue; }
}
