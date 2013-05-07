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

package de.Keyle.MyPet.entity.types.slime;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.ai.attack.MyPetAIMeleeAttack;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.world.World;

@EntitySize(width = 0.6F, height = 0.6F)
public class EntityMySlime extends EntityMyPet
{
    int jumpDelay;
    PathEntity lastPathEntity = null;

    public EntityMySlime(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/slime.png";
        this.jumpDelay = (this.rand.nextInt(20) + 10);
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            super.setMyPet(myPet);

            setSize(((MySlime) myPet).getSize());
        }
    }

    public int getSize()
    {
        return ((MySlime) myPet).size;
    }

    public void setSize(int value)
    {
        this.getDataWatcher().updateObject(16, new Byte((byte) value));
        EntitySize es = EntityMySlime.class.getAnnotation(EntitySize.class);
        if (es != null)
        {
            this.setSize(es.height() * value, es.width() * value);
        }
        if (petPathfinderSelector != null && petPathfinderSelector.hasGoal("MeleeAttack"))
        {
            petPathfinderSelector.replaceGoal("MeleeAttack", new MyPetAIMeleeAttack(this, 0.1F, 2 + getSize(), 20));
        }
        ((MySlime) myPet).size = value;
    }

    public void setPathfinder()
    {
        super.setPathfinder();
        petPathfinderSelector.replaceGoal("MeleeAttack", new MyPetAIMeleeAttack(this, 0.1F, 2 + getSize(), 20));
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(16, new Byte((byte) 1)); //size
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String getLivingSound()
    {
        return "";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String getHurtSound()
    {
        return getDeathSound();
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String getDeathSound()
    {
        return "mob.slime." + (getSize() > 1 ? "big" : "small");

    }

    /**
     * Method is called when pet moves
     * Is used to create the hopping motion
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.onGround && jumpDelay-- <= 0 && lastPathEntity != getNavigator().getPath())
        {
            getJumpHelper().doJump();
            jumpDelay = (this.rand.nextInt(20) + 10);
            lastPathEntity = getNavigator().getPath();
            playSound(getDeathSound(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
        }
    }
}