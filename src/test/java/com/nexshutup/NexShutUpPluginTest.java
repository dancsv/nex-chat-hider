package com.nexshutup;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class NexShutUpPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(NexShutUpPlugin.class);
		RuneLite.main(args);
	}
}
