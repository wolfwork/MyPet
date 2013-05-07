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

package de.Keyle.MyPet.entity.types.wolf;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.ai.movement.MyPetAISit;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.block.BlockCloth;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.bukkit.DyeColor;

@EntitySize(width = 0.6F, height = 0.8F)
public class EntityMyWolf extends EntityMyPet
{
    public static org.bukkit.Material GROW_UP_ITEM = org.bukkit.Material.POTION;

    protected boolean shaking;
    protected boolean isWet;
    protected float shakeCounter;

    private MyPetAISit sitPathfinder;

    public EntityMyWolf(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/wolf.png";
    }

    public void setPathfinder()
    {
        super.setPathfinder();
        petPathfinderSelector.addGoal("Sit", 2, sitPathfinder);
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            this.sitPathfinder = new MyPetAISit(this);

            super.setMyPet(myPet);

            this.setSitting(((MyWolf) myPet).isSitting());
            this.setTamed(((MyWolf) myPet).isTamed());
            this.setCollarColor(((MyWolf) myPet).getCollarColor());
        }
    }

    public void setHealth(int i)
    {
        super.setEntityHealth(i);
        this.updateAITick();
    }

    public boolean canMove()
    {
        return !isSitting();
    }

    public void setSitting(boolean sitting)
    {
        this.sitPathfinder.setSitting(sitting);
    }

    public boolean isSitting()
    {
        return ((MyWolf) myPet).isSitting;
    }

    public void applySitting(boolean sitting)
    {
        int i = this.getDataWatcher().getWatchableObjectByte(16);
        if (sitting)
        {
            this.getDataWatcher().updateObject(16, (byte) (i | 0x1));
        }
        else
        {
            this.getDataWatcher().updateObject(16, (byte) (i & 0xFFFFFFFE));
        }
        ((MyWolf) myPet).isSitting = sitting;
    }

    public boolean isTamed()
    {
        return ((MyWolf) myPet).isTamed;
    }

    public void setTamed(boolean flag)
    {
        int i = this.getDataWatcher().getWatchableObjectByte(16);
        if (flag)
        {
            this.getDataWatcher().updateObject(16, (byte) (i | 0x4));
        }
        else
        {
            this.getDataWatcher().updateObject(16, (byte) (i & 0xFFFFFFFB));
        }
        ((MyWolf) myPet).isTamed = flag;
    }

    public boolean isAngry()
    {
        return ((MyWolf) myPet).isAngry;
    }

    public void setAngry(boolean flag)
    {
        byte b0 = this.getDataWatcher().getWatchableObjectByte(16);
        if (flag)
        {
            this.getDataWatcher().updateObject(16, (byte) (b0 | 0x2));
        }
        else
        {
            this.getDataWatcher().updateObject(16, (byte) (b0 & 0xFFFFFFFD));
        }
        ((MyWolf) myPet).isAngry = flag;
    }

    public boolean isBaby()
    {
        return ((MyWolf) myPet).isBaby;
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
        ((MyWolf) myPet).isBaby = flag;
    }

    public DyeColor getCollarColor()
    {
        return ((MyWolf) myPet).collarColor;
    }

    public void setCollarColor(DyeColor color)
    {
        setCollarColor(color.getWoolData());
    }

    public void setCollarColor(byte color)
    {
        this.getDataWatcher().updateObject(20, color);
        ((MyWolf) myPet).collarColor = DyeColor.getByWoolData(color);
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(16, new Byte((byte) 0));               // tamed/angry/sitting
        this.getDataWatcher().addObject(17, "");                   // wolf owner name
        this.getDataWatcher().addObject(18, new Integer(this.getHealth()));    // tail height
        this.getDataWatcher().addObject(12, new Integer(0));                   // age
        this.getDataWatcher().addObject(19, new Byte((byte) 0));
        this.getDataWatcher().addObject(20, new Byte((byte) BlockCloth.getBlockFromDye(1))); // collar color
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

        if (itemStack != null)
        {
            if (itemStack.itemID == 351 && itemStack.getItemDamage() != ((MyWolf) myPet).getCollarColor().getDyeData())
            {
                if (itemStack.getItemDamage() <= 15)
                {
                    setCollarColor(DyeColor.getByDyeData((byte) itemStack.getItemDamage()));
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
        if (this.myPet.getOwner().equals(entityhuman) && !this.worldObj.isRemote)
        {
            this.sitPathfinder.toogleSitting();
            return true;
        }
        return false;
    }

    @Override
    protected void playStepSound(int i, int j, int k, int l)
    {
        playSound("mob.wolf.step", 0.15F, 1.0F);
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String getLivingSound()
    {
        return !playIdleSound() ? "" : (this.rand.nextInt(5) == 0 ? (getHealth() * 100 / getMaxHealth() <= 25 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String getHurtSound()
    {
        return "mob.wolf.hurt";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String getDeathSound()
    {
        return "mob.wolf.death";
    }

    @Override
    protected void updateAITick()
    {
        this.getDataWatcher().updateObject(18, (int) (25. * getHealth() / getMaxHealth())); // update tail height
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if ((!this.worldObj.isRemote) && (this.isWet) && (!this.shaking) && (!hasPath()) && (this.onGround))
        {
            this.shaking = true;
            this.shakeCounter = 0.0F;
            this.worldObj.setEntityState(this, (byte) 8);
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (isInWater())
        {
            this.isWet = true;
            this.shaking = false;
            this.shakeCounter = 0.0F;
        }
        else if ((this.isWet || this.shaking) && this.shaking)
        {
            if (this.shakeCounter == 0.0F)
            {
                playSound("mob.wolf.shake", getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.shakeCounter += 0.05F;
            if (this.shakeCounter - 0.05F >= 2.0F)
            {
                this.isWet = false;
                this.shaking = false;
                this.shakeCounter = 0.0F;
            }

            if (this.shakeCounter > 0.4F)
            {
                float locY = (float) this.boundingBox.minY;
                int i = (int) (MathHelper.sin((this.shakeCounter - 0.4F) * 3.141593F) * 7.0F);

                for (int j = 0 ; j < i ; j++)
                {
                    float offsetX = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float offsetZ = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;

                    this.worldObj.spawnParticle("splash", this.posX + offsetX, locY + 0.8F, this.posZ + offsetZ, this.motionX, this.motionY, this.motionZ);
                }
            }
        }
    }
}