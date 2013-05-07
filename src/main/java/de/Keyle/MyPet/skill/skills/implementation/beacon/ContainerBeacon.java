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
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.bukkit.craftbukkit.v1_5_R2.inventory.CraftInventoryView;
import org.bukkit.entity.Player;

public class ContainerBeacon extends net.minecraft.inventory.ContainerBeacon
{
    private final SlotBeacon slotBeacon;
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer playerInventory;
    private TileEntityBeacon tileEntityBeacon;
    MyPetCustomBeaconInventory beaconInv;
    Beacon beaconSkill;

    public ContainerBeacon(InventoryPlayer playerInventory, MyPetCustomBeaconInventory beaconInv, TileEntityBeacon tileEntityBeacon, Beacon beaconSkill)
    {
        super(playerInventory, tileEntityBeacon);
        this.inventoryItemStacks.clear();
        this.inventorySlots.clear();
        this.beaconInv = beaconInv;
        this.beaconSkill = beaconSkill;
        this.tileEntityBeacon = tileEntityBeacon;
        this.playerInventory = playerInventory;
        addSlotToContainer(this.slotBeacon = new SlotBeacon(beaconInv, 0, 136, 110));

        for (int i = 0 ; i < 3 ; i++)
        {
            for (int j = 0 ; j < 9 ; j++)
            {
                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 36 + j * 18, 137 + i * 18));
            }
        }

        for (int i = 0 ; i < 9 ; i++)
        {
            addSlotToContainer(new Slot(playerInventory, i, 36 + i * 18, 195));
        }
    }

    public void addSlotListener(ICrafting icrafting)
    {
        super.addCraftingToCrafters(icrafting);
        icrafting.sendProgressBarUpdate(this, 0, this.beaconSkill.getLevel());
        icrafting.sendProgressBarUpdate(this, 1, this.beaconSkill.getPrimaryEffectId());
        icrafting.sendProgressBarUpdate(this, 2, this.beaconSkill.getSecondaryEffectId());
    }

    public CraftInventoryView getBukkitView()
    {
        if (this.bukkitEntity != null)
        {
            return this.bukkitEntity;
        }

        CraftMyPetInventoryBeacon craftBeaconInventory = new CraftMyPetInventoryBeacon(this.beaconInv);
        Player player = (Player) this.playerInventory.player.getBukkitEntity();

        this.bukkitEntity = new CraftInventoryView(player, craftBeaconInventory, this);
        return this.bukkitEntity;
    }

    public boolean canInteractWith(EntityPlayer entityhuman)
    {
        return true;
    }

    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int slotNumber)
    {
        ItemStack slotItemClone = null;
        Slot slot = (Slot) this.inventorySlots.get(slotNumber); // c -> List<Slot>

        if ((slot != null) && (slot.getHasStack()))
        {
            ItemStack slotItem = slot.getStack();

            slotItemClone = slotItem.copy();
            if (slotNumber == 0)
            {
                if (!mergeItemStack(slotItem, 1, 37, true))
                {
                    return null;
                }

                slot.onSlotChange(slotItem, slotItemClone);
            }
            else if ((!this.slotBeacon.getHasStack()) && (this.slotBeacon.isAllowed(slotItem)) && (slotItem.stackSize == 1))
            {
                if (!mergeItemStack(slotItem, 0, 1, false))
                {
                    return null;
                }
            }
            else if ((slotNumber >= 1) && (slotNumber < 28))
            {
                if (!mergeItemStack(slotItem, 28, 37, false))
                {
                    return null;
                }
            }
            else if ((slotNumber >= 28) && (slotNumber < 37))
            {
                if (!mergeItemStack(slotItem, 1, 28, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(slotItem, 1, 37, false))
            {
                return null;
            }

            if (slotItem.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (slotItem.stackSize == slotItemClone.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(entityhuman, slotItem);
        }

        return slotItemClone;
    }

    @Override
    public TileEntityBeacon getBeacon()
    {
        return tileEntityBeacon;
    }
}
