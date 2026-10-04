package org.skriptlang.skript.test.tests.syntaxes.expressions;

import ch.njol.skript.test.runner.SkriptJUnitTest;
import org.bukkit.entity.Pig;
import org.junit.Before;
import org.junit.Test;

public class ExprDropsTest extends SkriptJUnitTest {

	private Pig pig;

	@Before
	public void spawnPig() {
		setShutdownDelay(1);
		pig = spawnTestPig();
	}

	@Test
	public void killPig() {
		pig.damage(100);
	}

}
