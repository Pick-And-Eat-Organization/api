package com.clickandeat.api.account.exception;

import com.clickandeat.account.application.exceptions.application.AccountNotFoundException;
import com.clickandeat.api.account.controllers.CurrentAccountController;
import com.clickandeat.api.shared.ErrorApiResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {CurrentAccountController.class})
public class AccountExceptionHandler {

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<ErrorApiResponse> handleAccountNotFoundException(
      AccountNotFoundException ex) {
    return ResponseEntity.status(HttpStatusCode.valueOf(404))
        .body(new ErrorApiResponse(ex.getKey(), ex.getMessage(), 404, null));
  }
}
