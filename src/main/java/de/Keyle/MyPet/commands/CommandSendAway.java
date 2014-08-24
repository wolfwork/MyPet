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

package de.Keyle.MyPet.commands;

import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.entity.types.MyPet.PetState;
import de.Keyle.MyPet.util.BukkitUtil;
import de.Keyle.MyPet.util.Util;
import de.Keyle.MyPet.util.locale.Locales;
import de.Keyle.MyPet.util.player.MyPetPlayer;
import de.Keyle.MyPet.util.support.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSendAway implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && !(sender instanceof Player)) {
            sender.sendMessage("You can't use this command from server console!");
            return true;
        }

        String playerName = sender.getName();
        String lang = "en_en";
        if (args.length > 0) {
            if (sender instanceof Player) {
                if (Permissions.has((Player) sender, "MyPet.admin", false)) {
                    playerName = args[0];
                    lang = BukkitUtil.getPlayerLanguage((Player) sender);
                }
            } else {
                playerName = args[0];
            }
        }
        if (!MyPetPlayer.isMyPetPlayer(playerName)) {
            if (args.length == 0) {
                sender.sendMessage(Locales.getString("Message.No.HasPet", (Player) sender));
            } else {
                sender.sendMessage(Util.formatText(Locales.getString("Message.No.UserHavePet", lang), args[0]));
            }
            return true;
        }
        MyPetPlayer petOwner = MyPetPlayer.getMyPetPlayer(playerName);
        if (petOwner != null && !petOwner.isOnline()) {
            sender.sendMessage(Locales.getString("Message.No.PlayerOnline", lang));
            return true;
        }
        if (petOwner != null && petOwner.hasMyPet()) {
            MyPet myPet = petOwner.getMyPet();
            if (myPet.getStatus() == PetState.Here) {
                myPet.removePet(false);
                sender.sendMessage(Util.formatText(Locales.getString("Message.Command.SendAway.Success", petOwner), myPet.getPetName()));
            } else if (myPet.getStatus() == PetState.Despawned) {
                sender.sendMessage(Util.formatText(Locales.getString("Message.Command.SendAway.AlreadyAway", petOwner), myPet.getPetName()));
            } else if (myPet.getStatus() == PetState.Dead) {
                sender.sendMessage(Util.formatText(Locales.getString("Message.Call.Dead", petOwner), myPet.getPetName(), myPet.getRespawnTime()));
            }
        } else {
            if (args.length == 0) {
                sender.sendMessage(Locales.getString("Message.No.HasPet", lang));
            } else {
                sender.sendMessage(Util.formatText(Locales.getString("Message.No.UserHavePet", lang), args[0]));
            }
        }
        return true;
    }
}