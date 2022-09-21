package com.codestates.seb006main;

import com.codestates.seb006main.group.repository.GroupRepository;
import com.codestates.seb006main.members.repository.MemberRepository;
import com.codestates.seb006main.posts.repository.PostsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    public TestDataInit dataInit(MemberRepository memberRepository,
                                 PostsRepository postsRepository,
                                 GroupRepository groupRepository,
                                 BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new TestDataInit(memberRepository, postsRepository, groupRepository, bCryptPasswordEncoder);
    }

}
