package org.gxstar.randomdatatwo.service;

import org.gxstar.randomdatatwo.api.IndividualService;
import org.gxstar.randomdatatwo.api.dto.IndividualDto;
import org.gxstar.randomdatatwo.persistence.repository.entity.Individual;
import org.gxstar.randomdatatwo.persistence.repository.IndividualRepository;
import lombok.RequiredArgsConstructor;
import org.gxstar.randomdata.entity.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndividualServiceImpl implements IndividualService {
    private final IndividualRepository individualRepository;
    @Override
    public void save(final Person person) {
        individualRepository.save(person);
    }

    @Override
    public List<IndividualDto> getAll() {
        final List<Individual> entities = individualRepository.getAll();
        return entities.stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<IndividualDto> getAllCreatedBefore(final LocalDateTime beforeTime) {
        final List<Individual> individuals = individualRepository.getAllIndividualsBefore(beforeTime);
        return individuals.stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<IndividualDto> getAllCreatedAfter(final LocalDateTime afterTime) {
        final List<Individual> individuals = individualRepository.getAllIndividualsAfter(afterTime);
        return individuals.stream()
                .map(this::map)
                .toList();
    }

    private IndividualDto map(Individual individual) {
        return IndividualDto.of(
                individual.getFirstName(),
                individual.getLastName(),
                individual.getEmail(),
                individual.getAge(),
                individual.getGender(),
                individual.getCreatedAt()
        );
    }
}
