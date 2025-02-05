package com.meiqiu.controller;

import com.meiqiu.base.BizResult;
import com.meiqiu.service.AsyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 线程池
 * @Author sgh
 * @Date 2025/1/21
 * @Time 14:26
 */
@RestController
@RequestMapping("/async")
@Api(tags = "线程")
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @ApiOperation(value = "异步生效测试")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public BizResult<Boolean> async() {
        asyncService.executeAsyncTask1();
        asyncService.executeAsyncTask2();
        return BizResult.success(true);
    }

    @ApiOperation(value = "高并发测试")
    @RequestMapping(value = "/hign-concurrency", method = RequestMethod.POST)
    public BizResult<Boolean> hignConcurrency() {
        //todo
        return BizResult.success(true);
    }

}
