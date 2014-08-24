/*
 * This file is part of MyPet
 *
 * Copyright (C) 2011-2014 Keyle
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

package de.Keyle.MyPet.entity.types.ocelot;

import de.Keyle.MyPet.entity.MyPetInfo;
import de.Keyle.MyPet.entity.types.IMyPetBaby;
import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.entity.types.MyPetType;
import de.Keyle.MyPet.util.ConfigItem;
import de.Keyle.MyPet.util.player.MyPetPlayer;
import de.keyle.knbt.TagByte;
import de.keyle.knbt.TagCompound;
import de.keyle.knbt.TagInt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Ocelot.Type;

import static de.Keyle.MyPet.entity.types.MyPet.LeashFlag.Tamed;
import static org.bukkit.Material.RAW_FISH;

@MyPetInfo(food = {RAW_FISH}, leashFlags = {Tamed})
public class MyOcelot extends MyPet implements IMyPetBaby {
    public static ConfigItem GROW_UP_ITEM;

    protected boolean isBaby = false;
    protected Type catType = Type.WILD_OCELOT;

    public MyOcelot(MyPetPlayer petOwner) {
        super(petOwner);
    }

    public Type getCatType() {
        return catType;
    }

    public void setCatType(Type value) {
        if (status == PetState.Here) {
            ((EntityMyOcelot) getCraftPet().getHandle()).setCatType(value.getId());
        }
        this.catType = value;
    }

    @Override
    public TagCompound getExtendedInfo() {
        TagCompound info = super.getExtendedInfo();
        info.getCompoundData().put("CatType", new TagInt(getCatType().getId()));
        info.getCompoundData().put("Baby", new TagByte(isBaby()));
        return info;
    }

    @Override
    public void setExtendedInfo(TagCompound info) {
        if (info.getCompoundData().containsKey("CatType")) {
            setCatType(Type.getType(info.getAs("CatType", TagInt.class).getIntData()));
        }
        if (info.getCompoundData().containsKey("Baby")) {
            setBaby(info.getAs("Baby", TagByte.class).getBooleanData());
        }
    }

    @Override
    public MyPetType getPetType() {
        return MyPetType.Ocelot;
    }

    public boolean isBaby() {
        return isBaby;
    }

    public void setBaby(boolean flag) {
        if (status == PetState.Here) {
            ((EntityMyOcelot) getCraftPet().getHandle()).setBaby(flag);
        }
        this.isBaby = flag;
    }

    @Override
    public String toString() {
        return "MyOcelot{owner=" + getOwner().getName() + ", name=" + ChatColor.stripColor(petName) + ", exp=" + experience.getExp() + "/" + experience.getRequiredExp() + ", lv=" + experience.getLevel() + ", status=" + status.name() + ", skilltree=" + (skillTree != null ? skillTree.getName() : "-") + ", worldgroup=" + worldGroup + ", cattype=" + getCatType().name() + ", baby=" + isBaby() + "}";
    }
}