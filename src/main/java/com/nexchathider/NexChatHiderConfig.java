package com.nexchathider;

import lombok.RequiredArgsConstructor;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("nexchathider")
public interface NexChatHiderConfig extends Config
{
	@RequiredArgsConstructor
	enum Mode
	{
		LATEST_ONLY("Only show latest"),
		HIDE_ALL("Hide all");

		private final String name;

		@Override
		public String toString()
		{
			return name;
		}
	}

	@ConfigItem(
		keyName = "mode",
		name = "Nex messages",
		description = "Only show Nex's most recent chatbox message, or hide all of them"
	)
	default Mode mode()
	{
		return Mode.LATEST_ONLY;
	}
}
