package Server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends ChannelInboundHandlerAdapter {
	private static final ChannelGroup  channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public void handleClientDisconnect(ChannelHandlerContext ctx) {
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			channel.writeAndFlush("User #" + incoming.remoteAddress() + " has been removed\n");
		}
		channels.remove(ctx.channel());
	}

	public void handleClientConnect(ChannelHandlerContext ctx) {
		channels.add(ctx.channel());
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			channel.writeAndFlush(("User #" + incoming.remoteAddress() + " has joined\n"));
		}
	}

	public void handleMessagesRead(ChannelHandlerContext arg0, Object arg1) {
		arg0.fireChannelRead(arg1);
		Channel incoming = arg0.channel();
		for (Channel channel : channels) {
			if (channel != incoming) {
				channel.writeAndFlush("User #" + incoming.remoteAddress() + ": " + arg1.toString() + "\n");
			} else {
				channel.writeAndFlush("Me: " + arg1.toString() + "\n");
			}
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		this.handleClientConnect(ctx);
	}
	
	@Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		this.handleClientDisconnect(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
		this.handleMessagesRead(arg0, arg1);
	}
}
