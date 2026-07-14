package model;

import java.io.Serializable;

public class BaseEntity implements Serializable {
    private int id;
    private String status;

    public BaseEntity() {
        super();
    }

    public BaseEntity(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
