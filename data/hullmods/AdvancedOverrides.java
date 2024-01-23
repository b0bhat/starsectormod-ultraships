package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;


public class IntricateMachinery extends BaseLogisticsHullMod {
	
	public static final float PROFILE_MULT = 0.80f;
	public static final float MAX_CR_BONUS = -15f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getSensorProfile().modifyMult(id, PROFILE_MULT);
		stats.getMaxCombatReadiness().modifyFlat(id, MAX_CR_BONUS * 0.01f);
	}
}
