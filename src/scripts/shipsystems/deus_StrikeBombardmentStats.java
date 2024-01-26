package scripts.shipsystems;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class deus_StrikeBombardmentStats extends BaseShipSystemScript {

	public static final float DAMAGE_BONUS_PERCENT = 500f;
	public static final float RANGE_MALUS_PERCENT = 40f;
	public static final float GUIDANCE_BONUS = 20f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		
		float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		float rangeMalusPercent = -RANGE_MALUS_PERCENT * effectLevel;
		float guidanceBonusPercent = GUIDANCE_BONUS * effectLevel;
		stats.getMissileWeaponDamageMult().modifyPercent(id, bonusPercent);
		stats.getMissileWeaponRangeBonus().modifyPercent(id, rangeMalusPercent);
		stats.getMissileGuidance().modifyPercent(id, guidanceBonusPercent);
		
		//float damageTakenPercent = EXTRA_DAMAGE_TAKEN_PERCENT * effectLevel;
//		stats.getArmorDamageTakenMult().modifyPercent(id, damageTakenPercent);
//		stats.getHullDamageTakenMult().modifyPercent(id, damageTakenPercent);
//		stats.getShieldDamageTakenMult().modifyPercent(id, damageTakenPercent);
		//stats.getWeaponDamageTakenMult().modifyPercent(id, damageTakenPercent);
		//stats.getEngineDamageTakenMult().modifyPercent(id, damageTakenPercent);
		
		//stats.getBeamWeaponFluxCostMult().modifyMult(id, 10f);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMissileWeaponDamageMult().unmodify(id);
		stats.getMissileWeaponRangeBonus().unmodify(id);
		stats.getMissileGuidance().unmodify(id);
//		stats.getArmorDamageTakenMult().unmodify(id);
//		stats.getHullDamageTakenMult().unmodify(id);
//		stats.getShieldDamageTakenMult().unmodify(id);
//		stats.getWeaponDamageTakenMult().unmodify(id);
//		stats.getEngineDamageTakenMult().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		float rangeMalusPercent = -RANGE_MALUS_PERCENT * effectLevel;
		float guidanceBonusPercent = GUIDANCE_BONUS * effectLevel;
		//float damageTakenPercent = EXTRA_DAMAGE_TAKEN_PERCENT * effectLevel;
		if (index == 0) {
			return new StatusData("+" + (int) bonusPercent + "% missile weapon damage" , false);
		} else if (index == 1) {
			return new StatusData("-" + (int) rangeMalusPercent + "% missile range", true);
		} else if (index == 2) {
			return new StatusData("+" + (int) guidanceBonusPercent + "% missile guidance improvement", false);
		}
		return null;
	}
}
