package com.avispa.microf.model.customer;

import com.avispa.microf.model.base.dto.Dto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public abstract class CustomerDto implements Dto {
    public static final String VM_PHONE_PATTERN_NOT_MATCH = "Phone does not match specified pattern";
    public static final String VM_EMAIL_INVALID = "Email address is invalid";
    public static final String VM_ADDRESS_NOT_NULL = "Address cannot be null";

    private UUID id;

    @Pattern(regexp = EMPTY_STRING_REGEX + "(\\+48 \\d{9})", message = VM_PHONE_PATTERN_NOT_MATCH)
    private String phoneNumber;

    @Email(message = VM_EMAIL_INVALID)
    private String email;

    @NotNull(message = VM_ADDRESS_NOT_NULL)
    private AddressDto address;
}
