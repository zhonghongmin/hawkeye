package com.avengers.hawkeye.listener;

import com.avengers.hawkeye.tools.ServerInfoTools;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by 鸿敏 on 2017/3/3.
 * 容器初始化完成监听器，主要为了获取应用端口，同时初始化服务器的IP，端口，机器名
 */
@Component
public class TracerListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        String port = event.getEmbeddedServletContainer().getPort()+"";
        //顺序不要乱
        ServerInfoTools.initLocalIp();
        ServerInfoTools.initLocalPort(port);
        ServerInfoTools.initFormatIp();
        ServerInfoTools.initFormatPort();
        ServerInfoTools.initLocalHostName();
    }
}
