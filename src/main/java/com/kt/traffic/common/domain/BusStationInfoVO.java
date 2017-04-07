package com.kt.traffic.common.domain;

import lombok.Data;

/**
 * Created by BigFence on 2017-04-07.
 */
@Data
public class BusStationInfoVO {
    private String regionCd;
    private String stationId;
    private String stationNm;
    private double gpsX;
    private double gpsY;
    private String stationOwnId;

    private String regionNm;
    private String centerYn;

    private double posX;
    private double posY;
}
