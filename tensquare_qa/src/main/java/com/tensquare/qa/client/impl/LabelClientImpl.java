package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.ILabelClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * LabelClient 的失败处理类。
 */
@Component
public class LabelClientImpl implements ILabelClient {

    @Override
    public Result findById(String id) {
        return new Result(false, StatusCode.REMOTE_ERROR, "熔断器开启了...");
    }

}
