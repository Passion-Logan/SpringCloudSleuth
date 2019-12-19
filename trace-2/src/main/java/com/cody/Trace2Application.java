package com.cody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *由于trace-1对trace-2发起的请求是通过RestTemplate实现的，所以spring-cloud-starter-sleuth组件会对
 * 该请求进行处理，在发送到trace-2之前sleuth会为在该请求的Header中增加实现跟踪需要的重要信息，主要有下面
 * 这几个（更多关于头信息的定义我们可以通过查看org.springframework.cloud.sleuth.Span的源码获取）：
 *
 * X-B3-TraceId：一条请求链路（Trace）的唯一标识，必须值
 * X-B3-SpanId：一个工作单元（Span）的唯一标识，必须值
 * X-B3-ParentSpanId:：标识当前工作单元所属的上一个工作单元，Root Span（请求链路的第一个工作单元）的该值为空
 * X-B3-Sampled：是否被抽样输出的标志，1表示需要被输出，0表示不需要被输出
 * X-Span-Name：工作单元的名称
 *
 *
 * 为了更直观的观察跟踪信息，我们还可以在application.properties中增加下面的配置：
 * logging.level.org.springframework.web.servlet.DispatcherServlet=DEBUG
 */
@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class Trace2Application {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/trace-2", method = RequestMethod.GET)
    public String trace(HttpServletRequest request) {
        logger.info("===<call trace-2, TraceId={}, SpanId={}>===",
                request.getHeader("X-B3-TraceId"), request.getHeader("X-B3-SpanId"));
        return "Trace";
    }

    public static void main(String[] args) {
        SpringApplication.run(Trace2Application.class, args);
    }

}
