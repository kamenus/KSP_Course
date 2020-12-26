import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ChatClienthandler extends ChannelInboundHandlerAdapter {

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ctx.fireChannelRead(msg);
        System.out.println((String)msg);
    }
}
