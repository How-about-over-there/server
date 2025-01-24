package com.haot.lodge.util;


import com.haot.lodge.application.dto.LodgeDateDto;
import com.haot.lodge.application.dto.LodgeDto;
import com.haot.lodge.application.dto.LodgeImageDto;
import com.haot.lodge.application.dto.LodgeRuleDto;

public class TestDtoFixture {

    public static LodgeDto createMockLodgeDto() {
        return LodgeDto.builder()
                .id("LodgeUUID")
                .hostId("UserUUID")
                .name("해변가 숙소")
                .description("아름다운 해변가 숙소입니다.")
                .address("123 Ocean View Drive")
                .term(7)
                .basicPrice(2500.0)
                .build();
    }

    public static LodgeImageDto createMockLodgeImageDto() {
        return LodgeImageDto.builder()
                .id("ImageUUID")
                .title("숙소 사진")
                .description("밖에서 볼 수 있는 숙소 사진입니다.")
                .url("https://example.com/image1.jpg")
                .build();
    }

    public static LodgeRuleDto createMockLodgeRuleDto() {
        return LodgeRuleDto.builder()
                .id("RuleUUID")
                .maxReservationDay(10)
                .maxPersonnel(5)
                .customization("애완견 출입 금지입니다.")
                .build();
    }

}
