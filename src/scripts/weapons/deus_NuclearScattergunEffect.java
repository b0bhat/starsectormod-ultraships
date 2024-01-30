package scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

public class deus_NuclearScattergunEffect implements OnHitEffectPlugin {
   public static float DAMAGE = 250.0F;

   public deus_NuclearScattergunEffect() {
   }

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (!shieldHit && target instanceof ShipAPI) {
         dealArmorDamage(projectile, (ShipAPI)target, point, DAMAGE);
      }

   }

   public static void dealArmorDamage(DamagingProjectileAPI projectile, ShipAPI target, Vector2f point, float armorDamage) {
      CombatEngineAPI engine = Global.getCombatEngine();
      ArmorGridAPI grid = target.getArmorGrid();
      int[] cell = grid.getCellAtLocation(point);
      if (cell != null) {
         int gridWidth = grid.getGrid().length;
         int gridHeight = grid.getGrid()[0].length;
         float damageTypeMult = getDamageTypeMult(projectile.getSource(), target);
         float damageDealt = 0.0F;

         for(int i = -2; i <= 2; ++i) {
            for(int j = -2; j <= 2; ++j) {
               if (i != 2 && i != -2 || j != 2 && j != -2) {
                  int cx = cell[0] + i;
                  int cy = cell[1] + j;
                  if (cx >= 0 && cx < gridWidth && cy >= 0 && cy < gridHeight) {
                     float damMult = 0.033333335F;
                     if (i == 0 && j == 0) {
                        damMult = 0.06666667F;
                     } else if (i <= 1 && i >= -1 && j <= 1 && j >= -1) {
                        damMult = 0.06666667F;
                     } else {
                        damMult = 0.033333335F;
                     }

                     float armorInCell = grid.getArmorValue(cx, cy);
                     float damage = armorDamage * damMult * damageTypeMult;
                     damage = Math.min(damage, armorInCell);
                     if (!(damage <= 0.0F)) {
                        target.getArmorGrid().setArmorValue(cx, cy, Math.max(0.0F, armorInCell - damage));
                        damageDealt += damage;
                     }
                  }
               }
            }
         }

         if (damageDealt > 0.0F) {
            if (Misc.shouldShowDamageFloaty(projectile.getSource(), target)) {
               engine.addFloatingDamageText(point, damageDealt, Misc.FLOATY_ARMOR_DAMAGE_COLOR, target, projectile.getSource());
            }

            target.syncWithArmorGridState();
         }

      }
   }

   public static float getDamageTypeMult(ShipAPI source, ShipAPI target) {
    if (source != null && target != null) {
       float damageTypeMult = target.getMutableStats().getArmorDamageTakenMult().getModifiedValue();
       switch (target.getHullSize()) {
          case FIGHTER:
             damageTypeMult *= source.getMutableStats().getDamageToFighters().getModifiedValue();
             break;
          case FRIGATE:
             damageTypeMult *= source.getMutableStats().getDamageToFrigates().getModifiedValue();
             break;
          case DESTROYER:
             damageTypeMult *= source.getMutableStats().getDamageToDestroyers().getModifiedValue();
             break;
          case CRUISER:
             damageTypeMult *= source.getMutableStats().getDamageToCruisers().getModifiedValue();
             break;
          case CAPITAL_SHIP:
             damageTypeMult *= source.getMutableStats().getDamageToCapital().getModifiedValue();
        default:
            break;
       }

       return damageTypeMult;
    } else {
       return 1.0F;
    }
 }
}
