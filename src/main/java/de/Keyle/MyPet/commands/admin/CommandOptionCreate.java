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

package de.Keyle.MyPet.commands.admin;

import de.Keyle.MyPet.api.commands.CommandOptionTabCompleter;
import de.Keyle.MyPet.commands.CommandAdmin;
import de.Keyle.MyPet.entity.types.InactiveMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.entity.types.MyPetList;
import de.Keyle.MyPet.entity.types.MyPetType;
import de.Keyle.MyPet.util.BukkitUtil;
import de.Keyle.MyPet.util.Util;
import de.Keyle.MyPet.util.WorldGroup;
import de.Keyle.MyPet.util.locale.Locales;
import de.Keyle.MyPet.util.player.MyPetPlayer;
import de.keyle.knbt.TagByte;
import de.keyle.knbt.TagCompound;
import de.keyle.knbt.TagInt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandOptionCreate implements CommandOptionTabCompleter {
    private static List<String> petTypeList = new ArrayList<String>();
    private static Map<String, List<String>> petTypeOptionMap = new HashMap<String, List<String>>();

    static {
        List<String> petTypeOptionList = new ArrayList<String>();

        petTypeOptionList.add("fire");
        petTypeOptionMap.put("blaze", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("baby");
        petTypeOptionMap.put("chicken", petTypeOptionList);
        petTypeOptionMap.put("cow", petTypeOptionList);
        petTypeOptionMap.put("mooshroom", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("powered");
        petTypeOptionMap.put("creeper", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("block:");
        petTypeOptionMap.put("enderman", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("size:");
        petTypeOptionMap.put("magmacube", petTypeOptionList);
        petTypeOptionMap.put("slime", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("baby");
        petTypeOptionList.add("cat:");
        petTypeOptionMap.put("ocelot", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("baby");
        petTypeOptionList.add("saddle");
        petTypeOptionMap.put("pig", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("baby");
        petTypeOptionList.add("color:");
        petTypeOptionMap.put("sheep", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("wither");
        petTypeOptionMap.put("skeleton", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("baby");
        petTypeOptionList.add("profession:");
        petTypeOptionMap.put("villager", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("baby");
        petTypeOptionList.add("angry");
        petTypeOptionList.add("tamed");
        petTypeOptionList.add("collar:");
        petTypeOptionMap.put("wolf", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("baby");
        petTypeOptionList.add("villager");
        petTypeOptionMap.put("zombie", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("baby");
        petTypeOptionMap.put("pigzombie", petTypeOptionList);

        petTypeOptionList = new ArrayList<String>();
        petTypeOptionList.add("baby");
        petTypeOptionList.add("chest");
        petTypeOptionList.add("saddle");
        petTypeOptionList.add("horse:");
        petTypeOptionList.add("variant:");
        petTypeOptionMap.put("horse", petTypeOptionList);

        for (MyPetType petType : MyPetType.values()) {
            petTypeList.add(petType.getTypeName());
        }
    }

    @Override
    public boolean onCommandOption(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }

        String lang = BukkitUtil.getCommandSenderLanguage(sender);

        int forceOffset = 0;
        if (args[0].equalsIgnoreCase("-f")) {
            forceOffset = 1;
        }

        MyPetType myPetType = MyPetType.getMyPetTypeByName(args[1 + forceOffset]);
        if (myPetType != null) {
            Player owner = Bukkit.getPlayer(args[forceOffset]);
            if (owner == null || !owner.isOnline()) {
                sender.sendMessage("[" + ChatColor.AQUA + "MyPet" + ChatColor.RESET + "] " + Locales.getString("Message.No.PlayerOnline", lang));
                return true;
            }

            MyPetPlayer newOwner = MyPetPlayer.getOrCreateMyPetPlayer(owner);
            if (newOwner.hasMyPet() && forceOffset == 1) {
                MyPetList.setMyPetInactive(newOwner);
            }

            if (!newOwner.hasMyPet()) {
                InactiveMyPet inactiveMyPet = new InactiveMyPet(newOwner);
                inactiveMyPet.setPetType(myPetType);
                inactiveMyPet.setPetName(Locales.getString("Name." + inactiveMyPet.getPetType().getTypeName(), inactiveMyPet.getOwner().getLanguage()));

                TagCompound TagCompound = inactiveMyPet.getInfo();
                if (args.length > 2 + forceOffset) {
                    for (int i = 2 + forceOffset; i < args.length; i++) {
                        if (args[i].equalsIgnoreCase("baby")) {
                            TagCompound.getCompoundData().put("Baby", new TagByte(true));
                        } else if (args[i].equalsIgnoreCase("fire")) {
                            TagCompound.getCompoundData().put("Fire", new TagByte(true));
                        } else if (args[i].equalsIgnoreCase("powered")) {
                            TagCompound.getCompoundData().put("Powered", new TagByte(true));
                        } else if (args[i].equalsIgnoreCase("saddle")) {
                            TagCompound.getCompoundData().put("Saddle", new TagByte(true));
                        } else if (args[i].equalsIgnoreCase("sheared")) {
                            TagCompound.getCompoundData().put("Sheared", new TagByte(true));
                        } else if (args[i].equalsIgnoreCase("wither")) {
                            TagCompound.getCompoundData().put("Wither", new TagByte(true));
                        } else if (args[i].equalsIgnoreCase("tamed")) {
                            TagCompound.getCompoundData().put("Tamed", new TagByte(true));
                        } else if (args[i].equalsIgnoreCase("angry")) {
                            TagCompound.getCompoundData().put("Angry", new TagByte(true));
                        } else if (args[i].equalsIgnoreCase("villager")) {
                            TagCompound.getCompoundData().put("Villager", new TagByte(true));
                        } else if (args[i].equalsIgnoreCase("chest")) {
                            TagCompound.getCompoundData().put("Chest", new TagByte(true));
                        } else if (args[i].startsWith("size:")) {
                            String size = args[i].replace("size:", "");
                            if (Util.isInt(size)) {
                                TagCompound.getCompoundData().put("Size", new TagInt(Integer.parseInt(size)));
                            }
                        } else if (args[i].startsWith("horse:")) {
                            String horseTypeString = args[i].replace("horse:", "");
                            if (Util.isByte(horseTypeString)) {
                                int horseType = Integer.parseInt(horseTypeString);
                                horseType = Math.min(Math.max(0, horseType), 4);
                                TagCompound.getCompoundData().put("Type", new TagByte((byte) horseType));
                            }
                        } else if (args[i].startsWith("variant:")) {
                            String variantString = args[i].replace("variant:", "");
                            if (Util.isInt(variantString)) {
                                int variant = Integer.parseInt(variantString);
                                variant = Math.min(Math.max(0, variant), 1030);
                                TagCompound.getCompoundData().put("Variant", new TagInt(variant));
                            }
                        } else if (args[i].startsWith("cat:")) {
                            String catTypeString = args[i].replace("cat:", "");
                            if (Util.isInt(catTypeString)) {
                                int catType = Integer.parseInt(catTypeString);
                                catType = Math.min(Math.max(0, catType), 3);
                                TagCompound.getCompoundData().put("CatType", new TagInt(catType));
                            }
                        } else if (args[i].startsWith("profession:")) {
                            String professionString = args[i].replace("profession:", "");
                            if (Util.isInt(professionString)) {
                                int profession = Integer.parseInt(professionString);
                                profession = Math.min(Math.max(0, profession), 5);
                                TagCompound.getCompoundData().put("Profession", new TagInt(profession));
                            }
                        } else if (args[i].startsWith("color:")) {
                            String colorString = args[i].replace("color:", "");
                            if (Util.isByte(colorString)) {
                                byte color = Byte.parseByte(colorString);
                                color = color > 15 ? 15 : color < 0 ? 0 : color;
                                TagCompound.getCompoundData().put("Color", new TagByte(color));
                            }
                        } else if (args[i].startsWith("collar:")) {
                            String colorString = args[i].replace("collar:", "");
                            if (Util.isByte(colorString)) {
                                byte color = Byte.parseByte(colorString);
                                color = color > 15 ? 15 : color < 0 ? 0 : color;
                                TagCompound.getCompoundData().put("CollarColor", new TagByte(color));
                            }
                        } else if (args[i].startsWith("block:")) {
                            String blocks = args[i].replace("block:", "");
                            String[] blockInfo = blocks.split(":");
                            if (blockInfo.length >= 1 && Util.isInt(blockInfo[0]) && BukkitUtil.isValidMaterial(Integer.parseInt(blockInfo[0]))) {
                                TagCompound.getCompoundData().put("BlockID", new TagInt(Integer.parseInt(blockInfo[0])));
                            }
                            if (blockInfo.length >= 2 && Util.isInt(blockInfo[1])) {
                                int blockData = Integer.parseInt(blockInfo[1]);
                                blockData = Math.min(Math.max(0, blockData), 15);
                                TagCompound.getCompoundData().put("BlockData", new TagInt(blockData));
                            }
                        } else {
                            sender.sendMessage("[" + ChatColor.AQUA + "MyPet" + ChatColor.RESET + "] \"" + ChatColor.RED + args[i] + "\" is not a valid option!");
                        }
                    }
                }

                WorldGroup wg = WorldGroup.getGroupByWorld(owner.getWorld().getName());

                inactiveMyPet.setWorldGroup(wg.getName());
                inactiveMyPet.getOwner().setMyPetForWorldGroup(wg.getName(), inactiveMyPet.getUUID());

                MyPetList.addInactiveMyPet(inactiveMyPet);
                MyPet myPet = MyPetList.setMyPetActive(inactiveMyPet);
                if (myPet != null) {
                    myPet.createPet();
                } else {
                    sender.sendMessage("[" + ChatColor.AQUA + "MyPet" + ChatColor.RESET + "] Can't create MyPet for " + newOwner.getName() + ". Is this player online?");
                }
            } else {
                sender.sendMessage("[" + ChatColor.AQUA + "MyPet" + ChatColor.RESET + "] " + newOwner.getName() + " has already an active MyPet!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        int forceOffset = 0;
        if (strings.length >= 2 && strings[1].equalsIgnoreCase("-f")) {
            forceOffset = 1;
        }
        if (strings.length == 2 + forceOffset) {
            return null;
        }
        if (strings.length == 3 + forceOffset) {
            return petTypeList;
        }
        if (strings.length >= 4 + forceOffset) {
            if (petTypeOptionMap.containsKey(strings[2 + forceOffset].toLowerCase())) {
                return petTypeOptionMap.get(strings[2 + forceOffset].toLowerCase());
            }
        }
        return CommandAdmin.emptyList;
    }
}