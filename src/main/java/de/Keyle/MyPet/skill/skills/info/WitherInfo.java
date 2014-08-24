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

package de.Keyle.MyPet.skill.skills.info;

import de.Keyle.MyPet.gui.skilltreecreator.skills.SkillPropertiesPanel;
import de.Keyle.MyPet.gui.skilltreecreator.skills.Wither;
import de.Keyle.MyPet.skill.skills.SkillName;
import de.Keyle.MyPet.skill.skills.SkillProperties;
import de.Keyle.MyPet.skill.skills.SkillProperties.NBTdatatypes;
import de.Keyle.MyPet.skill.skilltree.SkillTreeSkill;

@SkillName(value = "Wither", translationNode = "Name.Skill.Wither")
@SkillProperties(
        parameterNames = {"chance", "duration", "addset_chance", "addset_duration"},
        parameterTypes = {NBTdatatypes.Int, NBTdatatypes.Int, NBTdatatypes.String, NBTdatatypes.String},
        parameterDefaultValues = {"5", "3", "add", "add"})
public class WitherInfo extends SkillTreeSkill implements ISkillInfo {
    private SkillPropertiesPanel panel = null;

    protected int chance = 0;
    protected int duration = 0;

    public WitherInfo(boolean addedByInheritance) {
        super(addedByInheritance);
    }

    public SkillPropertiesPanel getGuiPanel() {
        if (panel == null) {
            panel = new Wither(this.getProperties());
        }
        return panel;
    }

    public ISkillInfo cloneSkill() {
        WitherInfo newSkill = new WitherInfo(this.isAddedByInheritance());
        newSkill.setProperties(getProperties());
        return newSkill;
    }
}