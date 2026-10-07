package com.nexchathider;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MessageNode;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@PluginDescriptor(
	name = "Nex Chat Hider",
	description = "Hides Nex's chatbox shouts during the fight, keeping only her latest line or hiding them all",
	tags = {"nex", "chat", "spam", "gwd", "zaros", "mute", "ignore", "filter", "hide", "silence", "quiet", "block", "messages", "declutter"}
)
public class NexChatHiderPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private NexChatHiderConfig config;

	private int latestNexMessageId = -1;

	@Override
	protected void startUp()
	{
		clientThread.invoke(() ->
		{
			for (MessageNode node : client.getMessages())
			{
				if (isNex(node))
				{
					latestNexMessageId = Math.max(latestNexMessageId, node.getId());
				}
			}
			client.refreshChat();
		});
	}

	@Override
	protected void shutDown()
	{
		latestNexMessageId = -1;
		clientThread.invoke(client::refreshChat);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("nexchathider"))
		{
			clientThread.invoke(client::refreshChat);
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (isNex(event.getMessageNode()))
		{
			latestNexMessageId = event.getMessageNode().getId();
		}
	}

	// Same hook the built-in Chat Filter uses: runs for every line each time the chatbox is drawn
	@Subscribe
	public void onScriptCallbackEvent(ScriptCallbackEvent event)
	{
		if (!event.getEventName().equals("chatFilterCheck"))
		{
			return;
		}

		int[] intStack = client.getIntStack();
		int intStackSize = client.getIntStackSize();
		int messageId = intStack[intStackSize - 1];
		MessageNode node = client.getMessages().get(messageId);

		if (node != null && isNex(node)
			&& (config.mode() == NexChatHiderConfig.Mode.HIDE_ALL || messageId != latestNexMessageId))
		{
			intStack[intStackSize - 3] = 0;
		}
	}

	// NPC_SAY messages arrive as "Nex|<line>"; the chatbox renders the "|" as ": "
	private static boolean isNex(MessageNode node)
	{
		return node.getType() == ChatMessageType.NPC_SAY
			&& Text.removeTags(node.getValue()).startsWith("Nex|");
	}

	@Provides
	NexChatHiderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NexChatHiderConfig.class);
	}
}
