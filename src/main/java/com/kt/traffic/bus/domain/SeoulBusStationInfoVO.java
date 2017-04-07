package com.kt.traffic.bus.domain;

import com.kt.traffic.common.domain.ComMsgHeaderVO;
import com.kt.traffic.common.domain.MsgHeaderVO;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by BigFence on 2017-04-07.
 */
@Data
@XmlRootElement(name = "ServiceResult")
public class SeoulBusStationInfoVO {

    private ComMsgHeaderVO comMsgHeaderVO;
    private MsgHeaderVO msgHeaderVO;

    //private MsgBody msgBody;

    //@Override
    public List<ItemList> items() {
        return msgBody;
    }

    private List<ItemList> msgBody;

    @XmlElementWrapper(name = "msgBody")
    @XmlElement(name = "itemList")
    public List<ItemList> getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(List<ItemList> msgBody) {
        this.msgBody = msgBody;
    }

    @Data
    @XmlRootElement(name = "itemList")
    public static class ItemList {
        private String arsId;
        private double posX;
        private double posY;
        private String stId;
        private String stNm;
        private double tmX;
        private double tmY;
    }
}
