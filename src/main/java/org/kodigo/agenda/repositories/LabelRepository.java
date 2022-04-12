package org.kodigo.agenda.repositories;

import org.kodigo.agenda.entities.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Integer> {
}