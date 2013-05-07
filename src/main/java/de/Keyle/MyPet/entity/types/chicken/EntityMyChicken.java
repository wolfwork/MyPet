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

package de.Keyle.MyPet.entity.types.chicken;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.bukkit.Material;

@EntitySize(width = 0.3F, height = 0.7F)
public class EntityMyChicken extends EntityMyPet
{
    public static boolean CAN_LAY_EGGS = true;
    public static Material GROW_UP_ITEM = Material.POTION;

    private int nextEggTimer;

    public EntityMyChicken(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/chicken.png";
        nextEggTimer = (rand.nextInt(6000) + 6000);
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            super.setMyPet(myPet);

            this.setBaby(((MyChicken) myPet).isBaby());
        }
    }

    public boolean isBaby()
    {
        return ((MyChicken) myPet).isBaby;
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
        ((MyChicken) myPet).isBaby = flag;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(12, new Integer(0)); // age
    }

    public boolean interact(EntityPlayer entityhuman)
    {
        if (super.interact(entityhuman))
        {
            return true;
        }

        ItemStack itemStack = entityhuman.inventory.getItemStack();

        if (getOwner().equals(entityhuman) && itemStack != null)
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
        playSound("mob.chicken.step", 0.15F, 1.0F);
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String getLivingSound()
    {
        return !playIdleSound() ? "" : "mob.chicken.say";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String getHurtSound()
    {
        return "mob.chicken.hurt";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String getDeathSound()
    {
        return "mob.chicken.hurt";
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        if (CAN_LAY_EGGS && !worldObj.isRemote && --nextEggTimer <= 0)
        {
            worldObj.playSoundAtEntity(this, "mob.chicken.plop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            dropItem(Item.egg.itemID, 1);
            nextEggTimer = (rand.nextInt(6000) + 6000);
        }
    }
}