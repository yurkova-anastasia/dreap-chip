package by.yurkova.dreap.chip.web;

import by.yurkova.dreap.chip.dto.AccountDto;
import by.yurkova.dreap.chip.dto.AccountRequest;
import by.yurkova.dreap.chip.service.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/accounts/{accountId}")
    public AccountDto findById(
        @PathVariable Integer accountId
    ) {
        return accountService.findById(accountId);
    }

    @GetMapping("/accounts/search")
    public List<AccountDto> search(
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false, defaultValue = "0") int from,
        @RequestParam(required = false, defaultValue = "10") int size
    ) {
        var accountRequest = new AccountRequest()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setEmail(email)
            .setFrom(from)
            .setSize(size);
        return accountService.search(accountRequest);
    }

    @PostMapping("/registration")
    public AccountDto registration(
        @RequestBody AccountDto accountDto
    ) {
        return accountService.save(accountDto);
    }

    @PutMapping("/accounts/{accountId}")
    public AccountDto update(
        @PathVariable Integer accountId,
        @RequestBody AccountDto accountDto
    ) {
        return accountService.update(accountDto.setId(accountId));
    }

    @DeleteMapping("/accounts/{accountId}")
    public void delete(
        @PathVariable Integer accountId
    ) {
        accountService.delete(accountId);
    }
}
