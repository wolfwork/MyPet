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

package de.Keyle.MyPet.entity.types.pigzombie;

import de.Keyle.MyPet.MyPetPlugin;
import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.EquipmentSlot;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.entity.types.MyPet.PetState;
import de.Keyle.MyPet.util.MyPetPermissions;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.network.packet.Packet5PlayerInventory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

@EntitySize(width = 0.6F, height = 0.9F)
public class EntityMyPigZombie extends EntityMyPet
{
    public EntityMyPigZombie(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/pigzombie.png";
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            super.setMyPet(myPet);
            final MyPigZombie myPigZombie = (MyPigZombie) myPet;
            final EntityMyPigZombie entityMyPigZombie = this;

            MyPetPlugin.getPlugin().getServer().getScheduler().runTaskLater(MyPetPlugin.getPlugin(), new Runnable()
            {
                public void run()
                {
                    if (myPigZombie.status == PetState.Here)
                    {
                        for (EquipmentSlot slot : EquipmentSlot.values())
                        {
                            if (myPigZombie.getEquipment(slot) != null)
                            {
                                entityMyPigZombie.setPetEquipment(slot.getSlotId(), myPigZombie.getEquipment(slot));
                            }
                        }
                    }
                }
            }, 5L);
        }
    }

    public void setPetEquipment(int slot, ItemStack itemStack)
    {
        ((WorldServer) this.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet5PlayerInventory(this.entityId, slot, itemStack));
        ((MyPigZombie) myPet).equipment.put(EquipmentSlot.getSlotById(slot), itemStack);
    }

    public ItemStack getPetEquipment(int slot)
    {
        return ((MyPigZombie) myPet).getEquipment(EquipmentSlot.getSlotById(slot));
    }

    public ItemStack[] getPetEquipment()
    {
        return ((MyPigZombie) myPet).getEquipment();
    }

    public boolean checkForEquipment(ItemStack itemstack)
    {
        int slot = getArmorPosition(itemstack);
        if (slot == 0)
        {
            if (itemstack.getItem() instanceof ItemSword)
            {
                return true;
            }
            else if (itemstack.getItem() instanceof ItemAxe)
            {
                return true;
            }
            else if (itemstack.getItem() instanceof ItemSpade)
            {
                return true;
            }
            else if (itemstack.getItem() instanceof ItemHoe)
            {
                return true;
            }
            else if (itemstack.getItem() instanceof ItemPickaxe)
            {
                return true;
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

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
                if (!MyPetPermissions.hasExtended(myPet.getOwner().getPlayer(), "MyPet.user.extended.Equip"))
                {
                    return false;
                }
                for (EquipmentSlot slot : EquipmentSlot.values())
                {
                    ItemStack itemInSlot = ((MyPigZombie) myPet).getEquipment(slot);
                    if (itemInSlot != null)
                    {
                        EntityItem entityitem = this.entityDropItem(ItemStack.copyItemStack(itemInSlot), 1.0F);
                        entityitem.motionY += (double) (this.rand.nextFloat() * 0.05F);
                        entityitem.motionX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                        entityitem.motionZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                        setPetEquipment(slot.getSlotId(), null);
                    }
                }
                return true;
            }
            else if (checkForEquipment(itemStack) && getOwner().getPlayer().isSneaking())
            {
                if (!MyPetPermissions.hasExtended(myPet.getOwner().getPlayer(), "MyPet.user.extended.Equip"))
                {
                    return false;
                }
                EquipmentSlot slot = EquipmentSlot.getSlotById(getArmorPosition(itemStack));
                ItemStack itemInSlot = ((MyPigZombie) myPet).getEquipment(slot);
                if (itemInSlot != null && !entityhuman.capabilities.isCreativeMode)
                {
                    EntityItem entityitem = this.entityDropItem(ItemStack.copyItemStack(itemInSlot), 1.0F);
                    entityitem.motionY += (double) (this.rand.nextFloat() * 0.05F);
                    entityitem.motionX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                    entityitem.motionZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                }
                ItemStack itemStackClone = ItemStack.copyItemStack(itemStack);
                itemStackClone.stackSize = 1;
                setPetEquipment(getArmorPosition(itemStack), itemStackClone);
                if (!entityhuman.capabilities.isCreativeMode)
                {
                    --itemStack.stackSize;
                }
                if (itemStack.stackSize <= 0)
                {
                    entityhuman.inventory.setInventorySlotContents(entityhuman.inventory.currentItem, null);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String getLivingSound()
    {
        return !playIdleSound() ? "" : "mob.zombiepig.zpig";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String getHurtSound()
    {
        return "mob.zombiepig.zpighurt";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String getDeathSound()
    {
        return "mob.zombiepig.zpigdeath";
    }
}