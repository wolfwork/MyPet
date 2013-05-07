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

public class MyPetAIFloat extends MyPetAIGoal
{
    private EntityMyPet entityMyPet;

    public MyPetAIFloat(EntityMyPet entityMyPet)
    {
        this.entityMyPet = entityMyPet;
        entityMyPet.getNavigator().setCanSwim(true);
    }

    @Override
    public boolean shouldStart()
    {
        return entityMyPet.worldObj.getBlockMaterial((int) entityMyPet.posX, (int) entityMyPet.posY, (int) entityMyPet.posZ).isLiquid();
    }

    @Override
    public void tick()
    {
        if (entityMyPet.getRNG().nextFloat() < 0.9D)
        {
            entityMyPet.motionY += 0.05D;
        }
    }
}
