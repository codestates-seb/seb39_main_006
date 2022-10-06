package com.codestates.seb006main;

import com.codestates.seb006main.members.repository.MemberRepository;
import com.codestates.seb006main.posts.repository.MemberPostsRepository;
import com.codestates.seb006main.posts.repository.PostsRepository;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableJpaAuditing
@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

//    @Bean
//    public TestDataInit dataInit(MemberRepository memberRepository,
//                                 PostsRepository postsRepository,
//                                 MemberPostsRepository memberPostsRepository,
//                                 BCryptPasswordEncoder bCryptPasswordEncoder) {
//        return new TestDataInit(memberRepository, postsRepository, memberPostsRepository, bCryptPasswordEncoder);
//    }
}
