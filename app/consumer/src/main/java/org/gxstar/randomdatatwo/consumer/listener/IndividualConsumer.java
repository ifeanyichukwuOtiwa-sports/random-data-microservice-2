package org.gxstar.randomdatatwo.consumer.listener;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gxstar.randomdata.entity.DateOfBirth;
import org.gxstar.randomdata.entity.Person;
import org.gxstar.randomdata.entity.PersonName;
import org.gxstar.randomdatatwo.api.IndividualService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.gxstar.randomdatatwo.api.Messages.DateOfBirthProto;
import static org.gxstar.randomdatatwo.api.Messages.PersonNameProto;
import static org.gxstar.randomdatatwo.api.Messages.PersonProto;

@RequiredArgsConstructor
@Component
@Slf4j
public class IndividualConsumer {
    private final IndividualService service;

    @KafkaListener(groupId = "random-consumer", topics = "person.topic")
    public void process(@Payload final  byte[] message) {
        final Person person = parse(message);
        log.info("processing received person: {}", person);
        service.save(person);
    }

    private Person parse(final byte[] message) {
        try {
            final PersonProto proto = PersonProto.parseFrom(message);
            return convert(proto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Person convert(final PersonProto proto) {
        return new Person(
                proto.getGender(),
                convert(proto.getName()),
                convert(proto.getDob()),
                proto.getPhone(),
                proto.getEmail(),
                proto.getCell(),
                proto.getNat()
        );
    }

    private DateOfBirth convert(final DateOfBirthProto dob) {
        return new DateOfBirth(
                convert(dob.getDate()),
                dob.getAge());
    }

    private LocalDateTime convert(final Timestamp date) {
        final Instant instant = Instant.ofEpochSecond(date.getSeconds(), date.getNanos());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    private PersonName convert(final PersonNameProto name) {
        return new PersonName(
                name.getTitle(),
                name.getFirst(),
                name.getLast()
        );
    }
}
