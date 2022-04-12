package org.kodigo.agenda.repositories;

import org.kodigo.agenda.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Integer> {
}