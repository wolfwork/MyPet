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

package de.Keyle.MyPet.entity.types.wither;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.world.World;

@EntitySize(width = 0.9F, height = 4.0F)
public class EntityMyWither extends EntityMyPet
{
    public EntityMyWither(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/wither.png";
    }


    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(16, new Integer(300));   // Healthbar
        this.getDataWatcher().addObject(17, new Integer(0));     // target EntityID
        this.getDataWatcher().addObject(18, new Integer(0));     // N/A
        this.getDataWatcher().addObject(19, new Integer(0));     // N/A
        this.getDataWatcher().addObject(20, new Integer(0));     // blue (1/0)
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String getLivingSound()
    {
        return !playIdleSound() ? "" : "mob.wither.idle";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String getHurtSound()
    {
        return "mob.wither.hurt";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String getDeathSound()
    {
        return "mob.wither.death";
    }

    @Override
    protected void updateAITick()
    {
        this.getDataWatcher().updateObject(16, (int) (300. * getHealth() / getMaxHealth())); // update healthbar
    }
}