package scripts.shipsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.impl.combat.RecallDeviceStats;
import com.fs.starfarer.api.util.IntervalUtil;
import java.awt.Color;
import java.util.Iterator;
import org.lazywizard.lazylib.FastTrig;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicRender;

public class deus_WarpstreamStats extends BaseShipSystemScript {
	private static final Color AFTERIMAGE_COLOR = new Color(174, 10, 138, 100);
	private static final Color JITTER_COLOR = new Color(225, 65, 246, 60);
	private static final Color JITTER_UNDER_COLOR = new Color(110, 11, 239, 120);
	private static final Color ENGINE_COLOR = new Color(118, 7, 146, 155);
	private static final Object KEY_JITTER = new Object();
	private static final float TIME_MULT = 3.0F;
	private static final float BURST_TIME_MULT = 4.0F;
	private static final float FLUX_REDUC_PERCENT = 10.0F;
	private static final float FLUX_REDUC_PERCENT_BURST = 20.0F;
	private static final float HARD_FLUX_DIS = 20.0F;
	private static final float HARD_FLUX_DIS_BURST = 40.0F;
	private final IntervalUtil interval = new IntervalUtil(0.6F, 0.6F);

   	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		boolean player = false;
		CombatEngineAPI engine = Global.getCombatEngine();
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI)stats.getEntity();
			player = ship == Global.getCombatEngine().getPlayerShip();
			float jitterRangeBonus;
			stats.getTimeMult().modifyMult(id, 4.0F);
			stats.getBallisticWeaponFluxCostMod().modifyPercent(id, -20.0F);
			stats.getEnergyWeaponFluxCostMod().modifyPercent(id, -20.0F);
			stats.getBeamWeaponFluxCostMult().modifyPercent(id, -20.0F);
			stats.getHardFluxDissipationFraction().modifyFlat(id, 0.39999998F);
			if (player) {
				engine.getTimeMult().modifyMult(id, 0.25F);
				engine.maintainStatusForPlayerShip("bbplus_ChronoDriveStats_TOOLTIP_TWO", "graphics/icons/hullsys/bbplus_clockup.png", "Hyper Clock Up", "-20% Weapon flux cost", false);
			} else {
				engine.getTimeMult().unmodify(id);
			}

			if (effectLevel > 0.0F) {
				jitterRangeBonus = effectLevel * 5.0F;
				Iterator var10 = RecallDeviceStats.getFighters(ship).iterator();

				while(var10.hasNext()) {
					ShipAPI fighter = (ShipAPI)var10.next();
					if (!fighter.isHulk()) {
						MutableShipStatsAPI fStats = fighter.getMutableStats();
						fStats.getTimeMult().modifyMult(id, 3.0F);
						fighter.getEngineController().fadeToOtherColor(this, ENGINE_COLOR, (Color)null, effectLevel, 0.5F);
						fighter.setJitterUnder(KEY_JITTER, JITTER_COLOR, effectLevel, 5, 0.0F, jitterRangeBonus);
						fighter.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, effectLevel, 2, 0.0F, 0.0F + jitterRangeBonus * 1.0F);
						fighter.setJitterShields(true);
						if (this.interval.intervalElapsed()) {
						SpriteAPI sprite = fighter.getSpriteAPI();
						float offsetX = sprite.getWidth() / 2.0F - sprite.getCenterX();
						float offsetY = sprite.getHeight() / 2.0F - sprite.getCenterY();
						float trueOffsetX = (float)FastTrig.cos(Math.toRadians((double)(fighter.getFacing() - 90.0F))) * offsetX - (float)FastTrig.sin(Math.toRadians((double)(fighter.getFacing() - 90.0F))) * offsetY;
						float trueOffsetY = (float)FastTrig.sin(Math.toRadians((double)(fighter.getFacing() - 90.0F))) * offsetX + (float)FastTrig.cos(Math.toRadians((double)(fighter.getFacing() - 90.0F))) * offsetY;
						MagicRender.battlespace(Global.getSettings().getSprite(fighter.getHullSpec().getSpriteName()), new Vector2f(fighter.getLocation().getX() + trueOffsetX, fighter.getLocation().getY() + trueOffsetY), new Vector2f(0.0F, 0.0F), new Vector2f(fighter.getSpriteAPI().getWidth(), fighter.getSpriteAPI().getHeight()), new Vector2f(0.0F, 0.0F), fighter.getFacing() - 90.0F, 0.0F, AFTERIMAGE_COLOR, true, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F, 0.1F, 0.8F, CombatEngineLayers.BELOW_SHIPS_LAYER);
						}

						Global.getSoundPlayer().playLoop("system_targeting_feed_loop", ship, 1.0F, 1.0F, fighter.getLocation(), fighter.getVelocity());
					}
				}
			}

			if (!Global.getCombatEngine().isPaused()) {
				this.interval.advance(Global.getCombatEngine().getElapsedInLastFrame());
				if (this.interval.intervalElapsed()) {
				SpriteAPI sprite = ship.getSpriteAPI();
				jitterRangeBonus = sprite.getWidth() / 2.0F - sprite.getCenterX();
				float offsetY = sprite.getHeight() / 2.0F - sprite.getCenterY();
				float trueOffsetX = (float)FastTrig.cos(Math.toRadians((double)(ship.getFacing() - 90.0F))) * jitterRangeBonus - (float)FastTrig.sin(Math.toRadians((double)(ship.getFacing() - 90.0F))) * offsetY;
				float trueOffsetY = (float)FastTrig.sin(Math.toRadians((double)(ship.getFacing() - 90.0F))) * jitterRangeBonus + (float)FastTrig.cos(Math.toRadians((double)(ship.getFacing() - 90.0F))) * offsetY;
				MagicRender.battlespace(Global.getSettings().getSprite(ship.getHullSpec().getSpriteName()), new Vector2f(ship.getLocation().getX() + trueOffsetX, ship.getLocation().getY() + trueOffsetY), new Vector2f(0.0F, 0.0F), new Vector2f(ship.getSpriteAPI().getWidth(), ship.getSpriteAPI().getHeight()), new Vector2f(0.0F, 0.0F), ship.getFacing() - 90.0F, 0.0F, AFTERIMAGE_COLOR, true, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F, 0.1F, 0.8F, CombatEngineLayers.BELOW_SHIPS_LAYER);
				}
			}
		}
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      ShipAPI ship = null;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         Global.getCombatEngine().getTimeMult().unmodify(id);
         stats.getTimeMult().unmodify(id);
         stats.getBallisticWeaponFluxCostMod().unmodify(id);
         stats.getEnergyWeaponFluxCostMod().unmodify(id);
         stats.getBeamWeaponFluxCostMult().unmodify(id);
         stats.getHardFluxDissipationFraction().unmodify(id);
         Iterator var4 = RecallDeviceStats.getFighters(ship).iterator();

         while(var4.hasNext()) {
            ShipAPI fighter = (ShipAPI)var4.next();
            if (!fighter.isHulk()) {
               MutableShipStatsAPI fStats = fighter.getMutableStats();
               fStats.getTimeMult().unmodify(id);
            }
         }
      }

   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      return index == 0 ? new StatusData("accelerated timeflow", false) : null;
   }
}
