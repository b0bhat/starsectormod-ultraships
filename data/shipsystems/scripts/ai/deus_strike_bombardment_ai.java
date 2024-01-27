package data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class deus_strike_bombardment_ai implements ShipSystemAIScript {
    private ShipAPI ship;
    private CombatEngineAPI engine;
    private ShipwideAIFlags flags;
    private ShipSystemAPI system;
    private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);

    @Override
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
      tracker.advance(amount);
      if (AIUtils.canUseSystemThisFrame(ship)) {
         if (tracker.intervalElapsed()) {
            
            if (target == null) {
               return;
            }

            if (ship.getShipTarget() == null) {
               ship.setShipTarget(target);
               return;
            }

            if (!target.isAlive() || target.isAlly()) {
               return;
            }

            if (target.isFighter()) {
               return;
            }

            // if (ship.getSystem().getState() == ShipSystemAPI.SystemState.ACTIVE && ship.getFluxLevel() >= MathUtils.getRandomNumberInRange(0.75f, 0.9f)) {
            //    ship.useSystem();
            // }

            if (MathUtils.isWithinRange(ship, target, 600f) && ship.getSystem().getState() == ShipSystemAPI.SystemState.IDLE && ship.getFluxLevel() <= MathUtils.getRandomNumberInRange(0.5f, 0.6f)) { //need to check flux
               ship.useSystem();
               flags.setFlag(AIFlags.DO_NOT_BACK_OFF, 2f);
               return;
            }
            
            if (MathUtils.isWithinRange(ship, target, 800f) && ship.getSystem().getState() == ShipSystemAPI.SystemState.IDLE) { //need to check flux
               flags.setFlag(AIFlags.MANEUVER_TARGET, 3f);
               return;
            }
         }

         //   if (flags.hasFlag(AIFlags.PURSUING) || flags.hasFlag(AIFlags.HARASS_MOVE_IN)) {
         //       ship.useSystem();
         //       flags.setFlag(AIFlags.DO_NOT_BACK_OFF, 3.5f);
         //   }
         }
      }
    
    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.flags = flags;
        this.engine = engine;
        this.system = system;
    }
}
