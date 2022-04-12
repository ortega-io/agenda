package org.kodigo.agenda.repositories;

import org.kodigo.agenda.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
}