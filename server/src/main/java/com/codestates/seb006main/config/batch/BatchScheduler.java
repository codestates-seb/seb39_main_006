package com.codestates.seb006main.config.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final BatchConfig batchConfig;

    // TODO: 원래 계획은 매주 특정 요일 아침 9시. 지금은 테스트용으로 1시간마다.
//    @Scheduled(cron = "0 0 9 * * WED")
    @Scheduled(cron = "0 0/10 * * * *")
    public void runJob() {
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(batchConfig.deleteUnusedObject(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobParametersInvalidException | JobRestartException e) {
            System.out.println(e.getMessage());
        }
    }
}
