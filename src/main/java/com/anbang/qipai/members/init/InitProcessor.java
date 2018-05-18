package com.anbang.qipai.members.init;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.jetty.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.anbang.qipai.members.cqrs.c.service.disruptor.CoreSnapshot;
import com.anbang.qipai.members.cqrs.c.service.disruptor.FileUtil;
import com.anbang.qipai.members.cqrs.c.service.disruptor.ProcessCoreCommandEventHandler;
import com.anbang.qipai.members.cqrs.c.service.disruptor.SnapshotJsonUtil;
import com.highto.framework.ddd.Command;
import com.highto.framework.ddd.CommonCommand;
import com.highto.framework.ddd.SingletonEntityRepository;

public class InitProcessor {
	@Autowired
	private HttpClient httpClient;

	@Autowired
	private HttpClient sslHttpClient;

	@Autowired
	private SnapshotJsonUtil snapshotJsonUtil;

	@Autowired
	private ProcessCoreCommandEventHandler coreCommandEventHandler;

	@Autowired
	private SingletonEntityRepository singletonEntityRepository;

	// @Autowired
	// private MemberDiamondAccountRepository memberDiamondAccountRepository;
	//
	// @Autowired
	// private MemberCashAccountRepository memberCashAccountRepository;

	@Autowired
	private ApplicationContext applicationContext;

	FileUtil fileUtil = new FileUtil();

	public void init() {

		httpClient.setFollowRedirects(false);
		try {
			httpClient.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		sslHttpClient.setFollowRedirects(false);
		try {
			sslHttpClient.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			recover();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			System.exit(0);
		}
	}

	private void recover() throws Throwable {
		// snapshot 恢复
		// core member snapshot恢复
		CoreSnapshot memberSnapshot = null;
		try {
			memberSnapshot = (CoreSnapshot) snapshotJsonUtil.recovery(coreCommandEventHandler.getFileBasePath(),
					CoreSnapshot.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (memberSnapshot != null) {
			singletonEntityRepository.fill(
					(SingletonEntityRepository) memberSnapshot.getContentMap().get(SingletonEntityRepository.class));
			// memberDiamondAccountRepository.fill((MemberDiamondAccountRepository)
			// memberSnapshot.getContentMap()
			// .get(MemberDiamondAccountRepository.class), null);
			// memberCashAccountRepository.fill(
			// (MemberCashAccountRepository)
			// memberSnapshot.getContentMap().get(MemberCashAccountRepository.class),
			// null);
		}

		// core 命令

		List<Command> commands = fileUtil.read("./", coreCommandEventHandler.getjFileNamePrefix());
		invokeCommands(commands);

	}

	private void invokeCommands(List<Command> commands)
			throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (Command command : commands) {
			if (command instanceof CommonCommand) {
				CommonCommand cmd = (CommonCommand) command;
				// List<Object> objects =
				// applicationContext.getBeansOfType(Class.forName(cmd.getType()));
				Class clazz = Class.forName(cmd.getType());
				Object service = applicationContext.getBean(clazz);
				if (cmd.getParameters() != null && cmd.getParameters().length > 0) {
					try {
						service.getClass().getMethod(cmd.getMethod(), cmd.getParameterTypes()).invoke(service,
								cmd.getParameters());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					service.getClass().getMethod(cmd.getMethod()).invoke(service);
				}
			}
		}
	}

}
