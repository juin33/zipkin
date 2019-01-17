package com.juin.client;

import brave.CurrentSpanCustomizer;
import brave.SpanCustomizer;
import brave.Tracing;
import brave.context.slf4j.MDCScopeDecorator;
import brave.http.HttpTracing;
import brave.httpclient.TracingHttpClientBuilder;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.servlet.TracingFilter;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import brave.spring.webmvc.SpanCustomizingAsyncHandlerInterceptor;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

import javax.servlet.Filter;

/**
 * @author kejun
 * @date 2019/1/17 上午10:08
 */

@Configuration
@Import(SpanCustomizingAsyncHandlerInterceptor.class)
public class ZipkinClientConfiguration implements WebMvcConfigurer{

    @Bean
    Sender sender(){
        return OkHttpSender.create("http://127.0.0.1:9411/api/v2/spans");
    }

    /**
     * 配置如何把 span 缓冲到给 zipkin 的消息
     */
    @Bean
    AsyncReporter<Span> spanReporter() {
        return AsyncReporter.create(sender());
    }

    /**
     * 配置跟踪过程中的Trace信息
     */
    @Bean
    Tracing tracing(@Value("${spring.application.name}") String serviceName) {
        return Tracing.newBuilder()
                .localServiceName(serviceName)  // 设置节点名称
                .propagationFactory(ExtraFieldPropagation.newFactory(B3Propagation.FACTORY, "user-name"))
                .currentTraceContext(ThreadLocalCurrentTraceContext.newBuilder()
                        .addScopeDecorator(MDCScopeDecorator.create()) // puts trace IDs into logs
                        .build()
                )
                .spanReporter(spanReporter()).build();
    }

    /** 注入可定制的Span */
    @Bean
    SpanCustomizer spanCustomizer(Tracing tracing) {
        return CurrentSpanCustomizer.create(tracing);
    }

    /** 决定如何命名和标记span。 默认情况下，它们的名称与http方法相同 */
    @Bean
    HttpTracing httpTracing(Tracing tracing) {
        return HttpTracing.create(tracing);
    }

    /** 导入过滤器，该过滤器中会为http请求创建span */
    @Bean
    Filter tracingFilter(HttpTracing httpTracing) {
        return TracingFilter.create(httpTracing);
    }

    /**
     * 导入 zipkin 定制的 RestTemplateCustomizer
     */
    @Bean
    RestTemplateCustomizer useTracedHttpClient(HttpTracing httpTracing) {
        final CloseableHttpClient httpClient = TracingHttpClientBuilder.create(httpTracing).build();
        return new RestTemplateCustomizer() {
            @Override public void customize(RestTemplate restTemplate) {
                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
            }
        };
    }

    @Autowired
    SpanCustomizingAsyncHandlerInterceptor webMvcTracingCustomizer;

    /** 使用应用程序定义的Web标记装饰服务器span */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webMvcTracingCustomizer);
    }
}
