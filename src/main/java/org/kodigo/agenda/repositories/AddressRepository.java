package org.kodigo.agenda.repositories;

import org.kodigo.agenda.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}