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

package de.Keyle.MyPet.entity.types.sheep;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.ai.movement.MyPetAIEatGrass;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.bukkit.DyeColor;
import org.bukkit.Material;

@EntitySize(width = 0.9F, height = 1.3F)
public class EntityMySheep extends EntityMyPet
{
    public static boolean CAN_BE_SHEARED = true;
    public static boolean CAN_REGROW_WOOL = true;
    public static Material GROW_UP_ITEM = Material.POTION;

    public EntityMySheep(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/sheep.png";
    }

    public void setPathfinder()
    {
        super.setPathfinder();
        petPathfinderSelector.addGoal("EatGrass", new MyPetAIEatGrass(this, 0.02));
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            super.setMyPet(myPet);

            this.setColor(((MySheep) myPet).getColor());
            this.setSheared(((MySheep) myPet).isSheared());
            this.setBaby(((MySheep) myPet).isBaby());
        }
    }

    public DyeColor getColor()
    {
        return ((MySheep) myPet).color;
    }

    public void setColor(DyeColor color)
    {
        setColor(color.getWoolData());
    }

    public void setColor(byte color)
    {
        this.getDataWatcher().updateObject(16, color);
        ((MySheep) myPet).color = DyeColor.getByWoolData(color);
    }

    public boolean isSheared()
    {
        return ((MySheep) myPet).isSheared;
    }

    public void setSheared(boolean flag)
    {

        byte b0 = this.getDataWatcher().getWatchableObjectByte(16);
        if (flag)
        {
            this.getDataWatcher().updateObject(16, (byte) (b0 | 16));
        }
        else
        {
            this.getDataWatcher().updateObject(16, (byte) (b0 & -17));
        }
        ((MySheep) myPet).isSheared = flag;
    }

    public boolean isBaby()
    {
        return this.getDataWatcher().getWatchableObjectInt(12) < 0;
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
        ((MySheep) myPet).isBaby = flag;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(16, new Byte((byte) 0)); // color/sheared
        this.getDataWatcher().addObject(12, new Integer(0));     // age
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
            if (itemStack.itemID == 351 && itemStack.getItemDamage() != ((MySheep) myPet).getColor().getDyeData())
            {
                if (itemStack.getItemDamage() <= 15)
                {
                    setColor(DyeColor.getByDyeData((byte) itemStack.getItemDamage()));
                    if (!entityhuman.capabilities.isCreativeMode)
                    {
                        if (--itemStack.stackSize <= 0)
                        {
                            entityhuman.inventory.setInventorySlotContents(entityhuman.inventory.currentItem, null);
                        }
                    }
                    return true;
                }
            }
            else if (CAN_BE_SHEARED && itemStack.itemID == Item.shears.itemID && !((MySheep) myPet).isSheared())
            {
                if (!this.worldObj.isRemote)
                {
                    ((MySheep) myPet).setSheared(true);
                    int i = 1 + this.rand.nextInt(3);

                    for (int j = 0 ; j < i ; ++j)
                    {
                        EntityItem entityitem = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, ((MySheep) myPet).getColor().getDyeData()), 1.0F);

                        entityitem.motionY += (double) (this.rand.nextFloat() * 0.05F);
                        entityitem.motionX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                        entityitem.motionZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                    }
                    playSound("mob.sheep.shear", 1.0F, 1.0F);
                }
                itemStack.damageItem(1, entityhuman);
                return true;
            }
            else if (getOwner().equals(entityhuman))
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
        }
        return false;
    }

    protected void playStepSound(int i, int j, int k, int l)
    {
        playSound("mob.sheep.step", 0.15F, 1.0F);
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String getLivingSound()
    {
        return !playIdleSound() ? "" : "mob.sheep.say";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String getHurtSound()
    {
        return "mob.sheep.say";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String getDeathSound()
    {
        return "mob.sheep.say";
    }
}