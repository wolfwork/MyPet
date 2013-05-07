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

package de.Keyle.MyPet.entity.types.bat;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.world.World;


@EntitySize(width = 0.5F, height = 0.9F)
public class EntityMyBat extends EntityMyPet
{
    public EntityMyBat(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/bat.png";
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            super.setMyPet(myPet);

            this.setHanging(((MyBat) myPet).ishanging());
        }
    }

    public void setHanging(boolean flags)
    {
        int i = this.getDataWatcher().getWatchableObjectByte(16);
        if (flags)
        {
            this.getDataWatcher().updateObject(16, (byte) (i | 0x1));
        }
        else
        {
            this.getDataWatcher().updateObject(16, (byte) (i & 0xFFFFFFFE));
        }
        ((MyBat) myPet).hanging = flags;
    }

    public boolean isHanging()
    {
        return ((MyBat) myPet).hanging;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(16, new Byte((byte) 0)); // hanging
    }

    /**
     * Returns the speed of played sounds
     */
    protected float getSoundPitch()
    {
        return super.getSoundPitch() * 0.95F;
    }

    @Override
    protected String getLivingSound()
    {
        return !playIdleSound() ? "" : "mob.bat.idle";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String getHurtSound()
    {
        return "mob.bat.hurt";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String getDeathSound()
    {
        return "mob.bat.death";
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }
    }

    public void onUpdate()
    {
        super.onUpdate();
        if (!worldObj.getBlockMaterial((int) posX, (int) posY, (int) posZ).isLiquid() && !worldObj.getBlockMaterial((int) posX, (int) (posY + 1.), (int) posZ).isSolid())
        {
            this.posY += 0.65;
        }
    }
}