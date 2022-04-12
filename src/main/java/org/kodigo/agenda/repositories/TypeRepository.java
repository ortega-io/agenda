package org.kodigo.agenda.repositories;

import org.kodigo.agenda.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Integer> {
}