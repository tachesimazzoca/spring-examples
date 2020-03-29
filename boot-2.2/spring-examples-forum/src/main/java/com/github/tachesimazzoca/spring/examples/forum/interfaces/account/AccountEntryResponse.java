package com.github.tachesimazzoca.spring.examples.forum.interfaces.account;

import java.util.List;
import org.springframework.validation.ObjectError;
import lombok.Data;

@Data
public class AccountEntryResponse {

    private AccountEntryForm accountEntryForm;

    private List<ObjectError> errors;
}
