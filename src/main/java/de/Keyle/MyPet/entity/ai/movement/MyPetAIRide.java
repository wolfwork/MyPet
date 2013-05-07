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

package de.Keyle.MyPet.entity.ai.movement;

import de.Keyle.MyPet.entity.ai.MyPetAIGoal;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.skill.skills.implementation.Ride;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;

public class MyPetAIRide extends MyPetAIGoal
{
    private final EntityMyPet petEntity;
    private final float startSpeed;
    private MyPet myPet;
    private float currentSpeed = 0.0F;
    private boolean stopRiding = true;

    public MyPetAIRide(EntityMyPet entityMyPet, float startSpeed)
    {
        this.petEntity = entityMyPet;
        this.startSpeed = startSpeed;
        myPet = petEntity.getMyPet();
    }

    @Override
    public boolean shouldStart()
    {
        if (!myPet.getSkills().isSkillActive("Ride"))
        {
            return false;
        }
        else if (this.petEntity.isDead)
        {
            return false;
        }
        else if (!this.petEntity.hasRider())
        {
            return false;
        }
        else if (!petEntity.canMove())
        {
            return false;
        }
        else if (!(petEntity.riddenByEntity instanceof EntityPlayer))
        {
            return false;
        }
        return true;
    }

    @Override
    public void start()
    {
        this.currentSpeed = 0.0F;
        this.petEntity.setRidden(true);
        petEntity.setSize(1);
    }

    @Override
    public void finish()
    {
        this.currentSpeed = 0.0F;
        this.petEntity.setRidden(false);
        petEntity.setSize();
    }

    @Override
    public void tick()
    {
        EntityPlayer petRider = (EntityPlayer) this.petEntity.riddenByEntity;

        if (petRider.isSneaking() && this.petEntity.onGround)
        {
            this.petEntity.motionY += 0.5;
        }
        if (stopRiding)
        {
            return;
        }

        float totalSpeed = this.startSpeed + (((Ride) myPet.getSkills().getSkill("Ride")).getSpeed());

        float rotationDiff = MathHelper.wrapAngleTo180_float(petRider.rotationYaw - this.petEntity.rotationYaw) * 0.5F;
        if (rotationDiff > 5.0F)
        {
            rotationDiff = 5.0F;
        }
        if (rotationDiff < -5.0F)
        {
            rotationDiff = -5.0F;
        }

        this.petEntity.rotationYaw = MathHelper.wrapAngleTo180_float(this.petEntity.rotationYaw + rotationDiff);
        if (this.currentSpeed < totalSpeed)
        {
            this.currentSpeed += (totalSpeed - this.currentSpeed) * 0.01F;
        }
        if (this.currentSpeed > totalSpeed)
        {
            this.currentSpeed = totalSpeed;
        }

        int x = MathHelper.floor_double(this.petEntity.posX);
        int y = MathHelper.floor_double(this.petEntity.posY);
        int z = MathHelper.floor_double(this.petEntity.posZ);

        // Calculation of new Pathpoint
        float f3 = 0.91F;
        if (this.petEntity.onGround)
        {
            f3 = 0.5460001F;
            int belowEntityBlockID = this.petEntity.worldObj.getBlockId(MathHelper.floor_float(x), MathHelper.floor_float(y) - 1, MathHelper.floor_float(z));
            if (belowEntityBlockID > 0)
            {
                f3 = Block.blocksList[belowEntityBlockID].slipperiness * 0.91F;
            }
        }
        float f4 = 0.1627714F / (f3 * f3 * f3);
        float f5 = MathHelper.sin(this.petEntity.rotationYaw * 3.141593F / 180.0F);
        float f6 = MathHelper.cos(this.petEntity.rotationYaw * 3.141593F / 180.0F);
        float f7 = this.petEntity.getAIMoveSpeed() * f4;
        float f8 = Math.max(this.currentSpeed, 1.0F);
        f8 = f7 / f8;
        float f9 = this.currentSpeed * f8;
        float f10 = -(f9 * f5);
        float f11 = f9 * f6;

        if (MathHelper.abs(f10) > MathHelper.abs(f11))
        {
            if (f10 < 0.0F)
            {
                f10 -= this.petEntity.width / 2.0F;
            }
            if (f10 > 0.0F)
            {
                f10 += this.petEntity.width / 2.0F;
            }
            f11 = 0.0F;
        }
        else
        {
            f10 = 0.0F;
            if (f11 < 0.0F)
            {
                f11 -= this.petEntity.width / 2.0F;
            }
            if (f11 > 0.0F)
            {
                f11 += this.petEntity.width / 2.0F;
            }
        }

        int n = MathHelper.floor_double(this.petEntity.posX + f10);
        int i1 = MathHelper.floor_double(this.petEntity.posZ + f11);

        PathPoint localPathPoint = new PathPoint(MathHelper.floor_float(this.petEntity.width + 1.0F), MathHelper.floor_float(this.petEntity.height + petRider.height + 1.0F), MathHelper.floor_float(this.petEntity.width + 1.0F));

        if ((x != n) || (z != i1))
        {
            int blockAtEntityPos = this.petEntity.worldObj.getBlockId(x, y, z);
            int blockbelowEntityPos = this.petEntity.worldObj.getBlockId(x, y - 1, z);
            boolean isStep = checkForStep(blockAtEntityPos) || ((Block.blocksList[blockAtEntityPos] == null) && checkForStep(blockbelowEntityPos));

            if (!isStep && PathFinder.func_82565_a(this.petEntity, n, y, i1, localPathPoint, false, false, true) == 0 && PathFinder.func_82565_a(this.petEntity, x, y + 1, z, localPathPoint, false, false, true) == 1 && PathFinder.func_82565_a(this.petEntity, n, y + 1, i1, localPathPoint, false, false, true) == 1)
            {
                this.petEntity.getJumpHelper().doJump();
            }
        }

        this.petEntity.moveEntityWithHeading(0.0F, this.currentSpeed);
    }

    private boolean checkForStep(int blockId)
    {
        return Block.blocksList[blockId] != null && (Block.blocksList[blockId].getRenderType() == 10 || Block.blocksList[blockId] instanceof BlockHalfSlab);
    }

    public void stopRiding(boolean flag)
    {
        this.currentSpeed = 0.0F;
        this.stopRiding = flag;
    }

    public void toggleRiding()
    {
        if (this.petEntity.riddenByEntity != null)
        {
            this.currentSpeed = 0.0F;
            this.stopRiding = !this.stopRiding;
        }
    }

    public boolean canRide()
    {
        return !this.stopRiding;
    }
}