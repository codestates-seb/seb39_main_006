package com.codestates.seb006main.matching.service;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.config.DomainEvent;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.matching.dto.MatchingDto;
import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.matching.mapper.MatchingMapper;
import com.codestates.seb006main.matching.repository.MatchingRepository;
import com.codestates.seb006main.message.entity.Message;
import com.codestates.seb006main.message.repository.MessageRepository;
import com.codestates.seb006main.posts.entity.MemberPosts;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.posts.repository.MemberPostsRepository;
import com.codestates.seb006main.posts.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class MatchingService {
    private final MatchingRepository matchingRepository;
    private final PostsRepository postsRepository;
    private final MemberPostsRepository memberPostsRepository;
    private final MatchingMapper matchingMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public MatchingDto.Response createMatching(Long postId, MatchingDto.Post postDto, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Posts posts = postsRepository.findActiveById(postId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
        if (posts.isFull()) {
            throw new BusinessLogicException(ExceptionCode.GROUP_IS_FULL);
        }
        if (posts.getParticipants().stream().map(memberPosts -> memberPosts.getMember().getMemberId()).collect(Collectors.toList()).contains(principalDetails.getMember().getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_PARTICIPATED);
        }
        if (posts.getMatching().stream().map(matching -> matching.getMember().getMemberId()).collect(Collectors.toList()).contains(principalDetails.getMember().getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_REQUESTED);
        }
        Matching matching = Matching.builder()
                .body(postDto.getBody())
                .member(principalDetails.getMember()).build();
        matching.setPosts(posts);

        applicationEventPublisher.publishEvent(new DomainEvent(this, matching, DomainEvent.EventType.CREATE_MATCHING));

        matching.checkPostsStatus();
        matchingRepository.save(matching);
        return matchingMapper.matchingToResponseDto(matching);
    }

    public MatchingDto.Response readMatching(Long matchingId, Authentication authentication) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MATCHING_NOT_FOUND));
        if (!checkLeader(matching, authentication) && !checkPermission(matching, authentication)) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        } else if (checkLeader(matching, authentication)) {
            matching.read();
        }
        matchingRepository.save(matching);
        return matchingMapper.matchingToResponseDto(matching);
    }

    public MatchingDto.Response acceptMatching(Long matchingId, Authentication authentication) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MATCHING_NOT_FOUND));
        if(!checkLeader(matching, authentication)) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }
        matching.accept();
        matchingRepository.save(matching);

        MemberPosts memberPosts = MemberPosts.builder().member(matching.getMember()).build();
        memberPosts.setPosts(matching.getPosts());
        memberPosts.checkPostsStatus();

        applicationEventPublisher.publishEvent(new DomainEvent(this, memberPosts, DomainEvent.EventType.APPLY_MATCHING));

        memberPostsRepository.save(memberPosts);
        return matchingMapper.matchingToResponseDto(matching);
    }

    public MatchingDto.Response refuseMatching(Long matchingId, Authentication authentication) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MATCHING_NOT_FOUND));
        if (!checkLeader(matching, authentication)){
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }
        matching.refuse();
        matchingRepository.save(matching);
        return matchingMapper.matchingToResponseDto(matching);
    }

    public void cancelMatching(Long matchingId, Authentication authentication) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MATCHING_NOT_FOUND));
        if (!checkPermission(matching, authentication)) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }

        DomainEvent.Domain domain = new DomainEvent.Domain(matching.getPosts().getPostId(), matching.getPosts().getTitle(), matching.getMember(), matching.getPosts().getMember());
        applicationEventPublisher.publishEvent(new DomainEvent(this, domain, DomainEvent.EventType.CANCEL_MATCHING));

        matchingRepository.deleteById(matchingId);
    }

    public boolean checkLeader(Matching matching, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return matching.getPosts().getMember().getMemberId() == principalDetails.getMember().getMemberId();
    }

    public boolean checkPermission(Matching matching, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return matching.getMember().getMemberId() == principalDetails.getMember().getMemberId();
    }
}
