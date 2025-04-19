package com.cloud.gateway.supplier;

import com.cloud.gateway.supplier.dto.ServerInfo;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ServiceSupplierHandler implements ServiceInstanceListSupplier {

    private final String serviceId;
    private final List<ServerInfo> serverInfos;

    public ServiceSupplierHandler(String serviceId, List<ServerInfo> serverInfos) {
        this.serviceId = serviceId;
        this.serverInfos = serverInfos;
    }

    @Override
    public String getServiceId() {
        return serviceId; // lb://~ 라우팅 대상
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.just(IntStream.range(0, serverInfos.size())
                .mapToObj(i -> new DefaultServiceInstance(serviceId.toLowerCase() + (i + 1), serviceId, serverInfos.get(i).getHost(), serverInfos.get(i).getPort(), false))
                .collect(Collectors.toList()));
    }
}
