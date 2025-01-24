package com.haot.lodge.util;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import com.haot.lodge.domain.model.Lodge;
import com.haot.lodge.domain.model.LodgeImage;
import com.haot.lodge.domain.model.LodgeRule;
import java.util.List;

public class TestEntityFixture {

    public static Lodge createMockLodge(String lodgeId) {
        Lodge lodge = mock(Lodge.class);
        lenient().when(lodge.getId()).thenReturn(lodgeId);
        lenient().when(lodge.getHostId()).thenReturn("HostUUID");
        lenient().when(lodge.getName()).thenReturn("해변가 숙소");
        lenient().when(lodge.getDescription()).thenReturn("아름다운 해변가 숙소입니다.");
        lenient().when(lodge.getAddress()).thenReturn("123 Street, City");
        lenient().when(lodge.getTerm()).thenReturn(7);
        lenient().when(lodge.getBasicPrice()).thenReturn(100.0);
        LodgeImage mockImage = createMockLodgeImage();
        lenient().when(lodge.getImages()).thenReturn(List.of(mockImage));
        return lodge;
    }

    public static LodgeImage createMockLodgeImage() {
        LodgeImage lodgeImage = mock(LodgeImage.class);
        lenient().when(lodgeImage.getId()).thenReturn("ImageUUID");
        lenient().when(lodgeImage.getTitle()).thenReturn("숙소 사진");
        lenient().when(lodgeImage.getDescription()).thenReturn("밖에서 볼 수 있는 숙소 사진입니다.");
        lenient().when(lodgeImage.getUrl()).thenReturn("https://example.com/image.jpg");
        return lodgeImage;
    }

    public static LodgeRule createMockLodgeRule() {
        LodgeRule lodgeRule = mock(LodgeRule.class);
        lenient().when(lodgeRule.getId()).thenReturn("RuleUUID");
        lenient().when(lodgeRule.getMaxReservationDay()).thenReturn(10);
        lenient().when(lodgeRule.getMaxPersonnel()).thenReturn(5);
        lenient().when(lodgeRule.getCustomization()).thenReturn("애완견 출입 금지입니다.");
        return lodgeRule;
    }

}
