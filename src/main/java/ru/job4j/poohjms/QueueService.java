package ru.job4j.poohjms;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        Resp resp = new Resp("", "501");
        if ("POST".equals(req.httpRequestType())) {
            ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
            queue.add(req.getParam());
            map.putIfAbsent(req.getSourceName(), queue);
        } else if ("GET".equals(req.httpRequestType())) {
            String poll = map.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>()).poll();
            resp = poll == null ? new Resp("", "204") : new Resp(poll, "200");
        }
        return resp;
    }
}