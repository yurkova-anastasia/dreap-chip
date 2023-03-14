package by.yurkova.dreap.chip.service;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.hasText;

import by.yurkova.dreap.chip.dto.AccountDto;
import by.yurkova.dreap.chip.dto.AccountRequest;
import by.yurkova.dreap.chip.exception.DreapChipException;
import by.yurkova.dreap.chip.repository.AccountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountDto findById(Integer accountId) {
        if (accountId == null || accountId <= 0) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        return accountRepository.findById(accountId);
    }

    public List<AccountDto> search(AccountRequest accountRequest) {
        if (accountRequest.getFrom() < 0 || accountRequest.getSize() <= 0) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        return accountRepository.search(accountRequest);
    }

    public AccountDto save(AccountDto accountDto) {
        validateAccountDto(accountDto);
        return accountRepository.save(accountDto);
    }

    private void validateAccountDto(AccountDto accountDto) {
        if (!hasText(accountDto.getFirstName()) || !hasText(accountDto.getLastName())
            || !hasText(accountDto.getEmail()) || !hasText(accountDto.getPassword())
        ) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        if (accountRepository.exists(accountDto.getEmail())) {
            var message = "Account has already existed with the following email = '%s'";
            throw new DreapChipException(format(message, accountDto.getEmail()), HttpStatus.CONFLICT);
        }
    }

    public AccountDto update(AccountDto accountDto) {
        validateAccountDto(accountDto);
        return accountRepository.update(accountDto);
    }

    public void delete(Integer accountId) {
        if (accountId == null || accountId <= 0) {
            throw new DreapChipException(HttpStatus.BAD_REQUEST);
        }
        if (accountRepository.hasAnimal(accountId)) {
            throw new DreapChipException("Account linked with animal", HttpStatus.BAD_REQUEST);
        }
        accountRepository.delete(accountId);
    }
}
