package com.example.kare.common.filter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;


@Slf4j
@WebFilter(urlPatterns = {"/signup/*", "/v1/*"})
@RequiredArgsConstructor
public class TransactionLogFilter implements Filter {
    private final JdbcTemplate jdbcTemplate;

    private static final String sql =
            "INSERT INTO transaction_log (transaction_id, url, request, status, response, start_time, end_time, elapse_time) " +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void init(FilterConfig filterConfig) {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LocalDateTime startTime = LocalDateTime.now();

        // ContentCachingRequestWrapper, ContentCachingResponseWrapper
        // Filter에서 body를 한 번밖에 읽지 못하는 불편함을 해소하기 위한 클래스
        // 다만 초기화 시점에는 body의 내용을 넣지는 않고, 스프링 호출 이후(chain.doFilter(httpServletRequest, httpServletResponse))에 body의 data를 read한다.
        // 때문에 chain.doFilter(httpServletRequest, httpServletResponse); 이후에 로직을 사용해야 한다.
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse)response);

        chain.doFilter(httpServletRequest, httpServletResponse);

        LocalDateTime endTime = LocalDateTime.now();

        String url = ((HttpServletRequest) request).getRequestURI();
        log.info("url : {} ", url);

        String reqContent = new String(httpServletRequest.getContentAsByteArray());

        log.info("request content : {}" , reqContent);

        int httpStatus = httpServletResponse.getStatus();
        String resContent = new String(httpServletResponse.getContentAsByteArray());

        // 위에서 response body stream을 읽기 때문에, copyBodyToResponse 메소드로 다시 복사
        // 그래야 response에 값이 넘어간다.
        httpServletResponse.copyBodyToResponse();
        log.info("response status : {} content : {}", httpStatus, resContent);

        Duration elapseTime = Duration.between(startTime, endTime);

        jdbcTemplate.update(sql, UUID.randomUUID(), url, reqContent, httpStatus, resContent, startTime, endTime, elapseTime.toMillis());
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}