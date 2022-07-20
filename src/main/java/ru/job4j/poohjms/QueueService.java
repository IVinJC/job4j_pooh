package ru.job4j.poohjms;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        Resp resp = new Resp(null, null);
        if ("POST".equals(req.httpRequestType())) {
            ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
            queue.add(req.getParam());
            map.putIfAbsent(req.getSourceName(), queue);
            resp = new Resp("OK", "200");
        } else if ("GET".equals(req.httpRequestType())) {
            if (!map.isEmpty()) {
                String poll = map.get(req.getSourceName()).poll();
                resp = poll == null ? new Resp("", "204") : new Resp(poll, "200");
            }
        }
        return resp;
    }
}