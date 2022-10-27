package ru.job4j.poohjms;

public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String httpRequest = "";
        if (content.contains("POST")) {
            httpRequest = "POST";
        } else if (content.contains("GET")) {
            httpRequest = "GET";
        }

        String poohMode = "";
        if (content.contains("queue")) {
            poohMode = "queue";
        } else if (content.contains("topic")) {
            poohMode = "topic";
        }

        String sourceName = "";
        if (content.contains("weather")) {
            sourceName = "weather";
        }
        String param = "";
        if ("POST".equals(httpRequest) && ("queue".equals(poohMode) || "topic".equals(poohMode))) {
            String[] splited = content.split(System.lineSeparator());
            param = splited[splited.length - 1];
        } else if ("GET".equals(httpRequest) && "topic".equals(poohMode)) {
            param = content.split("/")[3].split(" ")[0];
        }
        return new Req(httpRequest, poohMode, sourceName, param);
    }

    /**
     * httpRequestType - GET или POST. Он указывает на тип запроса.
     */
    public String httpRequestType() {
        return httpRequestType;
    }

    /**
     * poohMode - указывает на режим работы: queue или topic.
     */
    public String getPoohMode() {
        return poohMode;
    }

    /**
     * sourceName - имя очереди или топика.
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * param - содержимое запроса.
     */
    public String getParam() {
        return param;
    }
}