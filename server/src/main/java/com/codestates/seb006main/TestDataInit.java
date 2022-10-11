package com.codestates.seb006main;

import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.repository.MemberRepository;
import com.codestates.seb006main.posts.entity.MemberPosts;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.posts.repository.MemberPostsRepository;
import com.codestates.seb006main.posts.repository.PostsRepository;
import com.codestates.seb006main.util.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final PostsRepository postsRepository;
    private final MemberPostsRepository memberPostsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Value("${cloud.aws.s3.default-image}")
    String defaultImage;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        String password = "123456";
        String encPassword = bCryptPasswordEncoder.encode(password);

        Member 개발천재김기홍 = memberRepository.save(Member.builder()
                .email("rlghd153@gmail.com")
                .password(encPassword)
                .displayName("개발천재김기홍")
                .phone("010-1111-1111")
                .content("여행을 좋아하는 개발천재김기홍입니다.")
                .profileImage(defaultImage)
                .build());
        Member 수상한의도정윤조 = memberRepository.save(Member.builder()
                .email("jyjgom@gmail.com")
                .password(encPassword)
                .displayName("수상한의도정윤조")
                .phone("010-2222-2222")
                .content("여행을 좋아하는 수상한의도정윤조입니다.")
                .profileImage(defaultImage)
                .build());
        Member 여행을좋아하는배자현 = memberRepository.save(Member.builder()
                .email("bizbaeja@gmail.com")
                .password(encPassword)
                .displayName("여행을좋아하는배자현")
                .phone("010-3333-3333")
                .content("여행을 좋아하는 배자현입니다.")
                .profileImage(defaultImage)
                .build());
        Member 즐거운시간이동기 = memberRepository.save(Member.builder()
                .email("Lmoti@gmail.com")
                .password(encPassword)
                .displayName("즐거운시간이동기")
                .phone("010-5555-5555")
                .content("여행을 좋아하는 즐거운시간이동기입니다.")
                .profileImage(defaultImage)
                .build());

        Posts 첫번째 = postsRepository.save(Posts.builder()
                .title("(급합니다) 사이판 여행 모집 구해봐요")
                .body("일정이 촉박해 빠르게 구해봅니다. 사이판으로 여행가실 여자분 최대 2명 구합니다. 여자 혼자 가기 너무 심심하네요. 흑흑흑.")
                .postsStatus(Posts.PostsStatus.READY)
                .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
                        LocalDate.of(2022, 11, 14)))
                .location("사이판")
                .totalCount(3)
                .closeDate(LocalDate.of(2022, 10, 12))
                .member(여행을좋아하는배자현)
                .build());

        memberPostsRepository.save(MemberPosts.builder().member(여행을좋아하는배자현).posts(첫번째).build());

        Posts 두번째 = postsRepository.save(Posts.builder()
                .title("보라카이 혼자 가시는 분 같이 가요")
                .body("보라카이로 여행가시는 남자분 1명 구합니다. 남자 혼자 가기 외롭네요. 허허허.")
                .postsStatus(Posts.PostsStatus.READY)
                .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
                        LocalDate.of(2022, 11, 14)))
                .location("보라카이")
                .totalCount(2)
                .closeDate(LocalDate.of(2022, 10, 12))
                .member(수상한의도정윤조)
                .build());

        memberPostsRepository.save(MemberPosts.builder().member(수상한의도정윤조).posts(두번째).build());

        Posts 세번째 = postsRepository.save(Posts.builder()
                .title("제주도에서 즐거운 시간을 보낼 사람 있을까요")
                .body("이번 가을 제주도 함께 하실 분 구해봅니다. 혼자라서 여행 경비 부담이 되시는 분 적극 연락해주세요! 3명까지지만 다 차지 않아도 괜찮습니다.")
                .postsStatus(Posts.PostsStatus.READY)
                .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
                        LocalDate.of(2022, 11, 14)))
                .location("제주도")
                .totalCount(4)
                .closeDate(LocalDate.of(2022, 10, 12))
                .member(즐거운시간이동기)
                .build());

        memberPostsRepository.save(MemberPosts.builder().member(즐거운시간이동기).posts(세번째).build());

        Posts 네번째 = postsRepository.save(Posts.builder()
                .title("미국 샌프란시스코 여행 경비가 부담되어서 함께 다닐 사람 구합니다.")
                .body("인원은 2명 정도 생각하고 있습니다. 혼자서는 여행 경비가 부담스러워서 함께 다니면서 좋은 인연을 만들 사람이 있으면 좋겠습니다. 연락주세요!")
                .postsStatus(Posts.PostsStatus.READY)
                .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
                        LocalDate.of(2022, 11, 18)))
                .location("샌프란시스코")
                .totalCount(3)
                .closeDate(LocalDate.of(2022, 10, 12))
                .member(개발천재김기홍)
                .build());

        memberPostsRepository.save(MemberPosts.builder().member(개발천재김기홍).posts(네번째).build());

    }
}
