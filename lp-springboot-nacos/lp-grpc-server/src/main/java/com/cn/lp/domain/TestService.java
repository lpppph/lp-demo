package com.cn.lp.domain;

import com.cn.lp.GrpcServiceSupport;
import com.cn.lp.common.rpc.TestGrpc;
import com.cn.lp.common.rpc.model.HelloReplyMO;
import com.cn.lp.common.rpc.model.HelloRequestMO;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * Created by qirong on 2019/4/25.
 */
@GrpcService
public class TestService extends TestGrpc.TestImplBase implements GrpcServiceSupport {

    @Override
    public void sayHello(HelloRequestMO request, StreamObserver<HelloReplyMO> responseObserver) {
        wrap(request, responseObserver, (req) -> {
            return HelloReplyMO.newBuilder().setMessage("测试").build();
        });
    }
}
