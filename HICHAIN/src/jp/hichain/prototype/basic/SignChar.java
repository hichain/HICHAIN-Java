package jp.hichain.prototype.basic;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import jp.hichain.prototype.concept.Chain;

public class SignChar {
	private static Map<Character, SignChar> signs = new HashMap<>();

	private char signChar;
	private EnumMap<Chain, EnumMap<Chain.Relation, SignChar>> relationMap;

	private SignChar(char ch) {
		signChar = ch;
		relationMap = new EnumMap<>(Chain.class);
	}

	static {
		init();
	}

	public char get() {
		return signChar;
	}

	public SignChar getRelation(Chain kind, Chain.Relation relation) {
		return getRelation(kind, relation, 1);
	}

	public SignChar getRelation(Chain kind, Chain.Relation relation, int times) {
		if (times == 0) return this;
		if (!relationMap.containsKey(kind)) return null;
		SignChar nextSC = relationMap.get(kind).get(relation);
		if (nextSC == null) return null;
		return nextSC.getRelation(kind, relation, times-1);
	}

	public static boolean contains(char ch) {
		return signs.containsKey(ch);
	}

	public static SignChar get(char ch) {
		return signs.get(ch);
	}

	private static void init() {
		signs.put( ' ', new SignChar(' ') );

		for (Chain kind : Chain.values()) {
			String orderString = kind.getOrderString();
			char [] chs = orderString.toCharArray();
			//signs生成
			for (char ch : chs) {
				if (signs.containsKey(ch)) {
					continue;
				}
				signs.put( ch, new SignChar(ch) );
			}

			//nextMap生成
			for (int i = 0; i < chs.length; i++) {
				int l = (i == 0) ? chs.length-1 : i-1;
				int r = (i == chs.length-1) ? 0 : i+1;

				SignChar sc = signs.get(chs[i]);
				SignChar lsc = (kind == Chain.IDENTICAL) ? sc : signs.get(chs[l]);
				SignChar rsc = (kind == Chain.IDENTICAL) ? sc : signs.get(chs[r]);

				EnumMap<Chain.Relation, SignChar> map = new EnumMap<>(Chain.Relation.class);
				map.put( Chain.Relation.CHILD, rsc );
				map.put( Chain.Relation.PARENT, lsc );

				sc.relationMap.put(kind, map);
			}
		}

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		SignChar that = (SignChar)obj;
		return signChar == that.signChar;
	}

	@Override
	public String toString() {
		return String.valueOf(signChar);
	}
}
