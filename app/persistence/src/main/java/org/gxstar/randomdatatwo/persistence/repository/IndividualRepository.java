package org.gxstar.randomdatatwo.persistence.repository;

import org.gxstar.randomdatatwo.persistence.repository.entity.Individual;
import org.gxstar.randomdata.entity.Person;

import java.time.LocalDateTime;
import java.util.List;

public interface IndividualRepository {
    void save(Person person);
    List<Individual> getAll();
    List<Individual> getAllIndividualsBefore(LocalDateTime beforeTime);
    List<Individual> getAllIndividualsAfter(LocalDateTime afterTime);
}
