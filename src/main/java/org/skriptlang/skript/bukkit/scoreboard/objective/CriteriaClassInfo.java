package org.skriptlang.skript.bukkit.scoreboard.objective;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.yggdrasil.Fields;
import org.bukkit.scoreboard.Criteria;

import java.io.StreamCorruptedException;

// Criteria is an interface, not an enum so it cannot be an EnumClassInfo
public class CriteriaClassInfo extends ClassInfo<Criteria> {

	public CriteriaClassInfo() {
		super(Criteria.class, "criteria");
		this.user("criterias?")
			.name("Criteria")
			.description("""
				Represents a criteria.
				""")
			.parser(new CriteriaParser())
			.serializer(new CriteriaSerializer())
			.since("INSERT VERSION")
			.defaultExpression(new EventValueExpression<>(Criteria.class));
	}

	private static class CriteriaParser extends Parser<Criteria> {
		//<editor-fold desc="objective parser" defaultstate="collapsed">
		@Override
		public boolean canParse(ParseContext context) {
			return false;
		}

		@Override
		public String toString(Criteria criteria, int flags) {
			return criteria.getName();
		}

		@Override
		public String toVariableNameString(Criteria criteria) {
			return criteria.getName();
		}
		//</editor-fold>
	}

	private static class CriteriaSerializer extends Serializer<Criteria> {
		//<editor-fold desc="criteria serializer" defaultstate="collapsed">
		@Override
		public Fields serialize(Criteria criteria) {
			Fields fields = new Fields();
			fields.putObject("id", criteria.getName());
			return fields;
		}

		@Override
		public void deserialize(Criteria criteria, Fields fields) {
			assert false;
		}

		@Override
		protected Criteria deserialize(Fields fields) throws StreamCorruptedException {
			String id = fields.getObject("id", String.class);

			if (id == null)
				throw new StreamCorruptedException();

			return Criteria.create(id);
		}

		@Override
		public boolean mustSyncDeserialization() {
			return true;
		}

		@Override
		public boolean canBeInstantiated() {
			return false;
		}
		//</editor-fold>
	}

}
