package org.kodigo.agenda.repositories;

import org.kodigo.agenda.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query(
    value = "SELECT * FROM `appointments` WHERE (:starts >= starts && :starts <= ends OR :ends >= starts && :ends <= ends) OR (starts >= :starts && starts <= :ends AND ends >= :starts && ends <= :ends)",
    nativeQuery = true)
    List<Appointment> findConflictingAppointments(@Param("starts") Timestamp starts, @Param("ends") Timestamp ends);

}