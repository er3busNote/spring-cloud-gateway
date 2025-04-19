package com.cloud.gateway.config.loadbalancer;

import com.cloud.gateway.supplier.ServiceSupplierHandler;
import com.cloud.gateway.supplier.dto.ServerInfo;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InfoLoadBalancerConfig {

    private static final String INFO_INSTANCE = "INFO";

    /**
     * INFO 서비스 인스턴스를 반환하는 Supplier
     * @return INFO 로드밸런스
     */
    //@Bean
    public ServiceInstanceListSupplier infoServiceInstanceListSupplier() {
        List<ServerInfo> serverInfos = new ArrayList<>();
        serverInfos.add(ServerInfo.of("localhost", 8083));
        serverInfos.add(ServerInfo.of("localhost", 8084));
        return new ServiceSupplierHandler(INFO_INSTANCE, serverInfos);
    }
}
