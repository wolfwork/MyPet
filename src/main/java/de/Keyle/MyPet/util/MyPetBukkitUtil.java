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

package de.Keyle.MyPet.util;

import de.Keyle.MyPet.entity.types.CraftMyPet;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R2.util.UnsafeList;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MyPetBukkitUtil
{
    public static String setColors(String text)
    {
        for (ChatColor color : ChatColor.values())
        {
            text = text.replace("%" + color.name().replace("_", "").toLowerCase() + "%", color.toString());
        }
        return text;
    }

    public static Material checkMaterial(int itemid, Material defaultMaterial)
    {
        if (Material.getMaterial(itemid) == null)
        {
            return defaultMaterial;
        }
        else
        {
            return Material.getMaterial(itemid);
        }
    }

    public static boolean isValidMaterial(int itemid)
    {
        return Material.getMaterial(itemid) != null;
    }

    public static void sendMessage(Player player, String Message)
    {
        if (player != null && player.isOnline())
        {
            player.sendMessage(Message);
        }
    }

    public static Boolean canSpawn(Location loc, Entity entity)
    {
        return canSpawn(loc, entity.width, entity.height, entity.width);
    }

    public static Boolean canSpawn(Location loc, float width, float height, float length)
    {
        net.minecraft.world.World mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
        float halfEntityWidth = width / 2;
        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(loc.getX() - halfEntityWidth, loc.getY() - height, loc.getZ() - halfEntityWidth, loc.getX() + halfEntityWidth, loc.getY() - height + length, loc.getZ() + halfEntityWidth);

        return getBlockBBsInBB(loc.getWorld(), bb).isEmpty() && !mcWorld.isAnyLiquid(bb);
    }

    public static List getBlockBBsInBB(World world, AxisAlignedBB axisalignedbb)
    {
        UnsafeList unsafeList = new UnsafeList();
        int minX = MathHelper.floor_double(axisalignedbb.minX);
        int maxX = MathHelper.floor_double(axisalignedbb.maxX + 1.0D);
        int minY = MathHelper.floor_double(axisalignedbb.minY);
        int maxY = MathHelper.floor_double(axisalignedbb.maxY + 1.0D);
        int minZ = MathHelper.floor_double(axisalignedbb.minZ);
        int maxZ = MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);

        for (int x = minX ; x < maxX ; x++)
        {
            for (int z = minZ ; z < maxZ ; z++)
            {
                if (world.getChunkAt(x, z).isLoaded())
                {
                    for (int y = minY - 1 ; y < maxY ; y++)
                    {
                        Block block = Block.blocksList[world.getBlockAt(x, y, z).getTypeId()];

                        if (block != null)
                        {
                            block.addCollisionBoxesToList(((CraftWorld) world).getHandle(), x, y, z, axisalignedbb, unsafeList, null);
                        }
                    }
                }
            }
        }
        return unsafeList;
    }

    public List<org.bukkit.entity.Entity> getMyPetsInLineOfSight(Player player)
    {
        List<org.bukkit.entity.Entity> entityList = new ArrayList<org.bukkit.entity.Entity>();

        for (org.bukkit.block.Block b : player.getLineOfSight(null, 100))
        {
            Location blockLoc = b.getLocation();
            double bx = blockLoc.getX();
            double by = blockLoc.getY();
            double bz = blockLoc.getZ();

            for (org.bukkit.entity.Entity e : player.getNearbyEntities(100, 100, 100))
            {
                if (e instanceof CraftMyPet)
                {
                    Location loc = e.getLocation();
                    double ex = loc.getX();
                    double ey = loc.getY();
                    double ez = loc.getZ();

                    if ((bx - 1.5 <= ex && ex <= bx + 2) && (bz - 1.5 <= ez && ez <= bz + 2) && (by - 1 <= ey && ey <= by + 2.5))
                    {
                        entityList.add(e);
                    }
                }
            }
        }
        return entityList;
    }
}