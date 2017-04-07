package com.kt.traffic.common.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sino on 2017-04-04.
 */
@Data
@XmlRootElement(name = "comMsgHeader")
public class ComMsgHeaderVO {
    private String errMsg;
    private String returnCode;
}
