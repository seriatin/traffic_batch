package com.kt.traffic.bus;

import com.kt.traffic.bus.domain.GyeonggiBaseInfoServiceVO;
import com.kt.traffic.bus.domain.PusanBusStationInfoVO;
import com.kt.traffic.bus.domain.SeoulBusStationInfoVO;
import com.kt.traffic.bus.processor.SeoulBusStationProcessor;
import com.kt.traffic.common.domain.BusStationInfoVO;
import com.kt.traffic.common.reader.GyeonggiRestDomainReader;
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
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
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
        URI uri = new URI("http://ws.bus.go.kr/api/rest/stationinfo/getStationByName?serviceKey=lz3xwnQikI6Q667LJSVUDLny0sAiaKvWk1feXbrJ6Fc0paC7EE2rzEowBQmIkmDt%2B5oiN91ahjzT0Ne5NeK81Q%3D%3D&stSrch=%25");
        return new RestDomainReader<SeoulBusStationInfoVO.ItemList, SeoulBusStationInfoVO>(
                "", "", uri, restTemplate, SeoulBusStationInfoVO.class
        );
    }

    @Bean
    ItemReader<BusStationInfoVO> gyeonggiBusStationReader(Environment environment, RestTemplate restTemplate) throws URISyntaxException {
        URI uri = new URI("http://openapi.gbis.go.kr/ws/rest/baseinfoservice?serviceKey=lz3xwnQikI6Q667LJSVUDLny0sAiaKvWk1feXbrJ6Fc0paC7EE2rzEowBQmIkmDt%2B5oiN91ahjzT0Ne5NeK81Q%3D%3D");
        return new GyeonggiRestDomainReader<BusStationInfoVO, GyeonggiBaseInfoServiceVO>(
                "", "", uri, restTemplate, GyeonggiBaseInfoServiceVO.class
        );
    }

    @Bean
    ItemReader<PusanBusStationInfoVO.Body.Item> pusanBusStationReader(Environment environment, RestTemplate restTemplate) throws URISyntaxException {
        URI uri = new URI("http://61.43.246.153/openapi-data/service/busanBIMS/busStop?ServiceKey=lz3xwnQikI6Q667LJSVUDLny0sAiaKvWk1feXbrJ6Fc0paC7EE2rzEowBQmIkmDt%2B5oiN91ahjzT0Ne5NeK81Q%3D%3D");
        return new RestDomainReader<PusanBusStationInfoVO.Body.Item, PusanBusStationInfoVO>(
                "", "", uri, restTemplate, PusanBusStationInfoVO.class
        );
    }

    @Bean
    ItemProcessor<SeoulBusStationInfoVO.ItemList, BusStationInfoVO> seoulBusStationProcessor() {
        return new SeoulBusStationProcessor();
    }

    @Bean
    ItemWriter<BusStationInfoVO> seoulBusStationDBWriter(Environment environment, DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<BusStationInfoVO> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setJdbcTemplate(jdbcTemplate);
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.setSql(environment.getProperty("bus.api.station.seoul.query"));
        return itemWriter;
    }

    @Bean
    ItemWriter<BusStationInfoVO> gyeonggiBusStationDBWriter(Environment environment, DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<BusStationInfoVO> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setJdbcTemplate(jdbcTemplate);
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.setSql(environment.getProperty("bus.api.station.gyeonggi.query"));
        return itemWriter;
    }

    @Bean
    ItemWriter<BusStationInfoVO> pusanBusStationDBWriter(Environment environment, DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<BusStationInfoVO> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setJdbcTemplate(jdbcTemplate);
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.setSql(environment.getProperty("bus.api.station.pusan.query"));
        return itemWriter;
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
    public Step busStationListForGyeongggiStep(ItemReader<BusStationInfoVO> gyeonggiBusStationReader,
                                           ItemWriter<BusStationInfoVO> gyeonggiBusStationDBWriter,
                                           StepBuilderFactory stepBuilderFactory
    ) {
        return stepBuilderFactory.get("busStationListForGyeongggiStep")
                .<BusStationInfoVO, BusStationInfoVO>chunk(10)
                .reader(gyeonggiBusStationReader)
                .processor(new ItemProcessor<BusStationInfoVO, BusStationInfoVO>() {
                    @Override
                    public BusStationInfoVO process(BusStationInfoVO busStationInfoVO) throws Exception {
                        return busStationInfoVO;
                    }
                })
                .writer(gyeonggiBusStationDBWriter)
                .build();
    }

    @Bean
    public Step busStationListForPusanStep(ItemReader<PusanBusStationInfoVO.Body.Item> pusanBusStationReader,
                                               ItemWriter<BusStationInfoVO> pusanBusStationDBWriter,
                                               StepBuilderFactory stepBuilderFactory
    ) {
        return stepBuilderFactory.get("busStationListForPusanStep")
                .<PusanBusStationInfoVO.Body.Item, BusStationInfoVO>chunk(10)
                .reader(pusanBusStationReader)
                .processor(new ItemProcessor<PusanBusStationInfoVO.Body.Item, BusStationInfoVO>() {
                    @Override
                    public BusStationInfoVO process(PusanBusStationInfoVO.Body.Item item) throws Exception {
                        BusStationInfoVO stationInfoVO = new BusStationInfoVO();
                        stationInfoVO.setGpsX(item.getGpsX());
                        stationInfoVO.setGpsY(item.getGpsY());
                        stationInfoVO.setStationId(item.getBstopId());
                        stationInfoVO.setStationNm(item.getBstopNm());
                        stationInfoVO.setStationOwnId(item.getBstopArsno());
                        return stationInfoVO;
                    }
                })
                .writer(pusanBusStationDBWriter)
                .build();
    }

    @Bean
    public Job busStationListForSeoulJob(JobBuilderFactory jobBuilderFactory,
                                         @Qualifier("busStationListForSeoulStep") Step busStationListForSeoulStep,
                                         @Qualifier("busStationListForGyeongggiStep") Step busStationListForGyeongggiStep,
                                         @Qualifier("busStationListForPusanStep") Step busStationListForPusanStep) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                //.flow(busStationListForSeoulStep)
                //.next(busStationListForGyeongggiStep)
                //.next(busStationListForPusanStep)
                .flow(busStationListForPusanStep)
                .end()
                .build();
    }

}
