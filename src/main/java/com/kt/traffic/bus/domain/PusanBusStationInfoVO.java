package com.kt.traffic.bus.domain;

import com.kt.traffic.common.domain.HeaderVO;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by BigFence on 2017-04-10.
 */
@Data
@XmlRootElement(name = "response")
public class PusanBusStationInfoVO {
    private HeaderVO headerVO;

    private Body body;

    public List<Body.Item> items() {
        return body.getItems();
    }

    @Data
    @XmlRootElement(name = "body")
    public static class Body {

        private List<Item> items;
        @XmlElementWrapper(name = "items")
        @XmlElement(name = "item")
        public List<Item> getItems() {
            return items;
        }
        public void setItems(List<Item> items) {
            this.items = items;
        }

        @Data
        @XmlRootElement(name = "item")
        public static class Item {
            private String bstopArsno;
            private String bstopId;
            private String bstopNm;
            private double gpsX;
            private double gpsY;
        }

    }
}
