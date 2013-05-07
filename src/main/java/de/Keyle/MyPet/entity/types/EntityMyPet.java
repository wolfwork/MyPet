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

package de.Keyle.MyPet.entity.types;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.ai.MyPetAIGoalSelector;
import de.Keyle.MyPet.entity.ai.attack.MyPetAIMeleeAttack;
import de.Keyle.MyPet.entity.ai.attack.MyPetAIRangedAttack;
import de.Keyle.MyPet.entity.ai.movement.*;
import de.Keyle.MyPet.entity.ai.navigation.AbstractNavigation;
import de.Keyle.MyPet.entity.ai.navigation.MCNavigation;
import de.Keyle.MyPet.entity.ai.target.*;
import de.Keyle.MyPet.skill.skills.implementation.Control;
import de.Keyle.MyPet.skill.skills.implementation.Ride;
import de.Keyle.MyPet.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import java.util.List;

public abstract class EntityMyPet extends EntityCreature implements IMob
{
    public MyPetAIGoalSelector petPathfinderSelector, petTargetSelector;
    public EntityLiving goalTarget = null;
    protected float walkSpeed = 0.3F;
    protected boolean isRidden = false;
    protected boolean isMyPet = false;
    protected MyPet myPet;
    protected int idleSoundTimer = 0;
    public AbstractNavigation petNavigation;

    public EntityMyPet(World world, MyPet myPet)
    {
        super(world);

        setSize();

        setMyPet(myPet);
        myPet.craftMyPet = (CraftMyPet) this.getBukkitEntity();

        this.petPathfinderSelector = new MyPetAIGoalSelector();
        this.petTargetSelector = new MyPetAIGoalSelector();

        this.walkSpeed = MyPet.getStartSpeed(MyPetType.getMyPetTypeByEntityClass(this.getClass()).getMyPetClass());

        petNavigation = new MCNavigation(this);

        this.setPathfinder();
    }

    public boolean isMyPet()
    {
        return isMyPet;
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            this.myPet = myPet;
            isMyPet = true;

            ((LivingEntity) this.getBukkitEntity()).setMaxHealth(myPet.getMaxHealth());
            this.health = myPet.getHealth();
            this.func_94058_c("");
        }
    }

    public void setPathfinder()
    {
        petPathfinderSelector.addGoal("Float", new MyPetAIFloat(this));
        petPathfinderSelector.addGoal("Ride", new MyPetAIRide(this, this.walkSpeed));
        petPathfinderSelector.addGoal("Sprint", new MyPetAISprint(this, 0.25F));
        petPathfinderSelector.addGoal("RangedTarget", new MyPetAIRangedAttack(this, -0.1F, 35, 12.0F));
        petPathfinderSelector.addGoal("MeleeAttack", new MyPetAIMeleeAttack(this, 0.1F, 3, 20));
        petPathfinderSelector.addGoal("Control", new MyPetAIControl(myPet, 0.1F));
        petPathfinderSelector.addGoal("FollowOwner", new MyPetAIFollowOwner(this, 0F, MyPetConfiguration.MYPET_FOLLOW_START_DISTANCE, 2.0F, 17F));
        petPathfinderSelector.addGoal("LookAtPlayer", new MyPetAILookAtPlayer(this, 8.0F));
        petPathfinderSelector.addGoal("RandomLockaround", new MyPetAIRandomLookaround(this));
        petTargetSelector.addGoal("OwnerHurtByTarget", new MyPetAIOwnerHurtByTarget(this));
        petTargetSelector.addGoal("OwnerHurtTarget", new MyPetAIOwnerHurtTarget(this));
        petTargetSelector.addGoal("HurtByTarget", new MyPetAIHurtByTarget(this));
        petTargetSelector.addGoal("ControlTarget", new MyPetAIControlTarget(this, 1));
        petTargetSelector.addGoal("AggressiveTarget", new MyPetAIAggressiveTarget(this, 15));
        petTargetSelector.addGoal("FarmTarget", new MyPetAIFarmTarget(this, 15));
        petTargetSelector.addGoal("DuelTarget", new MyPetAIDuelTarget(this, 5));
    }

    public MyPet getMyPet()
    {
        return myPet;
    }

    public void setSize()
    {
        EntitySize es = this.getClass().getAnnotation(EntitySize.class);
        if (es != null)
        {
            this.setSize(es.width(), es.height());
        }
    }

    public void setSize(float extra)
    {
        EntitySize es = this.getClass().getAnnotation(EntitySize.class);
        if (es != null)
        {
            this.setSize(es.width(), es.height() + extra);
        }
    }

    public boolean hasRider()
    {
        return isRidden;
    }

    public void setRidden(boolean flag)
    {
        isRidden = flag;
    }

    public void setLocation(Location loc)
    {
        this.setLocationAndAngles(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
    }

    @Override
    //public void setCustomName(String ignored)
    public void func_94058_c(String ignored)
    {
        if (MyPetConfiguration.PET_INFO_OVERHEAD_NAME)
        {
            super.func_94058_c(MyPetUtil.cutString(MyPetConfiguration.PET_INFO_OVERHEAD_PREFIX + myPet.getPetName() + MyPetConfiguration.PET_INFO_OVERHEAD_SUFFIX, 64));
            this.func_94061_f(false);
        }
    }

    public void setCustomName(String ignored)
    {
        func_94058_c("");
    }

    @Override
    //public boolean getCustomNameVisible()
    public boolean func_94062_bN()
    {
        return MyPetConfiguration.PET_INFO_OVERHEAD_NAME;
    }

    public boolean getCustomNameVisible()
    {
        return func_94062_bN();
    }

    @Override
    //public void setCustomNameVisible(boolean ignored)
    public void func_94061_f(boolean ignored)
    {
        this.getDataWatcher().updateObject(6, Byte.valueOf((byte) (MyPetConfiguration.PET_INFO_OVERHEAD_NAME ? 1 : 0)));
    }

    public void setCustomNameVisible(boolean ignored)
    {
        func_94062_bN();
    }

    public boolean canMove()
    {
        return true;
    }

    public float getWalkSpeed()
    {
        return walkSpeed;
    }

    public boolean canEat(ItemStack itemstack)
    {
        List<Material> foodList = MyPet.getFood(myPet.getClass());
        for (Material foodItem : foodList)
        {
            if (itemstack.itemID == foodItem.getId())
            {
                return true;
            }
        }
        return false;
    }

    public boolean playIdleSound()
    {
        if (idleSoundTimer-- <= 0)
        {
            idleSoundTimer = 5;
            return true;
        }
        return false;
    }

    public MyPetPlayer getOwner()
    {
        return myPet.getOwner();
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        Entity entity = damagesource.getEntity();

        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
        {
            i = (i + 1) / 2;
        }
        return super.attackEntityFrom(damagesource, i);
    }

    /**
     * Is called when a MyPet attemps to do damge to another entity
     */
    public boolean attack(Entity entity)
    {
        int damage = isMyPet() ? myPet.getDamage() : 0;
        if (entity instanceof EntityPlayer)
        {
            Player victim = (Player) entity.getBukkitEntity();
            if (!MyPetPvP.canHurt(myPet.getOwner().getPlayer(), victim))
            {
                if (myPet.hasTarget())
                {
                    myPet.getCraftPet().getHandle().setRevengeTarget(null);
                }
                return false;
            }
        }
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
    }

    @Override
    public int getMaxHealth()
    {
        return this.maxHealth;
    }

    protected void tamedEffect(boolean tamed)
    {
        String str = tamed ? "heart" : "smoke";
        for (int i = 0 ; i < 7 ; i++)
        {
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            double d3 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(str, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d1, d2, d3);
        }
    }

    @Override
    public CraftEntity getBukkitEntity()
    {
        if (this.bukkitEntity == null)
        {
            this.bukkitEntity = new CraftMyPet(this.worldObj.getServer(), this);
        }
        return this.bukkitEntity;
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

        ItemStack itemStack = entityhuman.inventory.getCurrentItem();

        if (itemStack == null)
        {
            return false;
        }

        Player owner = this.getOwner().getPlayer();

        if (isMyPet() && entityhuman.getBukkitEntity() == owner)
        {
            if (this.hasRider())
            {
                ((CraftPlayer) owner).getHandle().mountEntity(null);
                return true;
            }
            if (myPet.getSkills().isSkillActive("Ride"))
            {
                if (itemStack.itemID == Ride.ITEM.getId() && canMove())
                {
                    if (MyPetPermissions.hasExtended(owner, "MyPet.user.extended.Ride"))
                    {
                        ((CraftPlayer) owner).getHandle().mountEntity(this);
                        return true;
                    }
                    else
                    {
                        getMyPet().sendMessageToOwner(MyPetBukkitUtil.setColors(MyPetLanguage.getString("Msg_CantUse")));
                    }
                }
            }
            if (myPet.getSkills().isSkillActive("Control"))
            {
                if (itemStack.itemID == Control.ITEM.getId())
                {
                    return true;
                }
            }
        }
        if (canEat(itemStack))
        {
            if (owner != null && !MyPetPermissions.hasExtended(owner, "MyPet.user.extended.CanFeed"))
            {
                return false;
            }
            if (this.petTargetSelector.hasGoal("DuelTarget"))
            {
                MyPetAIDuelTarget duelTarget = (MyPetAIDuelTarget) this.petTargetSelector.getGoal("DuelTarget");
                if (duelTarget.getDuelOpponent() != null)
                {
                    return true;
                }
            }
            int addHunger = MyPetConfiguration.HUNGER_SYSTEM_POINTS_PER_FEED;
            if (getHealth() < getMaxHealth())
            {
                if (!entityhuman.capabilities.isCreativeMode)
                {
                    --itemStack.stackSize;
                }
                addHunger -= Math.min(3, getMaxHealth() - getHealth()) * 2;
                this.heal(Math.min(3, getMaxHealth() - getHealth()), RegainReason.EATING);
                if (itemStack.stackSize <= 0)
                {
                    entityhuman.inventory.setInventorySlotContents(entityhuman.inventory.currentItem, null);
                }
                this.tamedEffect(true);
            }
            else if (myPet.getHungerValue() < 100)
            {
                if (!entityhuman.capabilities.isCreativeMode)
                {
                    --itemStack.stackSize;
                }
                if (itemStack.stackSize <= 0)
                {
                    entityhuman.inventory.setInventorySlotContents(entityhuman.inventory.currentItem, null);
                }
                this.tamedEffect(true);
            }
            if (addHunger > 0 && myPet.getHungerValue() < 100)
            {
                myPet.setHungerValue(myPet.getHungerValue() + addHunger);
                addHunger = 0;
            }
            if (addHunger < MyPetConfiguration.HUNGER_SYSTEM_POINTS_PER_FEED)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected abstract String getLivingSound();

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    protected abstract String getHurtSound();

    /**
     * Returns the sound that is played when the MyPet dies
     */
    protected abstract String getDeathSound();

    /**
     * Set weather the "new" AI is used
     */
    public boolean isAIEnabled()
    {
        return true;
    }


    /**
     * Entity AI tick method
     */
    @Override
    protected void updateAITasks()
    {
        entityAge += 1;

        getEntitySenses().clearSensingCache(); // sensing
        petTargetSelector.tick(); // target selector
        petPathfinderSelector.tick(); // pathfinder selector
        petNavigation.tick(); // navigation
        updateAITick(); // "mob tick"

        // controls
        getMoveHelper().onUpdateMoveHelper(); // move
        getLookHelper().onUpdateLook(); // look
        getJumpHelper().doJump(); // jump
    }
}