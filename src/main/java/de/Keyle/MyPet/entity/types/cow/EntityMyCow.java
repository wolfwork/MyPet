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

package de.Keyle.MyPet.entity.types.cow;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.bukkit.Material;

@EntitySize(width = 0.9F, height = 1.3F)
public class EntityMyCow extends EntityMyPet
{
    public static boolean CAN_GIVE_MILK = true;
    public static Material GROW_UP_ITEM = Material.POTION;

    public EntityMyCow(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/cow.png";
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            super.setMyPet(myPet);

            this.setBaby(((MyCow) myPet).isBaby());
        }
    }

    public boolean isBaby()
    {
        return ((MyCow) myPet).isBaby;
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
        ((MyCow) myPet).isBaby = flag;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(12, new Integer(0)); // age
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
            if (itemStack.itemID == Item.bucketEmpty.itemID)
            {
                if (CAN_GIVE_MILK && !this.worldObj.isRemote)
                {
                    ItemStack milkBucket = new ItemStack(Item.bucketEmpty, 1, 0);

                    entityhuman.inventory.setInventorySlotContents(entityhuman.inventory.currentItem, milkBucket);
                    return true;
                }
            }
        }
        else if (getOwner().equals(entityhuman) && itemStack != null)
        {
            if (itemStack.itemID == GROW_UP_ITEM.getId())
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
        playSound("mob.cow.step", 0.15F, 1.0F);
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String getLivingSound()
    {
        return !playIdleSound() ? "" : "mob.cow.say";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String getHurtSound()
    {
        return "mob.cow.hurt";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String getDeathSound()
    {
        return "mob.cow.hurt";
    }
}