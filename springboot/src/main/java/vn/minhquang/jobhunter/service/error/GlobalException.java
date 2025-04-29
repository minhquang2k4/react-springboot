package vn.minhquang.jobhunter.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.minhquang.jobhunter.domain.RestResponse;

@RestControllerAdvice //** Global exception handler for the application. */
public class GlobalException {
  
  @ExceptionHandler(value = IdInvalidException.class)
  public ResponseEntity<RestResponse<Object>> handleIdInvalidException(IdInvalidException e) {
    RestResponse<Object> res = new RestResponse<Object>();

    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError(e.getMessage());
    res.setMessage("IdInvalidException");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

}
