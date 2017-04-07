package com.kt.traffic;

import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class BusApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public BatchConfigurer batchConfigurer() {
		BatchConfigurer configurer = new BatchConfigurer() {
			private PlatformTransactionManager transactionManager;
			private JobRepository jobRepository;
			private JobLauncher jobLauncher;
			private JobExplorer jobExplorer;

			@Override
			public PlatformTransactionManager getTransactionManager() throws Exception {
				return transactionManager;
			}

			@Override
			public JobRepository getJobRepository() throws Exception {
				return jobRepository;
			}

			@Override
			public JobLauncher getJobLauncher() throws Exception {
				return jobLauncher;
			}

			@Override
			public JobExplorer getJobExplorer() throws Exception {
				return jobExplorer;
			}

			@PostConstruct
			public void initialize() {
				if (this.transactionManager == null) {
					this.transactionManager = new ResourcelessTransactionManager();
				}
				try {
					MapJobRepositoryFactoryBean jrf = new MapJobRepositoryFactoryBean(this.transactionManager);
					jrf.afterPropertiesSet();
					this.jobRepository = jrf.getObject();

					MapJobExplorerFactoryBean jef = new MapJobExplorerFactoryBean(jrf);
					jef.afterPropertiesSet();
					this.jobExplorer = jef.getObject();

					SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
					jobLauncher.setJobRepository(jobRepository);
					jobLauncher.afterPropertiesSet();
					this.jobLauncher = jobLauncher;
				} catch (Exception e) {
					throw new BatchConfigurationException(e);
				}
			}
		};
		return configurer;
	}
}
