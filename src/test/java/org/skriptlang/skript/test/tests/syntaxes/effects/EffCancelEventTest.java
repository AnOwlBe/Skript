package org.skriptlang.skript.test.tests.syntaxes.effects;

import ch.njol.skript.test.runner.SkriptJUnitTest;
import org.junit.Before;
import org.junit.Test;

public class EffCancelEventTest extends SkriptJUnitTest {

	@Before
	public void before() {
		setShutdownDelay(1);
	}

	@Test
	public void test() {
		spawnTestPig();
	}

}
