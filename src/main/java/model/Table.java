package model;

public class Table extends BaseEntity {
    private String tableCode;
    private String name;
    private String checkinTime;
    private int capacity;
    private String description;
    private boolean active = true;

    public static final String STATUS_EMPTY = "Trống";
    public static final String STATUS_SERVING = "Đang phục vụ";

    public Table() {
        super();
    }

    public Table(int id, String tableCode, String name, int capacity, String status) {
        super(id, status);
        this.tableCode = tableCode;
        this.name = name;
        this.capacity = capacity;
    }

    public String getTableCode() { return tableCode; }
    public void setTableCode(String tableCode) { this.tableCode = tableCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCode() { return tableCode; }
    public void setCode(String code) { this.tableCode = code; }

    public int getMaxNumberOfClients() { return capacity; }
    public void setMaxNumberOfClients(int maxNumberOfClients) { this.capacity = maxNumberOfClients; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getCheckinTime(){
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = formatCheckinTime(checkinTime);
    }

    private String formatCheckinTime(String raw) {
        if (raw == null) {
            return null;
        }
        String digitsOnly = raw.replace(",", "").trim();
        if (digitsOnly.matches("\\d{14}")) {
            String yyyy = digitsOnly.substring(0, 4);
            String MM   = digitsOnly.substring(4, 6);
            String dd   = digitsOnly.substring(6, 8);
            String HH   = digitsOnly.substring(8, 10);
            String mm   = digitsOnly.substring(10, 12);
            return HH + ":" + mm + " " + dd + "/" + MM + "/" + yyyy;
        }
        return raw;
    }

    @Override
    public String toString() {
        return tableCode + " - " + name + " (Sức chứa: " + capacity + ") - " + getStatus();
    }
}