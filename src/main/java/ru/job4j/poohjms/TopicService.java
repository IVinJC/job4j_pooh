package ru.job4j.poohjms;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> map = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        Resp resp = null;
        if ("POST".equals(req.httpRequestType())) {
            if (!map.containsKey(req.getSourceName())) {
                resp = new Resp("No Content", "204");
            } else {
                for (ConcurrentLinkedQueue<String> linkedQueue : map.get(req.getSourceName()).values()) {
                    linkedQueue.add(req.getParam());
                }
                resp = new Resp("OK", "200");
            }
        } else if ("GET".equals(req.httpRequestType())) {
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> mapValue = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> hashMap = map.putIfAbsent(req.getSourceName(), mapValue);
            ConcurrentLinkedQueue<String> value = map.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            if (value == null) {
                resp = new Resp("", "404");
            } else {
                String poll = map.get(req.getSourceName()).get(req.getParam()).poll();
                resp = new Resp(poll, "200");
            }
        }
        return resp;
    }
}