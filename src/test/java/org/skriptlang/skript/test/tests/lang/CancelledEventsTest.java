package org.skriptlang.skript.test.tests.lang;

import ch.njol.skript.test.runner.SkriptJUnitTest;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockFormEvent;
import org.junit.Before;
import org.junit.Test;

public class CancelledEventsTest extends SkriptJUnitTest {

	@Before
	public void before() {
		setShutdownDelay(1);
	}

	@Test
	public void callCancelledEvent() {
		BlockFormEvent event = new BlockFormEvent(getBlock(), getBlock().getState());

		// call cancelled event
		event.setCancelled(true);
		Bukkit.getPluginManager().callEvent(event);

		// call non-cancelled event
		event.setCancelled(false);
		Bukkit.getPluginManager().callEvent(event);
	}

}

