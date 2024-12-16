package com.KMA.BookingCare.Api.form;

public class ChangeTimeCloseForm {
    private Long idWk;
    private Long idMedical;

    public Long getIdWk() {
        return idWk;
    }

    public void setIdWk(Long idWk) {
        this.idWk = idWk;
    }

    public Long getIdMedical() {
        return idMedical;
    }

    public void setIdMedical(Long idMedical) {
        this.idMedical = idMedical;
    }

    public ChangeTimeCloseForm() {
    }

    public ChangeTimeCloseForm(Long idWk, Long idMedical) {
        this.idWk = idWk;
        this.idMedical = idMedical;
    }
}
