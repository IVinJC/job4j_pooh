package ru.job4j.poohjms;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> map = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        Resp resp = new Resp("", "501");
        var hashMap = map.get(req.getSourceName());
        if ("POST".equals(req.httpRequestType())) {
            if (hashMap == null) {
                resp = new Resp("No Content", "204");
            } else {
                for (ConcurrentLinkedQueue<String> linkedQueue : hashMap.values()) {
                    linkedQueue.add(req.getParam());
                }
                resp = new Resp("", "200");
            }
        } else if ("GET".equals(req.httpRequestType())) {
            map.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            map.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            String poll = map.get(req.getSourceName()).get(req.getParam()).poll();
                resp = new Resp(poll, "200");
                if (poll == null) {
                    resp = new Resp("", "404");
                }
        }
        return resp;
    }
}