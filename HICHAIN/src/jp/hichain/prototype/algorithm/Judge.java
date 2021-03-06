package jp.hichain.prototype.algorithm;

import jp.hichain.prototype.basic.ChainSign;
import jp.hichain.prototype.basic.Player;
import jp.hichain.prototype.basic.SignPS;
import jp.hichain.prototype.basic.Square;
import jp.hichain.prototype.concept.AroundDir;
import jp.hichain.prototype.concept.Direction.Relation;
import jp.hichain.prototype.concept.PS;
import jp.hichain.prototype.concept.PS.Contact;
import jp.hichain.prototype.concept.PS.Type;

/**
 * 着手ルール判定
 */
public class Judge {

	/**
	 * 接点の判定をする
	 * @param player プレイヤー
	 * @param holding 設置する文字
	 * @param target 対象マス
	 * @return 接点の種類
	 */
	public static PS.Contact getContact(Player player, ChainSign holding, Square target) {
		SignPS holdingSPS = holding.getSPS();
		System.out.println("HoldingSPS:\n" + holdingSPS.toString());

		//辺判定
		System.out.println("--SIDE Judge--");
		if (getSideJudge(holdingSPS, target)) {
			return Contact.SIDE_SIDE;
		}
		System.out.println("--POINT Judge--");
		//点判定
		if (getPointJudge(player, holdingSPS, target)) {
			return Contact.POINT_POINT;
		}
		System.out.println("--CORNER Judge--");
		//角判定
		if (getCornerJudge(player, holdingSPS, target)) {
			return Contact.POINT_POINT;
		}

		return Contact.NONE;
	}

	/**
	 * 辺の接点を判定する
	 * @param holdingSPS 設置する文字のSPS
	 * @param target 対象マス
	 * @return 設置できるかどうか
	 */
	private static boolean getSideJudge(SignPS holdingSPS, Square target) {
		SignPS.TypePart typePart = holdingSPS.getTypePart(Type.SIDE);

		for (AroundDir dir : typePart.keySet()) {
			System.out.println("[" + dir + "]");

			Square aroundSquare = target.getAround(dir);
			if (aroundSquare == null || aroundSquare.isEmpty()) {
				continue;
			}

			SignPS antiSPS = aroundSquare.getSign().getSPS();
			SignPS.TypePart antiTypePart = antiSPS.getTypePart(Type.SIDE);

			System.out.println("AroundSPS[" + dir + "]:\n" + antiSPS.toString());

			boolean me1 = typePart.contains(dir, PS.Block.LEFT);
			boolean you1 = antiTypePart.contains(dir.get(Relation.OPPOSITE), PS.Block.RIGHT);
			boolean me2 = typePart.contains(dir, PS.Block.RIGHT);
			boolean you2 = antiTypePart.contains(dir.get(Relation.OPPOSITE), PS.Block.LEFT);

			if (me1 && you1 || me2 && you2) {
				System.out.println(" Result: true");
				return true;
			}

			System.out.println(" Result: false");
		}

		return false;
	}

	/**
	 * 点の接点を判定する
	 * @param player プレイヤー
	 * @param holdingSPS 設置する文字のSPS
	 * @param target 対象マス
	 * @return 設置できるかどうか
	 */
	private static boolean getPointJudge(Player player, SignPS holdingSPS, Square target) {
		SignPS.TypePart typePart = holdingSPS.getTypePart(Type.POINT);

		for (AroundDir dir : typePart.keySet()) {
			System.out.println("[" + dir + "]");

			Square aroundSquare = target.getAround(dir);
			if (aroundSquare == null || aroundSquare.isEmpty() ||  player == aroundSquare.getPlayer()) {
				continue;
			}

			SignPS antiSPS = aroundSquare.getSign().getSPS();
			SignPS.TypePart antiTypePart = antiSPS.getTypePart(Type.POINT);

			System.out.println("AroundSPS[" + dir + "]:\n" + antiSPS.toString());

			for (PS.Block block : PS.Block.values()) {
				boolean me = typePart.contains(dir, block);
				boolean you = antiTypePart.contains(dir.get(Relation.OPPOSITE), block);
				if (me && you) {
					System.out.println(" Result: true");
					return true;
				}
			}

			System.out.println(" Result: false");
		}

		return false;
	}

	/**
	 * 角の接点を判定する
	 * @param player プレイヤー
	 * @param holdingSPS 設置する文字のSPS
	 * @param target 対象マス
	 * @return 設置できるかどうか
	 */
	private static boolean getCornerJudge(Player player, SignPS holdingSPS, Square target) {
		SignPS.TypePart typePart = holdingSPS.getTypePart(Type.CORNER);

		for (AroundDir dir : typePart.keySet()) {
			System.out.println("[" + dir + "]");

			Square aroundSquare = target.getAround(dir);
			if (aroundSquare == null || aroundSquare.isEmpty() || player == aroundSquare.getPlayer()) {
				continue;
			}

			SignPS antiSPS = aroundSquare.getSign().getSPS();
			SignPS.TypePart antiTypePart = antiSPS.getTypePart(Type.CORNER);

			System.out.println("AroundSPS[" + dir + "]:\n" + antiSPS.toString());

			boolean me = typePart.contains(dir, PS.Block.CENTER);
			boolean you = antiTypePart.contains(dir.get(Relation.OPPOSITE), PS.Block.CENTER);
			if (me && you) {
				System.out.println(" Result: true");
				return true;
			}

			System.out.println(" Result: false");
		}

		return false;
	}
}
