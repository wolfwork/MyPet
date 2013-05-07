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

public class TileEntityBeacon extends net.minecraft.tileentity.TileEntityBeacon
{
    private Beacon beaconSkill;
    private boolean primaryBuffCheck = false;

    public TileEntityBeacon(Beacon beaconSkill)
    {
        this.beaconSkill = beaconSkill;
    }

    @Override
    public void updateEntity()
    {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityhuman)
    {
        return true;
    }

    @Override
    public void setPrimaryEffect(int effectId)
    {
        if (!beaconSkill.activate(true, effectId))
        {
            beaconSkill.setTributeItem(beaconSkill.getTributeItem());
        }
        else
        {
            beaconSkill.setTributeItem(null);
            primaryBuffCheck = true;
        }
    }

    @Override
    public void setSecondaryEffect(int effectId)
    {
        if (!beaconSkill.activate(false, effectId))
        {
            beaconSkill.setTributeItem(beaconSkill.getTributeItem());
        }
        else
        {
            beaconSkill.setTributeItem(null);
        }
        if (primaryBuffCheck)
        {
            beaconSkill.setTributeItem(null);
            primaryBuffCheck = false;
        }
    }
}
