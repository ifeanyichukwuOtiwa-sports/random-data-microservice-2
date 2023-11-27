package org.gxstar.randomdatatwo.api;

import org.gxstar.randomdatatwo.api.dto.IndividualDto;
import org.gxstar.randomdata.entity.Person;

import java.time.LocalDateTime;
import java.util.List;

public interface IndividualService {
    void save(Person person);
    List<IndividualDto> getAll();
    List<IndividualDto> getAllCreatedBefore(LocalDateTime beforeTime);
    List<IndividualDto> getAllCreatedAfter(LocalDateTime afterTime);
}
