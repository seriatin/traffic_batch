package com.kt.traffic.common.domain;

import java.io.Serializable;

/**
 * Created by BigFence on 2017-04-07.
 */
public interface DataSelector<T> extends Serializable{
    T getData();
}
