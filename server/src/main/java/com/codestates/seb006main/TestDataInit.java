package com.codestates.seb006main;

import com.codestates.seb006main.group.entity.Group;
import com.codestates.seb006main.group.repository.GroupRepository;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.repository.MemberRepository;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.posts.repository.PostsRepository;
import com.codestates.seb006main.util.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final PostsRepository postsRepository;
    private final GroupRepository groupRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        String password = "123456";
        String encPassword = bCryptPasswordEncoder.encode(password);

        Member 김기홍 = memberRepository.save(Member.builder()
                .email("rlghd153@gmail.com")
                .password(encPassword)
                .displayName("김기홍")
                .phone("010-1111-1111")
                .content("여행을 좋아하는 김기홍입니다.")
                .profileImage("")
                .build());
        Member 정윤조 = memberRepository.save(Member.builder()
                .email("jyjgom@gmail.com")
                .password(encPassword)
                .displayName("정윤조")
                .phone("010-2222-2222")
                .content("여행을 좋아하는 정윤조입니다.")
                .profileImage("")
                .build());
        Member 배자현 = memberRepository.save(Member.builder()
                .email("bizbaeja@gmail.com")
                .password(encPassword)
                .displayName("배자현")
                .phone("010-3333-3333")
                .content("여행을 좋아하는 배자현입니다.")
                .profileImage("")
                .build());
        Member 이동기 = memberRepository.save(Member.builder()
                .email("Lmoti@gmail.com")
                .password(encPassword)
                .displayName("이동기")
                .phone("010-5555-5555")
                .content("여행을 좋아하는 이동기입니다.")
                .profileImage("")
                .build());

        Group 사이판 = Group.builder()
                .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
                        LocalDate.of(2022, 11, 14)))
                .location("사이판")
                .headcount(2)
                .groupStatus(Group.GroupStatus.RECRUITING)
                .closeDate(LocalDate.of(2022, 10, 12))
                .build();

        Group 보라카이 = Group.builder()
                .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
                        LocalDate.of(2022, 11, 14)))
                .location("보라카이")
                .headcount(2)
                .groupStatus(Group.GroupStatus.RECRUITING)
                .closeDate(LocalDate.of(2022, 10, 12))
                .build();

        Posts 첫번째 = postsRepository.save(Posts.builder()
                .title("(급합니다) 사이판 여행 모집 구해봐요")
                .body("일정이 촉박해 빠르게 구해봅니다. 사이판으로 여행가실 여자분 2명 구합니다. 여자 혼자 가기 너무 심심하네요. 흑흑흑.")
                .postsStatus(Posts.PostsStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2022, 9, 17, 12, 0, 0))
                .modifiedAt(LocalDateTime.now())
                .group(사이판)
                .build());

        Posts 두번째 = postsRepository.save(Posts.builder()
                .title("보라카이 혼자 가시는 분 같이 가요")
                .body("보라카이로 여행가시는 남자분 1명 구합니다. 남자 혼자 가기 외롭네요. 허허허.")
                .postsStatus(Posts.PostsStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2022, 9, 17, 12, 0, 0))
                .modifiedAt(LocalDateTime.now())
                .group(보라카이)
                .build());
    }
}
