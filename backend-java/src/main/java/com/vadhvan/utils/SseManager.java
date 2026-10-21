package com.vadhvan.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseManager {

    private static final Logger log = LoggerFactory.getLogger(SseManager.class);
    // Timeout of 30 minutes; EventSource auto-reconnects on timeout/drop
    private static final Long EMITTER_TIMEOUT = 30 * 60 * 1000L;

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter createEmitter(String tokenOrUserId) {
        SseEmitter emitter = new SseEmitter(EMITTER_TIMEOUT);

        emitters.add(emitter);

        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            log.debug("SSE client completed (active: {})", emitters.size());
        });

        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(emitter);
            log.debug("SSE client timed out (active: {})", emitters.size());
        });

        emitter.onError(e -> {
            emitter.complete();
            emitters.remove(emitter);
            log.debug("SSE client error: {} (active: {})", e.getMessage(), emitters.size());
        });

        try {
            // Initial connected handshake event
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data(Map.of("message", "Connected to Vadhvan Port real-time stream")));
        } catch (Exception e) {
            emitter.complete();
            emitters.remove(emitter);
        }

        return emitter;
    }

    public void broadcast(String eventName, Object data) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (Exception e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }

    // Keep-alive heartbeat ping every 15 seconds to ensure browser EventSource remains 'Live'
    @Scheduled(fixedRate = 15000)
    public void sendHeartbeat() {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().comment("ping"));
            } catch (Exception e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }

    public int getActiveClientCount() {
        return emitters.size();
    }
}
