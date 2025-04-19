package com.cloud.gateway.config.loadbalancer;

import com.cloud.gateway.supplier.ServiceSupplierHandler;
import com.cloud.gateway.supplier.dto.ServerInfo;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AccountsLoadBalancerConfig {

    private static final String ACCOUNT_INSTANCE = "ACCOUNTS";

    /**
     * ACCOUNTS 서비스 인스턴스를 반환하는 Supplier
     * @return ACCOUNTS 로드밸런스
     */
    @Bean
    public ServiceInstanceListSupplier accountsServiceInstanceListSupplier() {
        List<ServerInfo> serverInfos = new ArrayList<>();
        serverInfos.add(ServerInfo.of("localhost", 8081));
        serverInfos.add(ServerInfo.of("localhost", 8082));
        return new ServiceSupplierHandler(ACCOUNT_INSTANCE, serverInfos);
    }
}
