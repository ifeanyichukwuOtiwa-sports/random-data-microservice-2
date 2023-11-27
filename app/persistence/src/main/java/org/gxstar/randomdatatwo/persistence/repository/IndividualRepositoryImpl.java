package org.gxstar.randomdatatwo.persistence.repository;

import org.gxstar.randomdatatwo.persistence.repository.entity.Individual;
import lombok.RequiredArgsConstructor;
import org.gxstar.randomdata.entity.Person;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IndividualRepositoryImpl implements IndividualRepository {
    private final JdbcClient jdbcClient;
    @Override
    @Transactional
    public void save(final Person person) {
        final String sql = """
                INSERT INTO individual (first_name, last_name, email, phone_number, age, date_of_birth, gender, nationality, title)
                 VALUES (:firstName, :lastName, :email, :phoneNumber, :age, :dateOfBirth, :gender, :nationality, :title)
                """;
        final SqlParameterSource params = new MapSqlParameterSource("firstName", person.name().first())
                .addValue("lastName", person.name().last())
                .addValue("title", person.name().title())
                .addValue("email", person.email())
                .addValue("phoneNumber", person.phone())
                .addValue("age", person.dob().age())
                .addValue("gender", person.gender())
                .addValue("nationality", person.nat())
                .addValue("dateOfBirth", person.dob().date());

        jdbcClient.sql(sql)
                .paramSource(params)
                .update();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Individual> getAll() {
        final String sql = """
                SELECT id, first_name, last_name, email, title, gender, nationality, phone_number, age, date_of_birth, created_at
                FROM individual
                """;
        return jdbcClient.sql(sql)
                .query(rowMapper())
                .list();
    }

    @Override
    public List<Individual> getAllIndividualsBefore(final LocalDateTime beforeTime) {
        final String sql = """
                SELECT id, first_name, last_name, title, email, gender, nationality, phone_number, age, date_of_birth, created_at
                FROM individual
                WHERE created_at IS BEFORE :time
                """;
        final SqlParameterSource params = new MapSqlParameterSource("firstName", beforeTime);
        return jdbcClient.sql(sql)
                .paramSource(params)
                .query(rowMapper())
                .list();
    }

    @Override
    public List<Individual> getAllIndividualsAfter(final LocalDateTime afterTime) {

        final String sql = """
                SELECT id, first_name, last_name, title, email, gender, nationality, phone_number, age, date_of_birth, created_at
                FROM individual
                WHERE created_at IS AFTER :time
                """;
        final SqlParameterSource params = new MapSqlParameterSource("firstName", afterTime);
        return jdbcClient.sql(sql)
                .paramSource(params)
                .query(rowMapper())
                .list();

    }

    private RowMapper<Individual> rowMapper() {
        return ((rs, rowNum) -> new Individual(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("gender"),
                rs.getString("nationality"),
                rs.getInt("age"),
                rs.getTimestamp("date_of_birth").toLocalDateTime(),
                rs.getTimestamp("created_at").toLocalDateTime()
        ));
    }
}
