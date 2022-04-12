package org.kodigo.agenda.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "contact", nullable = false)
    private Integer contact;

    @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "starts", nullable = false)
    private Timestamp starts;

    @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ends", nullable = false)
    private Timestamp ends;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContact() {
        return contact;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
    }

    public Timestamp getStarts() {
        return starts;
    }

    public void setStarts(Timestamp starts) {
        this.starts = starts;
    }

    public Timestamp getEnds() {
        return ends;
    }

    public void setEnds(Timestamp ends) {
        this.ends = ends;
    }

}