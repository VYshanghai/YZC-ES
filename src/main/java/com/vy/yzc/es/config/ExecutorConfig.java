package com.vy.yzc.es.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author: vikko
 * @Date: 2021/3/1 11:29
 * @Description:
 */
@Configuration
public class ExecutorConfig {

	@Bean("userOffersViewRecordExecutor")
	public TaskExecutor learningJobExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 设置核心线程数
		executor.setCorePoolSize(20);
		// 设置最大线程数
		executor.setMaxPoolSize(20);
		// 设置队列容量
		executor.setQueueCapacity(Integer.MAX_VALUE);
		// 设置线程活跃时间（秒）
		executor.setKeepAliveSeconds(60);
		// 设置默认线程名称
		executor.setThreadNamePrefix("user-offers-view-record-");
		// 设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 等待所有任务结束后再关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		return executor;
	}


	@Bean("esOffersSaveExecutor")
	public TaskExecutor esOffersSaveExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 设置核心线程数
		executor.setCorePoolSize(20);
		// 设置最大线程数
		executor.setMaxPoolSize(20);
		// 设置队列容量
		executor.setQueueCapacity(Integer.MAX_VALUE);
		// 设置线程活跃时间（秒）
		executor.setKeepAliveSeconds(60);
		// 设置默认线程名称
		executor.setThreadNamePrefix("es-offers-save-record-");
		// 设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 等待所有任务结束后再关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		return executor;
	}

	@Bean("wechatPushExecutor")
	public TaskExecutor wechatPushExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 设置核心线程数
		executor.setCorePoolSize(20);
		// 设置最大线程数
		executor.setMaxPoolSize(20);
		// 设置队列容量
		executor.setQueueCapacity(Integer.MAX_VALUE);
		// 设置线程活跃时间（秒）
		executor.setKeepAliveSeconds(60);
		// 设置默认线程名称
		executor.setThreadNamePrefix("wechat-push-");
		// 设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 等待所有任务结束后再关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		return executor;
	}

	@Bean("esDataCleanExecutor")
	public TaskExecutor esDataCleanExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 设置核心线程数
		executor.setCorePoolSize(100);
		// 设置最大线程数
		executor.setMaxPoolSize(100);
		// 设置队列容量
		executor.setQueueCapacity(Integer.MAX_VALUE);
		// 设置线程活跃时间（秒）
		executor.setKeepAliveSeconds(60);
		// 设置默认线程名称
		executor.setThreadNamePrefix("esDataCleanExecutor");
		// 设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 等待所有任务结束后再关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		return executor;
	}

}
