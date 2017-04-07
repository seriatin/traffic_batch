package com.kt.traffic.bus;

import com.kt.traffic.bus.domain.SeoulBusStationInfoVO;
import com.kt.traffic.bus.processor.SeoulBusStationProcessor;
import com.kt.traffic.common.domain.BusStationInfoVO;
import com.kt.traffic.common.reader.RestDomainReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by BigFence on 2017-04-07.
 */
@Configuration
public class JobConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JobConfiguration.class);
    private static final String JOB_NAME = "busStationListForSeoul";
    private static final String STEP_NAME = "busStationListForSeoulStep";

    @Bean
    ItemReader<SeoulBusStationInfoVO.ItemList> seoulBusStationReader(Environment environment, RestTemplate restTemplate) throws URISyntaxException {
        URI uri = new URI("http://ws.bus.go.kr/api/rest/stationinfo/getStationByName?serviceKey=&stSrch=");
        return new RestDomainReader<SeoulBusStationInfoVO.ItemList, SeoulBusStationInfoVO>(
                "", "", uri, restTemplate, SeoulBusStationInfoVO.class
        );
    }

    @Bean
    ItemProcessor<SeoulBusStationInfoVO.ItemList, BusStationInfoVO> seoulBusStationProcessor() {
        return new SeoulBusStationProcessor();
    }

    @Bean
    ItemWriter<BusStationInfoVO> seoulBusStationDBWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<BusStationInfoVO>
    }

    @Bean
    public Step busStationListForSeoulStep(ItemReader<SeoulBusStationInfoVO.ItemList> seoulBusStationReader,
                                           ItemProcessor<SeoulBusStationInfoVO.ItemList, BusStationInfoVO> seoulBusStationProcessor,
                                           ItemWriter<BusStationInfoVO> seoulBusStationDBWriter,
                                           StepBuilderFactory stepBuilderFactory
                     ) {
        return stepBuilderFactory.get(STEP_NAME)
                .<SeoulBusStationInfoVO.ItemList, BusStationInfoVO>chunk(10)
                .reader(seoulBusStationReader)
                .processor(seoulBusStationProcessor)
                .writer(seoulBusStationDBWriter)
                .build();
    }

    @Bean
    public Job busStationListForSeoulJob(JobBuilderFactory jobBuilderFactory,
                                         @Qualifier("busStationListForSeoulStep") Step busStationListForSeoulStep) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(busStationListForSeoulStep)
                .end()
                .build();
    }

}
