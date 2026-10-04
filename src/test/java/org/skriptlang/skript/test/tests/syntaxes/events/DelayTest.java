package org.skriptlang.skript.test.tests.syntaxes.events;

import ch.njol.skript.test.runner.SkriptJUnitTest;
import org.bukkit.Bukkit;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class DelayTest extends SkriptJUnitTest {

	@Before
	public void before() {
		setShutdownDelay(40);
	}

	@Test
	public void test() {
		Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("Skript"), () -> {
			fail("Test should fail");
		}, 20L);
	}
}
