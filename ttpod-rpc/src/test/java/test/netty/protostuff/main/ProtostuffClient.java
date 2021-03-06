package test.netty.protostuff.main;

import com.ttpod.rpc.netty.Client;
import com.ttpod.rpc.netty.codec.StringReqDec;
import com.ttpod.rpc.netty.codec.StringReqEnc;
import com.ttpod.search.bean.Pojo;
import test.netty.protostuff.codec.ProtostuffRuntimeDecoder;
import test.netty.protostuff.codec.ProtostuffRuntimeEncoder;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.net.InetSocketAddress;

/**
 * date: 14-2-6 下午9:17
 *
 * @author: yangyang.cong@ttpod.com
 */
public class ProtostuffClient {
    public static void main(String[] args) throws Exception{
        final StringReqDec decoder =  new StringReqDec();
        final StringReqEnc encoder =  new StringReqEnc();

//        byte flag = 1;
////                                ctx.flush();
//                                final ChannelFuture f = ctx.writeAndFlush(new QueryReq(flag,flag,flag,"周记录"));
        new Client(new InetSocketAddress("127.0.0.1", 8080),
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                        p.addLast("protobufDecoder", new ProtostuffRuntimeDecoder());

                        p.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                        p.addLast("protobufEncoder", new ProtostuffRuntimeEncoder());

                        p.addLast("handler", new SimpleChannelInboundHandler<Pojo>() {
                            protected void messageReceived(ChannelHandlerContext ctx, Pojo msg) throws Exception {
                                System.out.println(" ProtostuffClient Revice  :" + msg);
                            }

                            protected void channelRead0(ChannelHandlerContext ctx, Pojo msg) throws Exception {
                                messageReceived(ctx, msg);
                            }
                        });
                    }
                });

    }
}
