package com.avispa.microf.model.customer.address;

import com.avispa.microf.model.base.dto.IDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class AddressDto implements IDto {
    public static final String VM_STREET_NOT_EMPTY_NOR_BLANK = "Street cannot be empty or blank";
    public static final String VM_PLACE_NOT_EMPTY_NOR_BLANK = "Place cannot be empty or blank";
    public static final String VM_ZIP_CODE_PATTERN_NOT_MATCH = "Zip code does not match specified pattern";

    private UUID id;

    @NotBlank(message = VM_STREET_NOT_EMPTY_NOR_BLANK)
    private String street;

    @NotBlank(message = VM_PLACE_NOT_EMPTY_NOR_BLANK)
    private String place;

    @Pattern(regexp = "[0-9]{2}-[0-9]{3}", message = VM_ZIP_CODE_PATTERN_NOT_MATCH)
    private String zipCode;
}
