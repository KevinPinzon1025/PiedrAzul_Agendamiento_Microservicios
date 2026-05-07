package co.unicauca.frontend.dto;


public class ProfessionalDTO {
    private Long id;
    private String profName;

    public ProfessionalDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfName() {
        return profName;
    }

    public void setProfName(String profName) {
        this.profName = profName;
    }

}