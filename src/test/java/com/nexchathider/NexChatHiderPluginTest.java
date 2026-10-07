package com.nexchathider;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class NexChatHiderPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(NexChatHiderPlugin.class);
		RuneLite.main(args);
	}
}
