package myProject1.gift.service;

import myProject1.gift.dto.GiftItemDto;
import myProject1.gift.domain.*;
import myProject1.gift.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class GiftServiceTest {
    @Autowired
    GiftService giftService;
    @Autowired
    GiftRepository giftRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    void 선물생성(){
        //given
        String message = "제 선물을 받아주세요!!";

        Long resultGiveMemberId = createMember("염재선", "^^");
        Long resultReceiveMemberId = createMember("김태희", "~~~~");

        Category category1 = createCategory("음식");
        Category category2 = createCategory("가전제품");
        Category category3 = createCategory("헬스용품");

        Long itemId1 = createItem(category2, "맥북", 20000, 100);
        Long itemId2 = createItem(category1, "치킨", 10000, 200);
        Long itemId3 = createItem(category1, "아메리카노", 5000, 300);
        Long itemId4 = createItem(category3, "덤벨", 50000, 300);


        GiftItemDto giftItemDto1 = new GiftItemDto();
        giftItemDto1.setItemId(itemId1);
        int giftCount1 = 10;
        giftItemDto1.setCount(giftCount1);

        GiftItemDto giftItemDto2 = new GiftItemDto();
        giftItemDto2.setItemId(itemId3);
        int giftCount2 = 30;
        giftItemDto2.setCount(giftCount2);

        GiftItemDto giftItemDto3 = new GiftItemDto();
        giftItemDto3.setItemId(itemId4);
        int giftCount3 = 100;
        giftItemDto3.setCount(giftCount3);

        //when
        Long giftId = giftService.createGift(resultGiveMemberId, resultReceiveMemberId, message, giftItemDto1, giftItemDto2, giftItemDto3);
        Gift resultGift = giftRepository.findOne(giftId);

        //then
        assertThat(resultGift.getGiftItems().size()).isEqualTo(3);
        assertThat(resultGift.getMember()).isEqualTo(memberRepository.findOne(resultGiveMemberId));
        assertThat(resultGift.getReceiveMember()).isEqualTo(memberRepository.findOne(resultReceiveMemberId));
        assertThat(resultGift.getTotalPrice()).isEqualTo(20000 * 10 + 5000 * 30 + 50000 * 100);
    }

    @Test
    void 생성한_선물_수락(){
        //given
        String message = "제 선물을 받아주세됴!";

        Long resultGiveMemberId = createMember("염재선", "^^");
        Long resultReceiveMemberId = createMember("김태희", "~~~~");

        Category category1 = createCategory("음식");
        Category category2 = createCategory("가전제품");
        Category category3 = createCategory("헬스용품");

        Long itemId1 = createItem(category2, "맥북", 20000, 100);
        Long itemId2 = createItem(category1, "치킨", 10000, 200);
        Long itemId3 = createItem(category1, "아메리카노", 5000, 300);
        Long itemId4 = createItem(category3, "덤벨", 50000, 300);


        GiftItemDto giftItemDto1 = new GiftItemDto();
        giftItemDto1.setItemId(itemId1);
        int giftCount1 = 10;
        giftItemDto1.setCount(giftCount1);

        GiftItemDto giftItemDto2 = new GiftItemDto();
        giftItemDto2.setItemId(itemId3);
        int giftCount2 = 30;
        giftItemDto2.setCount(giftCount2);

        GiftItemDto giftItemDto3 = new GiftItemDto();
        giftItemDto3.setItemId(itemId4);
        int giftCount3 = 100;
        giftItemDto3.setCount(giftCount3);

        Long giftId = giftService.createGift(resultGiveMemberId, resultReceiveMemberId, message, giftItemDto1, giftItemDto2, giftItemDto3);

        //when
        giftService.acceptGift(giftId);
        Gift gift = giftRepository.findOne(giftId);
        Item item1 = itemRepository.findOne(itemId1);
        Item item2 = itemRepository.findOne(itemId2);
        Item item3 = itemRepository.findOne(itemId3);
        Item item4 = itemRepository.findOne(itemId4);

        //then
        assertThat(gift.getStatus()).isEqualTo(GiftStatus.ACCEPTED);
        assertThat(gift.getReceiveMember().getStatus()).isEqualTo(GiftReceiveStatus.RECEIVED);
        assertThat(item1.getStockQuantity()).isEqualTo(90);
        assertThat(item2.getStockQuantity()).isEqualTo(200);
        assertThat(item3.getStockQuantity()).isEqualTo(270);
        assertThat(item4.getStockQuantity()).isEqualTo(200);
    }

    @Test
    void 생성한_선물_거절(){
        //given
        String message = "제 선물을 받아주세됴!";

        Long resultGiveMemberId = createMember("염재선", "^^");
        Long resultReceiveMemberId = createMember("김태희", "~~~~");

        Category category1 = createCategory("음식");
        Category category2 = createCategory("가전제품");
        Category category3 = createCategory("헬스용품");

        Long itemId1 = createItem(category2, "맥북", 20000, 100);
        Long itemId2 = createItem(category1, "치킨", 10000, 200);
        Long itemId3 = createItem(category1, "아메리카노", 5000, 300);
        Long itemId4 = createItem(category3, "덤벨", 50000, 300);


        GiftItemDto giftItemDto1 = new GiftItemDto();
        giftItemDto1.setItemId(itemId1);
        int giftCount1 = 10;
        giftItemDto1.setCount(giftCount1);

        GiftItemDto giftItemDto2 = new GiftItemDto();
        giftItemDto2.setItemId(itemId3);
        int giftCount2 = 30;
        giftItemDto2.setCount(giftCount2);

        GiftItemDto giftItemDto3 = new GiftItemDto();
        giftItemDto3.setItemId(itemId4);
        int giftCount3 = 100;
        giftItemDto3.setCount(giftCount3);

        Long giftId = giftService.createGift(resultGiveMemberId, resultReceiveMemberId, message, giftItemDto1, giftItemDto2, giftItemDto3);

        //when
        giftService.refuseGift(giftId);
        Gift gift = giftRepository.findOne(giftId);

        //then - 상품 거절은 item의 재고가 변화가없음.
        assertThat(gift.getStatus()).isEqualTo(GiftStatus.NOT_ACCEPTED);
        assertThat(gift.getReceiveMember().getStatus()).isEqualTo(GiftReceiveStatus.NOT_RECEIVED);
    }

    @Test
    void 선물_생성_시_재고부족(){
        //given
        String message=  "제 선물을 받아주세요!!";

        Long resultGiveMemberId = createMember("염재선", "^^");
        Long resultReceiveMemberId = createMember("김태희", "~~~~");

        Category category2 = createCategory("가전제품");

        Long itemId1 = createItem(category2, "맥북", 20000, 100);

        GiftItemDto giftItemDto1 = new GiftItemDto();
        giftItemDto1.setItemId(itemId1);

        //when
        int giftCount1 = 150;
        giftItemDto1.setCount(giftCount1); //생성

        Assertions.assertThrows(IllegalArgumentException.class, () -> giftService.createGift(resultGiveMemberId, resultReceiveMemberId, message, giftItemDto1));
    }

    private Long createItem(Category category2, String name, int price, int stockQuantity) {
        Item item1 = Item.createItem(name, price, stockQuantity, category2);
        Long itemId = itemRepository.save(item1);
        return itemId;
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return category;
    }

    private Long createMember(String name, String message) {
        Member giveMember = new Member();
        giveMember.setName(name);
        giveMember.setMessage(message);
        Long memberId = memberRepository.save(giveMember);
        return memberId;
    }

}