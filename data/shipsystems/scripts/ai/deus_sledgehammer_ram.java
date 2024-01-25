package data.shipsystems.scripts.ai;

import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.util.IntervalUtil;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class deus_sledgehammer_ram implements ShipSystemAIScript {
    private ShipAPI ship;
    private CombatEngineAPI engine;
    private ShipwideAIFlags flags;
    private ShipSystemAPI system;
    private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);

    @Override
    public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
        if (!engine.isPaused()) {
            tracker.advance(amount);
            if (AIUtils.canUseSystemThisFrame(ship)) {
                if (tracker.intervalElapsed()) {
                    if (ship.isDirectRetreat()) {ship.useSystem();return;}
                    
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
                    //Vector2f end = MathUtils.getPoint(ship.getLocation(), 600f, ship.getFacing());
                    List<ShipAPI> entity = AIUtils.getNearbyAllies(ship, 450f);
                    if (!entity.isEmpty()){
                        for (ShipAPI e : entity) {                                
                            if(e.getCollisionClass()!=CollisionClass.NONE && e.getCollisionClass() != CollisionClass.FIGHTER) {
                                    if (Math.abs(MathUtils.getShortestRotation(VectorUtils.getAngle(ship.getLocation(), e.getLocation()), ship.getFacing())) <= 15f) {
                                        return;
                                    }
                                //}
                            }
                        }
                    }
                    if (target.isFighter() || target.isDrone() || target.isStation() || target.isStationModule() || target.getEngineController().isFlamedOut() || ship.isRetreating()) {
                        return;
                    }

                    if (MathUtils.isWithinRange(ship, target, 200f) && ship.getSystem().getAmmo() >= 1) {
                        ship.useSystem();
                        return;
                    }

                    // if (MathUtils.isWithinRange(ship, target, 400f)) {
                    //     flags.setFlag(AIFlags.HARASS_MOVE_IN , 2f);
                    //     return;
                    // }
                    
                    if (MathUtils.isWithinRange(ship, target, 600f) && ship.getSystem().getAmmo() >= 1) {
                        flags.setFlag(AIFlags.PHASE_ATTACK_RUN, 8f);
                        return;
                    }

                    if (flags.hasFlag(AIFlags.PURSUING) || flags.hasFlag(AIFlags.HARASS_MOVE_IN)) {
                        ship.useSystem();
                        flags.setFlag(AIFlags.DO_NOT_BACK_OFF, 3.5f);
                    }
                }
            }
        }
    }
    
    public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
        this.ship = ship;
        this.flags = flags;
        this.engine = engine;
        this.system = system;
    }
}
