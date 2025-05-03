package vn.minhquang.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.minhquang.jobhunter.domain.RestResponse;

@ControllerAdvice
public class FormatResponse implements ResponseBodyAdvice<Object> {

  // when format response => all response will be format by this method
  @Override
  public boolean supports(MethodParameter returnType, Class converterType) {
    return true;
  }

  // format response
  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
      Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

    HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();

    int status = httpServletResponse.getStatus();

    RestResponse<Object> res = new RestResponse<Object>();
    res.setStatusCode(status);

    // nếu body là String thì không cần format lại. tránh lỗi vì yêu cầu đầu vào là object
    if (body instanceof String) {
      return body;
    }

    if (status >= 400) {
      return body;
    } else {
      // case success
      res.setMessage("call api success");
      res.setData(body);
    }

    return res;
  }
}
