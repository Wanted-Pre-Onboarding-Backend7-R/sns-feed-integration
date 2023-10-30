package com.wanted.teamr.snsfeedintegration.fixture;

public final class JwtFixture {


    public static String INVALID_AT = "Bearer eyJhbGci1NiJ9.eyJzdWXNCAig2NjAzNDB9.Tzd8X31";
    /**
     * exp = 1698660340 = Mon Oct 30 2023 19:05:40 UTC+0900 (한국 표준시)
     */
    public static String EXPIRED_AT_MEMBER1 = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0SWQiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjk4NjY0MDYwfQ.OIK6yD_WhUIFWlaTheY29OPfp24bJ6qIxacLAF21GQo";
    /**
     * exp = 32503647600 = Wed Jan 01 3000 00:00:00 UTC+0900 (한국 표준시)
     */
    public static String VAILD_AT_MEMBER1 = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0SWQiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjozMjUwMzY0NzYwMH0.UzzGTYcI6ioQ1euYsF21-CE5N0SPa5WtmQSnjUKVap8";
}
