/*
 * This file is part of MyPet
 *
 * Copyright (C) 2011-2013 Keyle
 * MyPet is licensed under the GNU Lesser General Public License.
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.entity.ai.attack;

import de.Keyle.MyPet.entity.ai.MyPetAIGoal;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.entity.EntityLiving;

public class MyPetAIMeleeAttack extends MyPetAIGoal
{
    MyPet myPet;
    EntityMyPet petEntity;
    EntityLiving targetEntity;
    double range;
    float walkSpeedModifier;
    private int ticksUntilNextHitLeft = 0;
    private int ticksUntilNextHit;
    private int timeUntilNextNavigationUpdate;

    public MyPetAIMeleeAttack(EntityMyPet petEntity, float walkSpeedModifier, double range, int ticksUntilNextHit)
    {
        this.petEntity = petEntity;
        this.myPet = petEntity.getMyPet();
        this.walkSpeedModifier = walkSpeedModifier;
        this.range = range * range;
        this.ticksUntilNextHit = ticksUntilNextHit;
    }

    @Override
    public boolean shouldStart()
    {
        if (myPet.getDamage() <= 0)
        {
            return false;
        }
        EntityLiving targetEntity = this.petEntity.getAITarget();
        if (targetEntity == null)
        {
            return false;
        }
        if (targetEntity.isDead)
        {
            return false;
        }
        if (petEntity.getMyPet().getRangedDamage() > 0 && this.petEntity.getDistanceSq(targetEntity.posX, targetEntity.boundingBox.minY, targetEntity.posZ) >= 16)
        {
            return false;
        }
        this.targetEntity = targetEntity;
        return this.petEntity.getEntitySenses().canSee(targetEntity);
    }

    @Override
    public boolean shouldFinish()
    {
        if (this.petEntity.getAITarget() == null || this.targetEntity.isDead)
        {
            return true;
        }
        else if (this.targetEntity != this.petEntity.getAITarget())
        {
            return true;
        }
        if (petEntity.getMyPet().getRangedDamage() > 0 && this.petEntity.getDistanceSq(targetEntity.posX, targetEntity.boundingBox.minY, targetEntity.posZ) >= 16)
        {
            return true;
        }
        return false;
    }

    @Override
    public void start()
    {
        this.petEntity.petNavigation.getParameters().addSpeedModifier("MeleeAttack", walkSpeedModifier);
        this.petEntity.petNavigation.navigateTo(this.targetEntity);
        this.timeUntilNextNavigationUpdate = 0;
    }

    @Override
    public void finish()
    {
        this.petEntity.petNavigation.getParameters().removeSpeedModifier("MeleeAttack");
        this.targetEntity = null;
        this.petEntity.petNavigation.stop();
    }

    @Override
    public void tick()
    {
        this.petEntity.getLookHelper().setLookPositionWithEntity(targetEntity, 30.0F, 30.0F);
        if (((this.petEntity.getEntitySenses().canSee(targetEntity))) && (--this.timeUntilNextNavigationUpdate <= 0))
        {
            this.timeUntilNextNavigationUpdate = (4 + this.petEntity.getRNG().nextInt(7));
            this.petEntity.petNavigation.navigateTo(targetEntity);
        }
        if ((this.petEntity.getDistanceSq(targetEntity.posX, targetEntity.boundingBox.minY, targetEntity.posZ) <= this.range) && (this.ticksUntilNextHitLeft-- <= 0))
        {
            this.ticksUntilNextHitLeft = ticksUntilNextHit;
            if (this.petEntity.getHeldItem() != null)
            {
                this.petEntity.swingItem();
            }
            this.petEntity.attack(targetEntity);
        }
    }
}