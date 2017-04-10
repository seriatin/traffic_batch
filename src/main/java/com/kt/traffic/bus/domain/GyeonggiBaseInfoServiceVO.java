package com.kt.traffic.bus.domain;

import com.kt.traffic.common.domain.AbstractMsgBodyVO;
import com.kt.traffic.common.domain.ComMsgHeaderVO;
import com.kt.traffic.common.domain.MsgHeaderVO;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sino on 2017-04-04.
 */
@Data
@XmlRootElement(name = "response")
public class GyeonggiBaseInfoServiceVO {

    private ComMsgHeaderVO comMsgHeader;
    private MsgHeaderVO msgHeader;

    private MsgBody msgBody;

    @Data
    public static class MsgBody extends AbstractMsgBodyVO {

        private BaseInfoItem baseInfoItem;

        @XmlRootElement(name = "baseInfoItem")
        @Data
        public static class BaseInfoItem {
            private String areaDownloadUrl;
            private String areaVersion;
            private String routeDownloadUrl;
            private String routeLineDownloadUrl;
            private String routeLineVersion;
            private String routeStationDownloadUrl;
            private String routeStationVersion;
            private String stationDownloadUrl;
            private String stationVersion;
        }

    }
}
