package com.kt.traffic.common.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by BigFence on 2017-04-10.
 */
@Data
@XmlRootElement(name = "header")
public class HeaderVO {
    private String resultCode;
    private String resultMsg;
}
