package org.gxstar.randomdatatwo.grpc.server.service;

import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.gxstar.randomdatatwo.api.IndividualService;
import org.gxstar.randomdatatwo.api.IndividualServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.gxstar.randomdatatwo.api.dto.IndividualDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.gxstar.randomdatatwo.api.Individual.IndividualRequest;
import static org.gxstar.randomdatatwo.api.Individual.IndividualsResponseProto;
import static org.gxstar.randomdatatwo.api.Individual.IndividualResponseProto;
import static org.gxstar.randomdatatwo.api.Individual.TimeRequest;

@RequiredArgsConstructor
@Service
@Slf4j
public class IndividualGrpcServer extends IndividualServiceGrpc.IndividualServiceImplBase {
    private final IndividualService individualService;

    @Override
    public void streamIndividuals(final IndividualRequest request, final StreamObserver<IndividualsResponseProto> responseObserver) {
        getIndividualsByMapperFunction(responseObserver, individualService::getAll);
    }

    @Override
    public void getIndividualsCreatedBefore(final TimeRequest request, final StreamObserver<IndividualsResponseProto> responseObserver) {
        final LocalDateTime localDateTime = toLocalDateTime(request.getTime());


        getIndividualsByMapperFunction(localDateTime, responseObserver, individualService::getAllCreatedBefore);
    }

    @Override
    public void getIndividualsCreatedAfter(final TimeRequest request, final StreamObserver<IndividualsResponseProto> responseObserver) {
        final LocalDateTime localDateTime = toLocalDateTime(request.getTime());


        getIndividualsByMapperFunction(localDateTime, responseObserver, individualService::getAllCreatedAfter);
    }

    private void getIndividualsByMapperFunction(final StreamObserver<IndividualsResponseProto> responseObserver, final Supplier<List<IndividualDto>> function) {
        final List<IndividualDto> dtos = function.get();
        processRequest(responseObserver, dtos);
    }

    private void getIndividualsByMapperFunction(final LocalDateTime time, final StreamObserver<IndividualsResponseProto> responseObserver, final Function<LocalDateTime, List<IndividualDto>> function) {
        final List<IndividualDto> dtos = function.apply(time);
        processRequest(responseObserver, dtos);
    }
    private void processRequest(final StreamObserver<IndividualsResponseProto> responseObserver, final List<IndividualDto> dtos) {
        final Iterable<IndividualResponseProto> protoList = dtos.stream()
                .map(this::map)
                .toList();


        final IndividualsResponseProto response = IndividualsResponseProto.newBuilder()
                .addAllItems(protoList)
                .build();
       try {
           responseObserver.onNext(response);
           responseObserver.onCompleted();
       } catch (Exception e) {
           responseObserver.onError(Status.INTERNAL.withDescription("Error processing request").asException());
       }
    }

    private IndividualResponseProto map(IndividualDto dto) {
        return IndividualResponseProto.newBuilder()
                .setAge(dto.age())
                .setEmail(dto.email())
                .setFirstName(dto.firstName())
                .setGender(dto.gender())
                .setLastName(dto.lastName())
                .setCreatedAt(toTimestamp(dto.createdAt()))
                .build();
    }

    private LocalDateTime toLocalDateTime(final Timestamp before) {

        final long seconds = before.getSeconds();
        final int nanos = before.getNanos();

        final Instant instant = Instant.ofEpochSecond(seconds, nanos);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    private Timestamp toTimestamp(final LocalDateTime time) {
        final Instant instant = time.atZone(ZoneId.systemDefault())
                .toInstant();
        return Timestamp.newBuilder()
                .setNanos(instant.getNano())
                .setSeconds(instant.getEpochSecond())
                .build();
    }

}
