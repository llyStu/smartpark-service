package com.vibe.service.information.push;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;

@ServerEndpoint("/bulletin")
public class Client {
	private static final String DEBUG = "DEBUG_";
	public static final String SUBSCRIBE = "SUBSCRIBE";
	public static final String UNSUBSCRIBE = "UNSUBSCRIBE";
	
	private static ConcurrentLinkedQueue<Client> clients = new ConcurrentLinkedQueue<>();
	private static ScheduledExecutorService scheduledThread = Executors.newSingleThreadScheduledExecutor();
	private static Map<String, BiConsumer<Client, Msg>> listener = new HashMap<>();
	private MessageQueue<String> msgQueue;
	ScheduledFuture<?> heartBeatA, heartBeatB;
	private Session sess;
	private Set<String> subscribe;
	private AtomicLong hbFlag = new AtomicLong(System.currentTimeMillis());
	
	
	public Client() {
		msgQueue = new MessageQueue<>(100, msgs -> {
			try {
				hbFlag.set(System.currentTimeMillis());
				String msg = Msg.JSON.writeValueAsString(msgs);
				sess.getBasicRemote().sendText(msg);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		this.subscribe = new ConcurrentSkipListSet<>();
	}

	public static void setListener(String type, BiConsumer<Client, Msg> handler) {
		listener.put(type, handler);
	}

	@OnOpen
	public void onOpen(Session session) throws Exception {
		this.sess = session;
		this.heartBeatA = msgQueue.addHeartBeat(500, scheduledThread);
		session.getUserProperties().put("@self", this);
		clients.add(this);
		
		this.heartBeatB = scheduledThread.scheduleWithFixedDelay(() -> {
			long prev = hbFlag.longValue();
			long now = System.currentTimeMillis();
			if (now - prev > 4500) {
				try {
					hbFlag.set(now);
					sess.getBasicRemote().sendText("[]");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 5, 5, TimeUnit.SECONDS);
	}

	@OnClose
	public void onClose(Session session) {
		clients.remove(this);
		this.subscribe.clear();
		this.heartBeatA.cancel(true);
		this.heartBeatB.cancel(true);
		this.msgQueue.clear();
	}
	
	public void write(Msg msg) {
		if (!this.subscribe.contains(msg.getType())
				&& !this.subscribe.contains(msg.typeAndDesc())) return;
		
		String data = msg.toString();
		if (msgQueue.add(data)) return;
		
		int i = 5;
		while (!msgQueue.add(data) && i --> 0);
		if (i == 0) {
			throw new RuntimeException("同一时间发送数据太多，请尝试调整消息队列大小");
		}
	}
	
	public static void writeToAll(Msg msg) {
		for (Client it : clients) {
			it.write(msg);
		}
	}


	@OnMessage
	public void onMessage(String message, Session session) {
		//System.out.println("onMessage: "+ message);
		try {
			Msg msg = Msg.from(message);
			if (msg.getType().startsWith(DEBUG)) {
				msg.setType(msg.getType().substring(DEBUG.length()));
				writeToAll(msg);
				return;
			}
			
			BiConsumer<Client, Msg> handler = null;
			switch (msg.getData()) {
			case SUBSCRIBE:
				this.subscribe.add(msg.typeAndDesc());
				handler = listener.get(SUBSCRIBE +"="+ msg.getType());
				break;
			case UNSUBSCRIBE:
				this.subscribe.remove(msg.typeAndDesc());
				handler = listener.get(UNSUBSCRIBE +"="+ msg.getType());
				break;
			default:
				handler = listener.get(msg.getType());
				break;
			}
			if (handler != null) handler.accept(this, msg);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	@OnError
	public void onError(Session session, Throwable error) {
		error.printStackTrace();
	}
}

