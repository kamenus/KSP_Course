import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerhandler extends ChannelInboundHandlerAdapter {
	private static final ChannelGroup  channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		channels.add(ctx.channel());
	    Channel incoming = ctx.channel();
        for (Channel channel : channels) {
        	channel.writeAndFlush(("[SERVER] - " + incoming.remoteAddress() + " has joined\n"));
        }
	}
	
	@Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
        for (Channel channel : channels) {
        	channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " has removed\n");
        }
        channels.remove(ctx.channel());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
		arg0.fireChannelRead(arg1);
		Channel incoming = arg0.channel();
		for (Channel channel : channels) {
			if (channel != incoming) {
				channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + arg1.toString() + "\n");
			}
		}
	}
}
