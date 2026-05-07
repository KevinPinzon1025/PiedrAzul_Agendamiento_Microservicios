package co.unicauca.frontend.dto;

public class PatientDTO {
    private long id;
    private String patName;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getPatName() {
        return patName;
    }
    public void setPatName(String patName) {
        this.patName = patName;
    }
}