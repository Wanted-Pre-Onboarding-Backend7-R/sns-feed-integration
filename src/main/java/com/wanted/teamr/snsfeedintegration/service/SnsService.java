package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.config.ExternalApiConfig;
import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.domain.SnsType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 외부 SNS 서비스에서 동작하는 좋아요, 공유 기능 등을 처리합니다.
 * <p>
 * 본 서비스가 아닌 외부 SNS 서비스의 기능을 실제로 동작하기 위해 외부 API를 호출합니다.
 */
@Service
@RequiredArgsConstructor
public class SnsService {
    private final WebClientService webClientService;
    private final ExternalApiConfig externalApiConfig;

    /**
     * 본 서비스가 아닌 실제 외부 SNS의 좋아요 API를 호출하여 좋아요 기능을 처리합니다.
     *
     * @param contentId 게시물이 해당하는 SNS에서 관리되는 고유 인식 값
     * @param snsType   SNS 서비스 종류
     * @param member 게시물 좋아요를 요청한 유저 정보
     * @return 외부 SNS 서비스 좋아요 기능 동작 성공 여부
     */
    public boolean likePost(String contentId, SnsType snsType, Member member) {
        String domain = externalApiConfig.getSnsApiDomains()
                                         .get(snsType.name());
        String path = String.format(externalApiConfig.getPostPath()
                                                     .get("like"), contentId);
        String url = String.format("%s%s", domain, path);

        /**
         * 게시물 좋아요를 요청한 유저 정보는 본 서비스에 좋아요 요청 내역을 저장하거나
         * 외부 SNS 좋아요 API 호출할 때 필요한 정보겠지만 현재 개발 사항에서는
         * 아직 member entity를 직접 다루는 로직이 없습니다
         */

        /**
         * 외부 API 통신에 대한 로직을 보여주기 위한 코드로
         * webClientService구현체 webClientServiceImpl에서 send 함수가 항상 null을 반환합니다.
         * 그래서 아래 코드의 주석 처리를 지우면 외부 API 통신에 대해 항상 실패합니다.
         */
        // TODO: 외부 API 호출 통신 구현
        /*HttpResponse response = webClientService.send(url, HttpMethod.POST);
        int httpStatus = response.statusCode();
        if (httpStatus != HttpStatus.OK.value()) {
            // throw exception
        }*/

        return true;
    }

    /**
     * 본 서비스가 아닌 실제 외부 SNS의 공유 API를 호출하여 공유 기능을 처리합니다.
     *
     * @param contentId 게시물이 해당하는 SNS에서 관리되는 고유 인식 값
     * @param snsType   SNS 종류
     * @param member 게시물 좋아요를 요청한 유저 정보
     * @return 외부 SNS 게시물 공유 기능 동작 성공 여부
     */
    public boolean sharePost(String contentId, SnsType snsType, Member member) {
        String domain = externalApiConfig.getSnsApiDomains()
                                         .get(snsType.name());
        String path = String.format(externalApiConfig.getPostPath()
                                                     .get("share"), contentId);
        String url = String.format("%s%s", domain, path);

        /**
         * 게시물 공유를 요청한 유저 정보는 본 서비스에 공유 요청 내역을 저장하거나
         * 외부 SNS 공유 API 호출할 때 필요한 정보겠지만 현재 개발 사항에서는
         * 아직 member entity를 직접 다루는 로직이 없습니다
         */

        /**
         * 외부 API 통신에 대한 로직을 보여주기 위한 코드로
         * webClientService구현체 webClientServiceImpl에서 send 함수가 항상 null을 반환합니다.
         * 그래서 아래 코드의 주석 처리를 지우면 외부 API 통신에 대해 항상 실패합니다.
         */
        // TODO: 외부 API 호출 구현
        /*HttpResponse response = webClientService.send(url, HttpMethod.POST);
        int httpStatus = response.statusCode();
        if (httpStatus != HttpStatus.OK.value()) {
            // throw exception
        }*/

        return true;
    }
}
