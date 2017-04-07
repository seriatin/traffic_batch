package com.kt.traffic.common.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sino on 2017-04-04.
 */
@Data
@XmlRootElement(name = "msgHeader")
public class MsgHeaderVO {
    private String queryTime;
    private int resultCode;
    private String resultMessage;

    private int headerCd;
    private String headerMsg;
    private int itemCount;

}
