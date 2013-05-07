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

package de.Keyle.MyPet.skill.skills.implementation.beacon;

import de.Keyle.MyPet.skill.skills.implementation.Beacon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;

public class MyPetCustomBeaconInventory implements IInventory
{
    public List<HumanEntity> transaction = new ArrayList<HumanEntity>();
    private int maxStack = 64;
    private ItemStack tributeItem;
    Beacon beaconSkill;

    public MyPetCustomBeaconInventory(Beacon beaconSkill)
    {
        this.beaconSkill = beaconSkill;
    }

    public ItemStack[] getContents()
    {
        return null;
    }

    public void onOpen(CraftHumanEntity who)
    {
        this.transaction.add(who);
    }

    public void onClose(CraftHumanEntity who)
    {
        this.transaction.remove(who);
    }

    public List<HumanEntity> getViewers()
    {
        return this.transaction;
    }

    public InventoryHolder getOwner()
    {
        return null;
    }

    public int getSizeInventory()
    {
        return 1;
    }

    public ItemStack getStackInSlot(int slot)
    {
        return slot == 0 ? this.tributeItem : null;
    }

    public ItemStack decrStackSize(int slot, int amount)
    {
        if (slot == 0 && this.tributeItem != null)
        {
            if (amount >= this.tributeItem.stackSize)
            {
                ItemStack itemstack = this.tributeItem;

                this.tributeItem = null;
                return itemstack;
            }
            this.tributeItem.stackSize -= amount;
            return new ItemStack(this.tributeItem.itemID, amount, this.tributeItem.getItemDamage());
        }
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (i == 0 && this.tributeItem != null)
        {
            ItemStack itemstack = this.tributeItem;

            beaconSkill.tributeItem = null;
            this.tributeItem = null;
            return itemstack;
        }
        return null;
    }

    public void setInventorySlotContents(int i, ItemStack itemStack)
    {
        if (i == 0)
        {
            if (itemStack != null)
            {
                beaconSkill.tributeItem = ItemStack.copyItemStack(itemStack);
            }
            else
            {
                beaconSkill.tributeItem = null;
            }
            this.tributeItem = itemStack;
        }
    }

    public String getInvName()
    {
        return "inventory.mypet.beacon";
    }

    public int getInventoryStackLimit()
    {
        return this.maxStack;
    }

    public void setMaxStackSize(int size)
    {
        this.maxStack = size;
    }

    public void onInventoryChanged()
    {
    }

    public void openChest()
    {
    }

    public void closeChest()
    {
    }

    public boolean isUseableByPlayer(EntityPlayer entityHuman)
    {
        return true;
    }

    public boolean isStackValidForSlot(int i, ItemStack itemStack)
    {
        return true;
    }

    public boolean isInvNameLocalized()
    {
        return true;
    }
}