package com.codestates.seb006main.config.batch;

import com.codestates.seb006main.Image.entity.Image;
import com.codestates.seb006main.Image.repository.ImageRepository;
import com.codestates.seb006main.Image.service.ImageService;
import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.matching.repository.MatchingRepository;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.repository.MemberRepository;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.posts.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@EnableBatchProcessing
@Configuration
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final PostsRepository postsRepository;
    private final MatchingRepository matchingRepository;
    private final ImageService imageService;

    // TODO: 대용량 데이터 처리, chunkSize 조정이 필요하다. 그건 어떻게 할까? 너는 알고 있다. 구현하지 않았을 뿐.

    @Bean
    public Job deleteUnusedObject() {
        return jobBuilderFactory.get("deleteUnusedObject")
                .start(deleteUnusedImage())
                .next(deleteInactivePosts())
                .next(deleteWithdrawalMember())
                .next(deleteClosedMatching())
                .build();
    }

    @Bean
    public Step deleteUnusedImage() {
        return stepBuilderFactory.get("deleteUnusedImage")
                .tasklet((contribution, chunkContext) -> {
                    LocalDateTime aHourAgo = LocalDateTime.now().minusHours(1);
                    List<Image> unusedImages = imageRepository.findUnusedImages(aHourAgo);
                    if (unusedImages.size() > 0) {
                        for (Image image : unusedImages) {
                                imageService.deleteImage(image.getImageId());
                        }
                    }
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step deleteWithdrawalMember() {
        return stepBuilderFactory.get("deleteWithdrawalMember")
                .tasklet((contribution, chunkContext) -> {
                    List<Member> withdrawalMembers = memberRepository.findByMemberStatus(Member.MemberStatus.WITHDRAWAL);
                    if (withdrawalMembers.size() > 0) {
                        for (Member member : withdrawalMembers) {
                            memberRepository.deleteById(member.getMemberId());
                        }
                    }
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step deleteInactivePosts() {
        return stepBuilderFactory.get("deleteInactivePosts")
                .tasklet((contribution, chunkContext) -> {
                    List<Posts> inactivePosts = postsRepository.findByPostsStatus(Posts.PostsStatus.INACTIVE);
                    if (inactivePosts.size() > 0) {
                        for (Posts posts : inactivePosts) {
                            postsRepository.deleteById(posts.getPostId());
                        }
                    }
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step deleteClosedMatching() {
        return stepBuilderFactory.get("deleteClosedMatching")
                .tasklet((contribution, chunkContext) -> {
                    List<Matching> closedMatching = matchingRepository.findClosedMatching();
                    if (closedMatching.size() > 0) {
                        for (Matching matching : closedMatching) {
                            matchingRepository.deleteById(matching.getMatchingId());
                        }
                    }
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}
