package org.skriptlang.skript.test.tests.syntaxes.effects;

import ch.njol.skript.test.runner.SkriptJUnitTest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Goat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EffGoatHornsTest extends SkriptJUnitTest {

	private Goat goat;

	@Before
	public void before() {
		setShutdownDelay(10);
	}

	@Test
	public void test() {
		goat = spawnTestEntity(EntityType.GOAT);
	}

	@After
	public void after() {
		if (goat != null)
			goat.remove();
	}

}
