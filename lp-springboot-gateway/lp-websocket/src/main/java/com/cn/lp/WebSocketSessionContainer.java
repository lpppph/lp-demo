package com.cn.lp;

import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class WebSocketSessionContainer {

    private final Map<String, Object> webSocketSessionMap;

    private final SubProtocolWebSocketHandler websocketHandler;
    private final Field                              field_session;
    private final Function<Object, WebSocketSession> transferFn;

    @Inject
    public WebSocketSessionContainer(WebSocketHandler websocketHandler) throws ClassNotFoundException {
        this.websocketHandler = SubProtocolWebSocketHandler.class.cast(websocketHandler);

        Field field_sessions = ReflectionUtils.findField(SubProtocolWebSocketHandler.class, "sessions");
        ReflectionUtils.makeAccessible(field_sessions);
        Object idToSessionHolder = ReflectionUtils.getField(field_sessions, websocketHandler);
        this.webSocketSessionMap = idToSessionHolder != null ? (Map<String, Object>) idToSessionHolder : Map.of();

        String   typeName = ((ParameterizedType) field_sessions.getGenericType()).getActualTypeArguments()[1].getTypeName();
        Class<?> clazz    = ClassUtils.forName(typeName, null);  //WebSocketSessionHolder.class
        this.field_session = ReflectionUtils.findField(clazz, "session");
        ReflectionUtils.makeAccessible(this.field_session);  //type is WebSocketSession

        this.transferFn = (webSocketSessionHolder) -> (WebSocketSession) ReflectionUtils.getField(field_session, webSocketSessionHolder);

    }

    public Map<String, WebSocketSession> findAll() {
        Function<String, Object> fn = k -> webSocketSessionMap.get(k);
        return webSocketSessionMap.keySet().stream()
            .collect(Collectors.toUnmodifiableMap(Function.identity(), fn.andThen(transferFn)));
    }

    public WebSocketSession getOrNull(String id) {
        return Optional.ofNullable(webSocketSessionMap.getOrDefault(id, null))
            .filter(Objects::nonNull)
            .map(transferFn)
            .orElseGet(() -> null);
    }

}
