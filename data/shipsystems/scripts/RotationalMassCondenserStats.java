package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import com.fs.starfarer.api.combat.ShipAPI;

public class RotationalMassCondenserStats extends BaseShipSystemScript {

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
		} else {
			stats.getMaxSpeed().modifyFlat(id, 300f * effectLevel);
			stats.getAcceleration().modifyFlat(id, 1000f * effectLevel);
			
			//stats.getAcceleration().modifyPercent(id, 200f * effectLevel);
		}

		stats.getHullDamageTakenMult().modifyMult(id, 0.5F * effectLevel);
		stats.getArmorDamageTakenMult().modifyMult(id, 0.5F * effectLevel);
		stats.getEmpDamageTakenMult().modifyMult(id, 0.5F * effectLevel);
		
		// boolean player = false;
		// if (stats.getEntity() instanceof ShipAPI) {
		// 	ship = (ShipAPI)stats.getEntity();
		// 	player = ship == Global.getCombatEngine().getPlayerShip();
		// }

		// if (player) {
		// 	ShipSystemAPI system = getDamper(ship);
		// 	if (system != null) {
		// 		float percent = (1.0F - mult) * effectLevel * 100.0F;
		// 		Global.getCombatEngine().maintainStatusForPlayerShip(this.STATUSKEY1, system.getSpecAPI().getIconSpriteName(), system.getDisplayName(), Math.round(percent) + "% less damage taken", false);
		// 	}
		// }
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getMaxSpeed().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getHullDamageTakenMult().unmodify(id);
		stats.getArmorDamageTakenMult().unmodify(id);
		stats.getEmpDamageTakenMult().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData(" increased engine power", false);
		}
		return null;
	}
}
