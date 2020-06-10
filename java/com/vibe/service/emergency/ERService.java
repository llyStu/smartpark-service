package com.vibe.service.emergency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.vibe.pojo.emergency.Action;
import com.vibe.pojo.emergency.EmergencyEvent;
import com.vibe.pojo.emergency.EventType;
import com.vibe.pojo.emergency.HandlerAction;
import com.vibe.pojo.emergency.Operation;

@Service
public class ERService {
	
	private IBMSClient[] clientObject = {
		new VibeWebClient(this)
	};
	private List<IBMSClient> disconn = new ArrayList<>(Arrays.asList(clientObject));
	private Map<String, IBMSClient> clients = new HashMap<>();
	private Map<String, Action> actions = new HashMap<>();
	private List<EventType> eventTypes = new ArrayList<>();
	
	public List<Action> queryAllAction() {
		return Arrays.asList(actions.values().toArray(new Action[0]));
	}
	public List<EventType> queryAllEventType() {
		return Collections.unmodifiableList(eventTypes);
	}
	
	public void handleEvent(EmergencyEvent event, IBMSClient src) {
		List<Operation> findOperationByEvent = findOperationByEvent(event.getType(), event.getLevel());
		
		Map<String, List<Operation>> handlers = new HashMap<>();
		for (Operation operation : findOperationByEvent) {
			Action action = this.toAction(operation.getActionId());
			operation.setAction(action);
			
			String key = operation.getTarget() != null ? operation.getTarget()
					: (action != null ? action.getDefaultTarget() : Action.TARGET_SELF);
			List<Operation> list = handlers.get(key);
			if (list == null) {
				list = new ArrayList<>();
				handlers.put(key, list);
			}
			list.add(operation);
		}
		List<Operation> targetSelf = handlers.remove(Action.TARGET_SELF);
		handle(targetSelf, event, src);
		
		List<Operation> targetSrc = handlers.remove(Action.TARGET_SRC);
		src.handle(targetSrc, event, src);
		
		if (!handlers.isEmpty()) for (Entry<String, List<Operation>> operation : handlers.entrySet()) {
			IBMSClient ibmsClient = clients.get(operation.getKey());
			if (ibmsClient == null) {
				System.out.println(operation.getKey() +" 不存在或遇到连接错误，已忽略");
				continue;
			}
			ibmsClient.handle(operation.getValue(), event, src);
		}
	}
	
	private void handle(List<Operation> operations, EmergencyEvent event, IBMSClient src) {
		if (operations == null || operations.isEmpty()) return;
		
		for (Operation operation : operations) {
			try {
				Action action = operation.getAction();
				if (action != null && action.isHandler()) {
					((HandlerAction) action).handle(operation, event, src);
				} else {
					System.out.println(action +" 没有对应动作，已忽略");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Action toAction(String action) {
		return actions.get(action);
	}
	

	public List<Operation> findOperationByEvent(String event, String level) {
		return Arrays.asList(
				new Operation("DO_SEND_LIGHTS_ON", event, level),
				new Operation("DO_OPEN_DOORS", event, level),
				new Operation("DO_BROADCAST", event, level),
				new Operation("DO_SEND_EMAIL", event, level)
		);
	}

	@PostConstruct
	private void init() {
		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(this::connect, 0, 1, TimeUnit.MINUTES);
		for (Action action : HandlerAction.actions) {
			this.actions.put(action.getName(), action);
		}
	}
	
	private void connect() {
		for (int i=disconn.size()-1; i>=0; --i) {
			disconn.get(i).init();
		}
	}
	public void disconn(IBMSClient client) {
		clients.remove(client.getName());
		disconn.add(client);
	}

	public void conn(IBMSClient client) {
		disconn.remove(client);
		clients.put(client.getName(), client);
	}
}
