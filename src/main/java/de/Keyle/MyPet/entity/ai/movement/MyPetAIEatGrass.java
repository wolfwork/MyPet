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
import de.Keyle.MyPet.entity.types.sheep.EntityMySheep;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R2.event.CraftEventFactory;

public class MyPetAIEatGrass extends MyPetAIGoal
{
    private EntityMySheep entityMySheep;
    private World world;
    private double chanceToEat;
    int eatTicks = 0;

    public MyPetAIEatGrass(EntityMySheep entityMySheep, double chanceToEat)
    {
        this.entityMySheep = entityMySheep;
        this.chanceToEat = chanceToEat;
        this.world = entityMySheep.worldObj;
    }

    @Override
    public boolean shouldStart()
    {
        if (!EntityMySheep.CAN_REGROW_WOOL)
        {
            return false;
        }
        else if (!this.entityMySheep.isSheared())
        {
            return false;
        }
        else if (entityMySheep.getRNG().nextDouble() > chanceToEat / 100.)
        {
            return false;
        }
        else if (this.entityMySheep.getAITarget() != null && !this.entityMySheep.getAITarget().isDead)
        {
            return false;
        }
        int blockLocX = MathHelper.floor_double(this.entityMySheep.posX);
        int blockLocY = MathHelper.floor_double(this.entityMySheep.posY);
        int blockLocZ = MathHelper.floor_double(this.entityMySheep.posZ);

        return this.world.getBlockId(blockLocX, blockLocY, blockLocZ) == Block.tallGrass.blockID || this.world.getBlockId(blockLocX, blockLocY - 1, blockLocZ) == Block.grass.blockID;
    }

    @Override
    public boolean shouldFinish()
    {
        return this.eatTicks <= 0;
    }

    @Override
    public void start()
    {
        this.eatTicks = 40;
        this.world.setEntityState(this.entityMySheep, (byte) 10);
        this.entityMySheep.petNavigation.stop();
    }

    @Override
    public void finish()
    {
        this.eatTicks = 0;
    }

    @Override
    public void tick()
    {
        this.eatTicks--;
        if (this.eatTicks == 4)
        {
            int blockLocX = MathHelper.floor_double(this.entityMySheep.posX);
            int blockLocY = MathHelper.floor_double(this.entityMySheep.posY);
            int blockLocZ = MathHelper.floor_double(this.entityMySheep.posZ);

            if (this.world.getBlockId(blockLocX, blockLocY, blockLocZ) == Block.tallGrass.blockID)
            {
                if (!CraftEventFactory.callEntityChangeBlockEvent(this.entityMySheep.getBukkitEntity(), this.entityMySheep.worldObj.getWorld().getBlockAt(blockLocX, blockLocY, blockLocZ), Material.AIR).isCancelled())
                {
                    this.world.playAuxSFX(2001, blockLocX, blockLocY, blockLocZ, Block.tallGrass.blockID + 4096);
                    this.world.destroyBlock(blockLocX, blockLocY, blockLocZ, false);
                    this.entityMySheep.setSheared(false);
                }
            }
            else if (this.world.getBlockId(blockLocX, blockLocY - 1, blockLocZ) == Block.grass.blockID)
            {
                if (!CraftEventFactory.callEntityChangeBlockEvent(this.entityMySheep.getBukkitEntity(), this.entityMySheep.worldObj.getWorld().getBlockAt(blockLocX, blockLocY - 1, blockLocZ), Material.DIRT).isCancelled())
                {
                    this.world.playAuxSFX(2001, blockLocX, blockLocY - 1, blockLocZ, Block.grass.blockID);
                    this.world.setBlock(blockLocX, blockLocY - 1, blockLocZ, Block.dirt.blockID, 0, 2);
                    this.entityMySheep.setSheared(false);
                }
            }
        }
    }
}
