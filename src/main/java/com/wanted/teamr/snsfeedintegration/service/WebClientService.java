package com.wanted.teamr.snsfeedintegration.service;


import org.springframework.http.HttpMethod;

import java.net.http.HttpResponse;

/**
 * 게시물 좋아요, 공유 기능 개발을 위한 요소로 외부 API 통신 작업을 추상화한 interface 입니다.
 * 이번 과제에서는 외부 API 호출이 실제로 동작하지 않습니다.
 */
public interface WebClientService {

    /**
     * Http API 통신
     * <p>
     * 실제 API 통신 라이브러리에서는 Query Parameter, Header등의 정보도 포함해서 호출 하겠지만,
     * 게시물 좋아요, 공유 기능 개발을 위한 요소이므로 간추려서
     * URL과 Http Method만 입력 받게 했습니다.
     * @param url URL
     * @param method Http Method
     * @return Http Response
     */
    public HttpResponse send(String url, HttpMethod method);
}
