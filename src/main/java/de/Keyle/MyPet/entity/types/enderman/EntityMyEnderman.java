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

package de.Keyle.MyPet.entity.types.enderman;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


@EntitySize(width = 0.6F, height = 2.9F)
public class EntityMyEnderman extends EntityMyPet
{
    public EntityMyEnderman(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/enderman.png";
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            super.setMyPet(myPet);

            this.setScreaming(((MyEnderman) myPet).isScreaming());
            this.setBlock(((MyEnderman) myPet).getBlockID(), ((MyEnderman) myPet).getBlockData());
        }
    }

    public int getBlockID()
    {
        return ((MyEnderman) myPet).BlockID;
    }

    public int getBlockData()
    {
        return ((MyEnderman) myPet).BlockData;
    }

    public void setBlock(int blockID, int blockData)
    {
        this.getDataWatcher().updateObject(16, (byte) (blockID & 0xFF));
        ((MyEnderman) myPet).BlockID = blockID;

        this.getDataWatcher().updateObject(17, (byte) (blockData & 0xFF));
        ((MyEnderman) myPet).BlockData = blockData;
    }

    public boolean isScreaming()
    {
        return ((MyEnderman) myPet).isScreaming;
    }

    public void setScreaming(boolean screaming)
    {
        this.getDataWatcher().updateObject(18, (byte) (screaming ? 1 : 0));
        ((MyEnderman) myPet).isScreaming = screaming;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(16, new Byte((byte) 0));  // BlockID
        this.getDataWatcher().addObject(17, new Byte((byte) 0));  // BlockData
        this.getDataWatcher().addObject(18, new Byte((byte) 0));  // Face(angry)
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
            if (itemStack.itemID == Item.shears.itemID)
            {
                if (getBlockID() != 0)
                {
                    EntityItem entityitem = this.entityDropItem(new ItemStack(getBlockID(), 1, getBlockData()), 1.0F);
                    entityitem.motionY += (double) (this.rand.nextFloat() * 0.05F);
                    entityitem.motionX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                    entityitem.motionZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);

                    setBlock(0, 0);

                    return true;
                }
            }
            else if (getBlockID() <= 0 && itemStack.itemID > 0 && itemStack.itemID < 256)
            {
                setBlock(itemStack.itemID, itemStack.getItemDamage());
                if (!entityhuman.capabilities.isCreativeMode)
                {
                    --itemStack.stackSize;
                }
                if (itemStack.stackSize <= 0)
                {
                    entityhuman.inventory.setInventorySlotContents(entityhuman.inventory.currentItem, null);
                }
            }
        }
        return false;
    }

    @Override
    protected String getLivingSound()
    {
        return !playIdleSound() ? "" : isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String getHurtSound()
    {
        return "mob.endermen.hit";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String getDeathSound()
    {
        return "mob.endermen.death";
    }
}