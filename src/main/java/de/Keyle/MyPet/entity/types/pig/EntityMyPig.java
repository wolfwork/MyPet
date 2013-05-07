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

package de.Keyle.MyPet.entity.types.pig;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@EntitySize(width = 0.9F, height = 0.9F)
public class EntityMyPig extends EntityMyPet
{
    public static org.bukkit.Material GROW_UP_ITEM = org.bukkit.Material.POTION;

    public EntityMyPig(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/pig.png";
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            super.setMyPet(myPet);

            this.setSaddle(((MyPig) myPet).hasSaddle());
            this.setBaby(((MyPig) myPet).isBaby());
        }
    }

    public boolean hasSaddle()
    {
        return ((MyPig) myPet).hasSaddle;
    }

    public void setSaddle(boolean flag)
    {
        if (flag)
        {
            this.getDataWatcher().updateObject(16, (byte) 1);
        }
        else
        {
            this.getDataWatcher().updateObject(16, (byte) 0);
        }
        ((MyPig) myPet).hasSaddle = flag;
    }

    public boolean isBaby()
    {
        return ((MyPig) myPet).isBaby;
    }

    @SuppressWarnings("boxing")
    public void setBaby(boolean flag)
    {
        if (flag)
        {
            this.getDataWatcher().updateObject(12, Integer.valueOf(Integer.MIN_VALUE));
        }
        else
        {
            this.getDataWatcher().updateObject(12, new Integer(0));
        }
        ((MyPig) myPet).isBaby = flag;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(16, new Byte((byte) 0)); // saddle
        this.getDataWatcher().addObject(12, new Integer(0));        // age
    }

    /**
     * Is called when player rightclicks this MyPet
     * return:
     * true: there was a reaction on rightclick
     * false: no reaction on rightclick
     */
    public boolean interact(EntityPlayer entityhuman)
    {
        if (super.interact(entityhuman))
        {
            return true;
        }

        ItemStack itemStack = entityhuman.inventory.getItemStack();

        if (getOwner().equals(entityhuman) && itemStack != null)
        {
            if (itemStack.itemID == 329 && !((MyPig) myPet).hasSaddle())
            {
                if (!entityhuman.capabilities.isCreativeMode)
                {
                    --itemStack.stackSize;
                }
                if (itemStack.stackSize <= 0)
                {
                    entityhuman.inventory.setInventorySlotContents(entityhuman.inventory.currentItem, null);
                }
                ((MyPig) myPet).setSaddle(true);
                return true;
            }
            else if (itemStack.itemID == Item.shears.itemID && ((MyPig) myPet).hasSaddle())
            {
                if (!this.worldObj.isRemote)
                {
                    ((MyPig) myPet).setSaddle(false);
                    if (!entityhuman.capabilities.isCreativeMode)
                    {
                        EntityItem entityitem = this.entityDropItem(new ItemStack(Item.saddle.itemID, 1, 1), 1.0F);
                        entityitem.motionY += (double) (this.rand.nextFloat() * 0.05F);
                        entityitem.motionX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                        entityitem.motionZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                    }
                    playSound("mob.sheep.shear", 1.0F, 1.0F);
                }
                itemStack.damageItem(1, entityhuman);
            }
            else if (itemStack.itemID == GROW_UP_ITEM.getId())
            {
                if (isBaby())
                {
                    if (!entityhuman.capabilities.isCreativeMode)
                    {
                        if (--itemStack.stackSize <= 0)
                        {
                            entityhuman.inventory.setInventorySlotContents(entityhuman.inventory.currentItem, null);
                        }
                    }
                    this.setBaby(false);
                    return true;
                }
            }
        }
        return false;
    }

    protected void playStepSound(int i, int j, int k, int l)
    {
        playSound("mob.pig.step", 0.15F, 1.0F);
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String getLivingSound()
    {
        return !playIdleSound() ? "" : "mob.pig.say";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String getHurtSound()
    {
        return "mob.pig.say";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String getDeathSound()
    {
        return "mob.pig.death";
    }
}