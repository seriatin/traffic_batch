package com.kt.traffic.bus.processor;

import com.kt.traffic.bus.domain.SeoulBusStationInfoVO;
import com.kt.traffic.common.domain.BusStationInfoVO;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by BigFence on 2017-04-07.
 */
public class SeoulBusStationProcessor implements ItemProcessor<SeoulBusStationInfoVO.ItemList, BusStationInfoVO> {
    @Override
    public BusStationInfoVO process(SeoulBusStationInfoVO.ItemList itemList) throws Exception {
        BusStationInfoVO stationInfoVO = new BusStationInfoVO();
        stationInfoVO.setGpsX(itemList.getTmX());
        stationInfoVO.setGpsY(itemList.getTmY());
        stationInfoVO.setStationId(itemList.getStId());
        stationInfoVO.setStationNm(itemList.getStNm());
        stationInfoVO.setStationOwnId(itemList.getArsId());
        stationInfoVO.setPosX(itemList.getPosX());
        stationInfoVO.setPosY(itemList.getPosY());
        return stationInfoVO;
    }
}
