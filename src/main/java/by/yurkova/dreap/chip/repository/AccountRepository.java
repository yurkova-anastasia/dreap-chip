package by.yurkova.dreap.chip.repository;

import static java.lang.String.format;

import by.yurkova.dreap.chip.dto.AccountDto;
import by.yurkova.dreap.chip.dto.AccountRequest;
import by.yurkova.dreap.chip.exception.DreapChipException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static final String FIND_ACCOUNT_BY_ID = """
        SELECT id, first_name, last_name, email
        FROM accounts
        WHERE id = :id
    """;

    public static final String FIND_ACCOUNT = """
        SELECT id, first_name, last_name, email
        FROM accounts
        WHERE (:firstName IS NULL OR LOWER(first_name) LIKE LOWER(:firstName))
            AND (:lastName IS NULL OR LOWER(last_name) LIKE LOWER(:lastName))
            AND (:email IS NULL OR LOWER(email) LIKE LOWER(:email))
        ORDER BY id ASC
        LIMIT :size
        OFFSET :from
    """;

    public static final String SAVE = """
        INSERT INTO accounts (first_name, last_name, email, password)
        VALUES (:firstName, :lastName, :email, :password)
    """;

    public static final String UPDATE = """
        UPDATE accounts
        SET first_name = :firstName, last_name = :lastName, email = :email, password = :password
        WHERE id = :id
    """;

    public static final String DELETE = """
        DELETE FROM accounts
        WHERE id = :id
    """;

    public static final String EXISTS_ACCOUNT = """
        SELECT EXISTS (SELECT * FROM accounts WHERE email = :email);
    """;

    public static final String HAS_ANIMAL = """
        SELECT EXISTS (SELECT * FROM Animal WHERE account_id = :id);
    """;

    public AccountDto findById(Integer accountId) {
        var parameters = new MapSqlParameterSource("id", accountId);
        try {
            return namedParameterJdbcTemplate.queryForObject(FIND_ACCOUNT_BY_ID, parameters, this::buildAccountDto);
        } catch (EmptyResultDataAccessException e) {
            var message = "Account with the following accountId = '%s' does not found";
            throw new DreapChipException(format(message, accountId), HttpStatus.NOT_FOUND);
        }
    }

    private AccountDto buildAccountDto(ResultSet rs, int rowNum) throws SQLException {
        return new AccountDto()
            .setId(rs.getInt("id"))
            .setFirstName(rs.getString("first_name"))
            .setLastName(rs.getString("last_name"))
            .setEmail(rs.getString("email"));
    }

    public List<AccountDto> search(AccountRequest accountRequest) {
        var parameters = new MapSqlParameterSource()
            .addValue("firstName", accountRequest.getFirstName())
            .addValue("lastName", accountRequest.getLastName())
            .addValue("email", accountRequest.getEmail())
            .addValue("from", accountRequest.getFrom())
            .addValue("size", accountRequest.getSize());
        return namedParameterJdbcTemplate.query(FIND_ACCOUNT, parameters, this::buildAccountDto);
    }

    public AccountDto save(AccountDto accountDto) {
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SAVE, fillParameters(accountDto), keyHolder, new String[]{"id"});
        return accountDto.setId((Integer) keyHolder.getKey());
    }

    private MapSqlParameterSource fillParameters(AccountDto accountDto) {
        return new MapSqlParameterSource()
            .addValue("firstName", accountDto.getFirstName())
            .addValue("lastName", accountDto.getLastName())
            .addValue("email", accountDto.getEmail())
            .addValue("password", accountDto.getPassword());
    }

    public AccountDto update(AccountDto accountDto) {
        var updatedRow = namedParameterJdbcTemplate.update(UPDATE, fillParameters(accountDto));
        if (updatedRow != 1) {
            var message = "Account with the following email = '%s' does not found";
            throw new DreapChipException(format(message, accountDto.getEmail()), HttpStatus.FORBIDDEN);
        }
        return accountDto;
    }

    public void delete(Integer accountId) {
        var parameters = new MapSqlParameterSource("id", accountId);
        var updatedRow = namedParameterJdbcTemplate.update(DELETE, parameters);
        if (updatedRow != 1) {
            var message = "Account with the following accountId = '%s' does not found";
            throw new DreapChipException(format(message, accountId), HttpStatus.FORBIDDEN);
        }
    }

    public Boolean exists(String email) {
        var parameters = new MapSqlParameterSource("email", email);
        return namedParameterJdbcTemplate.queryForObject(EXISTS_ACCOUNT, parameters, Boolean.class);
    }

    public Boolean hasAnimal(Integer id) {
        var parameters = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(HAS_ANIMAL, parameters, Boolean.class);
    }
}
