package org.gxstar.randomdatatwo.api.dto;

import java.time.LocalDateTime;

public record IndividualDto(
        String firstName,
        String lastName,
        String email,
        int age,
        String gender,
        LocalDateTime createdAt
) {
    public static IndividualDto of(final String firstName, final String lastName, final String email, final int age, final String gender, LocalDateTime time) {
        return new IndividualDto(firstName, lastName, email, age, gender, time);
    }
}
