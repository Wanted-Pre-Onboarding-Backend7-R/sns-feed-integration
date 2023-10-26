package com.wanted.teamr.snsfeedintegration.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * application.yml에 저장된 데이터 중 {@code @ConfigurationProperties(prefix = ~~~)}에서
 * prefix값으로 선언된 데이터를 불러옵니다.
 * <p>
 * 외부 API 호출에 대한 정보(2023-10-26 기준 domain 정보만)를 yaml파일로 관리하려고
 * 생성했으며, sns api 기준 {@code Map<String, String>} 타입으로 관리됩니다.
 * <p>
 * sns api 기준으로 {@code snsApiDomains.get("instagram")}으로 조회하면
 * {@code "https://www.facebook.com/"}을 반환합니다.
 */
@Getter
@Component
@ConfigurationProperties(prefix = "external-api")
public class ExternalApiConfig {
    private Map<String, String> snsApiDomains;

    private Map<String, String> postPath;

    /**
     * Set 함수를 만드는 것을 선호하진 않으나 Set 함수가 없을 때 다음 예외가 발생했습니다.
     * java.lang.IllegalStateException: No setter found for property: sns-api-domains
     * <p>
     * {@code @ConfigurationProperties}로 property를 읽어올 때 Set 함수가 필요한지 set 함수를 만들고
     * 나서 정상 작동 했습니다.
     *
     * @param snsApiDomains
     */
    public void setSnsApiDomains(Map<String, String> snsApiDomains) {
        this.snsApiDomains = snsApiDomains;
    }

    public void setPostPath(Map<String, String> postPath) {
        this.postPath = postPath;
    }
}
