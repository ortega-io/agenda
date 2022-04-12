package org.kodigo.agenda.repositories;

import org.kodigo.agenda.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Integer> {
}