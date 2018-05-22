package com.anbang.qipai.members.config;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.anbang.qipai.members.cqrs.c.repository.SingletonEntityFactoryImpl;
import com.anbang.qipai.members.cqrs.c.service.disruptor.ProcessCoreCommandEventHandler;
import com.anbang.qipai.members.cqrs.c.service.disruptor.SnapshotJsonUtil;
import com.anbang.qipai.members.init.InitProcessor;
import com.dml.users.UserSessionsManager;
import com.highto.framework.ddd.SingletonEntityRepository;

@Configuration
@ComponentScan("com.anbang.qipai.members")
public class BeanConfig {

	@Autowired
	private SnapshotJsonUtil snapshotJsonUtil;

	@Autowired
	private ProcessCoreCommandEventHandler coreCommandEventHandler;

	@Autowired
	private SingletonEntityFactoryImpl entityFactory;

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public HttpClient httpClient() {
		return new HttpClient();
	}

	@Bean
	public HttpClient sslHttpClient() {
		return new HttpClient(new SslContextFactory());
	}

	@Bean
	public UserSessionsManager userSessionsManager() {
		return new UserSessionsManager();
	}

	@Bean
	public SingletonEntityRepository singletonEntityRepository() {
		SingletonEntityRepository singletonEntityRepository = new SingletonEntityRepository();
		singletonEntityRepository.setEntityFactory(entityFactory);
		return singletonEntityRepository;
	}

	@Bean
	public InitProcessor initProcessor() {
		InitProcessor initProcessor = new InitProcessor(httpClient(), sslHttpClient(), snapshotJsonUtil,
				coreCommandEventHandler, singletonEntityRepository(), applicationContext);
		initProcessor.init();
		return initProcessor;
	}

}
