package com.cloud.gateway.config;

import com.cloud.gateway.config.loadbalancer.AccountsLoadBalancerConfig;
import com.cloud.gateway.config.loadbalancer.InfoLoadBalancerConfig;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClients({
        @LoadBalancerClient(name = "ACCOUNTS", configuration = AccountsLoadBalancerConfig.class),
        @LoadBalancerClient(name = "INFO", configuration = InfoLoadBalancerConfig.class)
})
public class LoadBalancerConfig {
}
