package com.clickandeat.api.authentication.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.clickandeat.api.authentication.dto.ProRegisterRequestDto;
import com.clickandeat.authentication.application.usecase.register.RegisterCommand;
import com.clickandeat.shared.account.CreateProAccountRequest;
import com.clickandeat.shared.enums.RoleName;
import java.time.LocalDate;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class ProRegisterRequestMapperUnitTest {

  @Test
  void shouldMapProRegisterRequestDtoToRegisterCommand() {
    ProRegisterRequestDto dto =
        new ProRegisterRequestDto(
            "pro@example.com",
            "securePass123",
            "John",
            "Doe",
            "0123456789",
            "1995-01-01",
            "KBIS-123",
            "12345678900011",
            "Click and Eat",
            "SAS",
            "1 rue de Paris",
            "Batiment A",
            "Etage 1",
            "Paris",
            "75001",
            "France");

    RegisterCommand command = ProRegisterRequestMapper.toRegisterCommand(dto);

    assertEquals("pro@example.com", command.email());
    assertEquals(RoleName.PRO, command.role().name());
    assertEquals(LocalDate.of(1995, 1, 1), command.birthDate());
  }

  @Test
  void shouldMapProRegisterRequestDtoToCreateProAccountRequest() {
    ProRegisterRequestDto dto =
        new ProRegisterRequestDto(
            "pro@example.com",
            "securePass123",
            "John",
            "Doe",
            "0123456789",
            "1995-01-01",
            "KBIS-123",
            "12345678900011",
            "Click and Eat",
            "SAS",
            "1 rue de Paris",
            "Batiment A",
            "Etage 1",
            "Paris",
            "75001",
            "France");

    CreateProAccountRequest request = ProRegisterRequestMapper.toCreateProAccountRequest(dto);

    assertEquals("John", request.firstName());
    assertEquals("Doe", request.lastName());
    assertEquals("KBIS-123", request.kbisRef());
    assertEquals("France", request.country());
  }
}
