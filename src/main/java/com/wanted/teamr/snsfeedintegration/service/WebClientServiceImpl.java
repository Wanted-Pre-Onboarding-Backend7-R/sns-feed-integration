package com.wanted.teamr.snsfeedintegration.service;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;

@Service
public class WebClientServiceImpl implements WebClientService {
    // private final WebClient webClient; // 외부 서버와 통신하기 위해 선택한 라이브러리

    @Override
    public HttpResponse send(String url, HttpMethod method) {
        // TODO: 외부 서버 통신 라이브러리에 맞게 API 통신 구현
        return null;
    }
}
