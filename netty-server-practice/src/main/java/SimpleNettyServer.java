import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class SimpleNettyServer {
    private final int port;

    public SimpleNettyServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 이벤트 그룹 1개, 이벤트 루프 1개, 쓰레드 1개, selector 1개
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class) // bossGroup은 NioServerSocketChannel을 사용 -> TCP 요청 받아서 workerGroup에 넘기는 것까지는 NioServerSocketChannel이 담당. 이 과정에서 굳이 여러 개의 스레드가 필요하지 않아서 1개로 지정
                .childHandler(new ChannelInitializer<SocketChannel>() { // workerGroup은 SocketChannel을 사용 -> TCP 요청을 받아서 처리하는 것까지는 SocketChannel이 담당. 이 과정에서 여러 개의 스레드가 필요하므로 workerGroup은 여러 개로 지정.
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new HttpRequestDecoder());
                        ch.pipeline().addLast(new HttpResponseEncoder());
                        ch.pipeline().addLast(new HttpServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            System.out.println("Server is listening on http://localhost:" + port);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new SimpleNettyServer(port).run();
    }
}
