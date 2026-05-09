package com.clickandeat.api.authentication.mapper;

import com.clickandeat.api.authentication.dto.ProRegisterRequestDto;
import com.clickandeat.authentication.application.usecase.register.RegisterCommand;
import com.clickandeat.authentication.domain.valueobject.Role;
import com.clickandeat.shared.account.CreateProAccountRequest;
import com.clickandeat.shared.enums.RoleName;
import java.time.LocalDate;

public class ProRegisterRequestMapper {

  public static RegisterCommand toRegisterCommand(ProRegisterRequestDto dto) {
    return new RegisterCommand(
        dto.getEmail(),
        dto.getPassword(),
        dto.getFirstName(),
        dto.getLastName(),
        dto.getPhoneNumber(),
        LocalDate.parse(dto.getBirthDate()),
        new Role(RoleName.PRO, null));
  }

  public static CreateProAccountRequest toCreateProAccountRequest(ProRegisterRequestDto dto) {
    return new CreateProAccountRequest(
        dto.getFirstName(),
        dto.getLastName(),
        dto.getPhoneNumber(),
        LocalDate.parse(dto.getBirthDate()),
        dto.getKbisRef(),
        dto.getSiret(),
        dto.getLegalName(),
        dto.getLegalForm(),
        dto.getAddress1(),
        dto.getAddress2(),
        dto.getAddress3(),
        dto.getCity(),
        dto.getPostalCode(),
        dto.getCountry());
  }
}
