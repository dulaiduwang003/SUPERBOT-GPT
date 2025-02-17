package com.cn.chat.net;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cn.chat.dto.RequestDto;
import com.cn.chat.exceptions.SocketColoseException;
import com.cn.chat.service.ChatService;
import com.cn.chat.service.impl.ChatServiceImpl;
import com.cn.common.utils.SpringContextUtil;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * The type Dialogue socket.
 *
 * @author bdth github @dulaiduwang003
 * @version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/chat/{token}", subprotocols = {"protocol"})
@SuppressWarnings("all")
@Service
public class ChatSocket {
    private Session session;

    private ChatService chatService;

    private int maxSize = 10 * 1024 * 1024;

    @OnOpen
    public void onOpen(final Session session, @PathParam("token") final String token) {
        //设置传输缓存流
        if (session.getMaxTextMessageBufferSize() <= 8192) {
            session.setMaxBinaryMessageBufferSize(maxSize);
            session.setMaxTextMessageBufferSize(maxSize);
        }
        try {
            assert session.getId() != null;
            this.session = session;
            //校验用户
            final Object id = StpUtil.getLoginIdByToken(token);
            if (id == null) {
                throw new SocketColoseException("无法获取用户信息以建立连接数据,已拒绝连接");
            }

        } catch (Exception e) {

            handleWebSocketCompletion();
            return;
        }
        if (chatService == null) {
            chatService = (ChatServiceImpl) SpringContextUtil.getBean("chatServiceImpl");
        }

    }

    @OnMessage
    public void onMessage(final String parameter) {

        try {
            final RequestDto requestDto = JSONObject.parseObject(parameter, RequestDto.class);
            chatService.execution(requestDto).doFinally(signal -> handleWebSocketCompletion())
                    .subscribe(s -> {
                        try {
                            this.session.getBasicRemote().sendText(s);
                        } catch (Exception e) {
                            throw new SocketColoseException();
                        }
                    }, throwable -> {
                        //为 Close异常时 过滤
                        if (!(throwable instanceof SocketColoseException)) {
                            sendErrorMessages(throwable.getMessage());
                        }
                    });

        } catch (Exception e) {
            sendErrorMessages(e.getMessage());
        }

    }

    private void sendErrorMessages(final String msg) {
        try {
            this.session.getBasicRemote().sendText(msg);
            handleWebSocketCompletion();
        } catch (Exception e) {

        }
    }

    @OnClose
    public void handleWebSocketCompletion() {
        try {
            this.session.close();
        } catch (IOException e) {
            log.error("关闭 WebSocket 会话失败.", e);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn("GPT websocket出现异常 原因:{}", throwable.getMessage());
        //打印堆栈
        //      throwable.printStackTrace();
    }
}
