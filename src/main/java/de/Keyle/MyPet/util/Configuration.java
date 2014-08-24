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

package de.Keyle.MyPet.util;

import de.Keyle.MyPet.MyPetPlugin;
import de.Keyle.MyPet.commands.CommandInfo.PetInfoDisplay;
import de.Keyle.MyPet.entity.MyPetInfo;
import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.entity.types.MyPet.LeashFlag;
import de.Keyle.MyPet.entity.types.MyPetType;
import de.Keyle.MyPet.entity.types.chicken.MyChicken;
import de.Keyle.MyPet.entity.types.cow.MyCow;
import de.Keyle.MyPet.entity.types.horse.MyHorse;
import de.Keyle.MyPet.entity.types.irongolem.MyIronGolem;
import de.Keyle.MyPet.entity.types.mooshroom.MyMooshroom;
import de.Keyle.MyPet.entity.types.ocelot.MyOcelot;
import de.Keyle.MyPet.entity.types.pig.MyPig;
import de.Keyle.MyPet.entity.types.pigzombie.MyPigZombie;
import de.Keyle.MyPet.entity.types.sheep.MySheep;
import de.Keyle.MyPet.entity.types.snowman.MySnowman;
import de.Keyle.MyPet.entity.types.villager.MyVillager;
import de.Keyle.MyPet.entity.types.wolf.MyWolf;
import de.Keyle.MyPet.entity.types.zombie.MyZombie;
import de.Keyle.MyPet.skill.Experience;
import de.Keyle.MyPet.skill.MonsterExperience;
import de.Keyle.MyPet.skill.skills.implementation.*;
import de.Keyle.MyPet.util.logger.MyPetLogger;
import de.Keyle.MyPet.util.support.Economy;
import de.Keyle.MyPet.util.support.Permissions;
import de.Keyle.MyPet.util.support.PvPChecker;
import de.Keyle.MyPet.util.support.arenas.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

public class Configuration {
    public static FileConfiguration config;

    public static String PET_INFO_OVERHEAD_PREFIX = "<aqua>";
    public static String PET_INFO_OVERHEAD_SUFFIX = "";
    public static int RESPAWN_TIME_FACTOR = 5;
    public static int RESPAWN_TIME_PLAYER_FACTOR = 5;
    public static int RESPAWN_TIME_FIXED = 0;
    public static int RESPAWN_TIME_PLAYER_FIXED = 0;
    public static int AUTOSAVE_TIME = 60;
    public static int HUNGER_SYSTEM_TIME = 60;
    public static int HUNGER_SYSTEM_POINTS_PER_FEED = 6;
    public static int SKILLTREE_SWITCH_PENALTY_PERCENT = 5;
    public static int LEVELUP_FIREWORK_COLOR = 0x00FF00;
    public static int MAX_PET_NAME_LENGTH = 64;
    public static double MYPET_FOLLOW_START_DISTANCE = 7.0F;
    public static double SKILLTREE_SWITCH_PENALTY_FIXED = 0.0;
    public static double RESPAWN_COSTS_FACTOR = 1.0;
    public static double RESPAWN_COSTS_FIXED = 0.0;
    public static boolean ALWAYS_SHOW_LEASH_FOR_OWNER = false;
    public static boolean CONSUME_LEASH_ITEM = false;
    public static boolean SKILLTREE_SWITCH_PENALTY_ADMIN = false;
    public static boolean AUTOMATIC_SKILLTREE_ASSIGNMENT = false;
    public static boolean CHOOSE_SKILLTREE_ONLY_ONCE = true;
    public static boolean PREVENT_LEVELLING_WITHOUT_SKILLTREE = true;
    public static boolean OWNER_CAN_ATTACK_PET = false;
    public static boolean DISABLE_PET_VS_PLAYER = false;
    public static boolean USE_HUNGER_SYSTEM = true;
    public static boolean INHERIT_ALREADY_INHERITED_SKILLS = false;
    public static boolean REMOVE_PETS_AFTER_RELEASE = false;
    public static boolean PET_INFO_OVERHEAD_NAME = true;
    public static boolean STORE_PETS_ON_PLAYER_QUIT = true;
    public static boolean STORE_PETS_ON_PET_LEASH = true;
    public static boolean STORE_PETS_ON_PET_RELEASE = true;
    public static boolean RELEASE_PETS_ON_DEATH = false;
    public static boolean ADD_ZOMBIE_TARGET_GOAL = true;
    public static boolean RETAIN_EQUIPMENT_ON_TAME = true;
    public static boolean INVISIBLE_LIKE_OWNER = true;

    public static void setDefault() {
        config.addDefault("MyPet.Leash.Consume", CONSUME_LEASH_ITEM);
        config.addDefault("MyPet.Leash.ShowAlwaysForOwner", ALWAYS_SHOW_LEASH_FOR_OWNER);
        config.addDefault("MyPet.OwnerCanAttackPet", OWNER_CAN_ATTACK_PET);
        config.addDefault("MyPet.DisablePetVersusPlayer", DISABLE_PET_VS_PLAYER);
        config.addDefault("MyPet.RemovePetsAfterRelease", REMOVE_PETS_AFTER_RELEASE);
        config.addDefault("MyPet.FollowStartDistance", MYPET_FOLLOW_START_DISTANCE);
        config.addDefault("MyPet.ReleasePetsOnDeath", RELEASE_PETS_ON_DEATH);
        config.addDefault("MyPet.ZombieVsMyPet", ADD_ZOMBIE_TARGET_GOAL);
        config.addDefault("MyPet.MaxPetNameLength", MAX_PET_NAME_LENGTH);
        config.addDefault("MyPet.RetainEquipmentOnTame", RETAIN_EQUIPMENT_ON_TAME);
        config.addDefault("MyPet.Make-Pet-Invisible-When-Owner-Is-Invisible", INVISIBLE_LIKE_OWNER);

        config.addDefault("MyPet.Backup.Active", Backup.MAKE_BACKUPS);
        config.addDefault("MyPet.Backup.SaveInterval", Backup.SAVE_INTERVAL);
        config.addDefault("MyPet.Backup.DateFormat", Backup.DATE_FORMAT);

        config.addDefault("MyPet.PetStorage.AutoSaveTime", AUTOSAVE_TIME);
        config.addDefault("MyPet.PetStorage.OnPlayerQuit", STORE_PETS_ON_PLAYER_QUIT);
        config.addDefault("MyPet.PetStorage.OnPetLeash", STORE_PETS_ON_PET_LEASH);
        config.addDefault("MyPet.PetStorage.OnPetRelease", STORE_PETS_ON_PET_RELEASE);

        config.addDefault("MyPet.Respawn.Time.Default.Factor", RESPAWN_TIME_FACTOR);
        config.addDefault("MyPet.Respawn.Time.Player.Factor", RESPAWN_TIME_PLAYER_FACTOR);
        config.addDefault("MyPet.Respawn.Time.Default.Fixed", RESPAWN_TIME_FIXED);
        config.addDefault("MyPet.Respawn.Time.Player.Fixed", RESPAWN_TIME_PLAYER_FIXED);
        config.addDefault("MyPet.Respawn.EconomyCost.Fixed", RESPAWN_COSTS_FIXED);
        config.addDefault("MyPet.Respawn.EconomyCost.Factor", RESPAWN_COSTS_FACTOR);

        config.addDefault("MyPet.Permissions.Enabled", Permissions.ENABLED);
        config.addDefault("MyPet.Permissions.UseExtendedPermissions", Permissions.USE_EXTENDET_PERMISSIONS);

        config.addDefault("MyPet.LevelSystem.CalculationMode", Experience.CALCULATION_MODE);
        config.addDefault("MyPet.LevelSystem.Firework.Enabled", Experience.FIREWORK_ON_LEVELUP);
        config.addDefault("MyPet.LevelSystem.Firework.Color", "#00FF00");

        config.addDefault("MyPet.HungerSystem.Active", USE_HUNGER_SYSTEM);
        config.addDefault("MyPet.HungerSystem.Time", HUNGER_SYSTEM_TIME);
        config.addDefault("MyPet.HungerSystem.HungerPointsPerFeed", HUNGER_SYSTEM_POINTS_PER_FEED);

        config.addDefault("MyPet.Skilltree.AutomaticAssignment", AUTOMATIC_SKILLTREE_ASSIGNMENT);
        config.addDefault("MyPet.Skilltree.InheritAlreadyInheritedSkills", INHERIT_ALREADY_INHERITED_SKILLS);
        config.addDefault("MyPet.Skilltree.ChooseOnce", CHOOSE_SKILLTREE_ONLY_ONCE);
        config.addDefault("MyPet.Skilltree.PreventLevellingWithout", PREVENT_LEVELLING_WITHOUT_SKILLTREE);
        config.addDefault("MyPet.Skilltree.SwitchPenaltyFixed", SKILLTREE_SWITCH_PENALTY_FIXED);
        config.addDefault("MyPet.Skilltree.SwitchPenaltyPercent", SKILLTREE_SWITCH_PENALTY_PERCENT);
        config.addDefault("MyPet.Skilltree.SwitchPenaltyAdmin", SKILLTREE_SWITCH_PENALTY_ADMIN);

        config.addDefault("MyPet.Support.PlayerDamageEntityEvent", false);
        config.addDefault("MyPet.Support.Towny", true);
        config.addDefault("MyPet.Support.Heroes", true);
        config.addDefault("MyPet.Support.Factions", true);
        config.addDefault("MyPet.Support.WorldGuard", true);
        config.addDefault("MyPet.Support.Citizens", true);
        config.addDefault("MyPet.Support.mcMMO", true);
        config.addDefault("MyPet.Support.Regios", true);
        config.addDefault("MyPet.Support.MobArena.PvP", true);
        config.addDefault("MyPet.Support.MobArena.DisablePetsInArena", false);
        config.addDefault("MyPet.Support.Residence", true);
        config.addDefault("MyPet.Support.AncientRPG", true);
        config.addDefault("MyPet.Support.GriefPrevention", true);
        config.addDefault("MyPet.Support.PvPManager", true);
        config.addDefault("MyPet.Support.Minigames.DisablePetsInGames", true);
        config.addDefault("MyPet.Support.PvPArena.DisablePetsInArena", true);
        config.addDefault("MyPet.Support.PvPArena.PvP", true);
        config.addDefault("MyPet.Support.SurvivalGames.PvP", true);
        config.addDefault("MyPet.Support.SurvivalGames.DisablePetsInGames", true);
        config.addDefault("MyPet.Support.MyHungerGames.DisablePetsInGames", true);
        config.addDefault("MyPet.Support.BattleArena.DisablePetsInArena", true);
        config.addDefault("MyPet.Support.Vault.Economy", true);

        config.addDefault("MyPet.Exp.DamageWeightedExperienceDistribution", Experience.DAMAGE_WEIGHTED_EXPERIENCE_DISTRIBUTION);
        config.addDefault("MyPet.Exp.Passive.Always-Grant-Passive-XP", Experience.ALWAYS_GRANT_PASSIVE_XP);
        config.addDefault("MyPet.Exp.Passive.PercentPerMonster", Experience.PASSIVE_PERCENT_PER_MONSTER);
        config.addDefault("MyPet.Exp.Loss.Percent", Experience.LOSS_PERCENT);
        config.addDefault("MyPet.Exp.Loss.Fixed", Experience.LOSS_FIXED);
        config.addDefault("MyPet.Exp.Loss.Drop", Experience.DROP_LOST_EXP);
        config.addDefault("MyPet.Exp.Gain.MonsterSpawner", Experience.GAIN_EXP_FROM_MONSTER_SPAWNER_MOBS);

        config.addDefault("MyPet.Skill.Control.Item", Material.LEASH.getId());
        config.addDefault("MyPet.Skill.Ride.Item", Material.LEASH.getId());
        config.addDefault("MyPet.Skill.Inventory.Creative", Inventory.OPEN_IN_CREATIVEMODE);
        config.addDefault("MyPet.Skill.Inventory.DropWhenOwnerDies", Inventory.DROP_WHEN_OWNER_DIES);
        config.addDefault("MyPet.Skill.Behavior.Aggro", Behavior.BehaviorState.Aggressive.isActive());
        config.addDefault("MyPet.Skill.Behavior.Farm", Behavior.BehaviorState.Farm.isActive());
        config.addDefault("MyPet.Skill.Behavior.Friendly", Behavior.BehaviorState.Friendly.isActive());
        config.addDefault("MyPet.Skill.Behavior.Raid", Behavior.BehaviorState.Raid.isActive());
        config.addDefault("MyPet.Skill.Behavior.Duel", Behavior.BehaviorState.Duel.isActive());
        config.addDefault("MyPet.Skill.Beacon.HungerDecreaseTime", Beacon.HUNGER_DECREASE_TIME);
        config.addDefault("MyPet.Skill.Beacon.Party-Support", true);

        config.addDefault("MyPet.Pets.Chicken.CanLayEggs", MyChicken.CAN_LAY_EGGS);
        config.addDefault("MyPet.Pets.Cow.CanGiveMilk", MyCow.CAN_GIVE_MILK);
        config.addDefault("MyPet.Pets.Sheep.CanBeSheared", MySheep.CAN_BE_SHEARED);
        config.addDefault("MyPet.Pets.Sheep.CanRegrowWool", MySheep.CAN_REGROW_WOOL);
        config.addDefault("MyPet.Pets.IronGolem.CanThrowUp", MyIronGolem.CAN_THROW_UP);
        config.addDefault("MyPet.Pets.Snowman.FixSnowTrack", MySnowman.FIX_SNOW_TRACK);
        config.addDefault("MyPet.Pets.Chicken.GrowUpItem", Material.POTION.getId());
        config.addDefault("MyPet.Pets.Cow.GrowUpItem", Material.POTION.getId());
        config.addDefault("MyPet.Pets.Horse.GrowUpItem", Material.BREAD.getId());
        config.addDefault("MyPet.Pets.Mooshroom.GrowUpItem", Material.POTION.getId());
        config.addDefault("MyPet.Pets.Mooshroom.CanGiveStew", MyMooshroom.CAN_GIVE_SOUP);
        config.addDefault("MyPet.Pets.Ocelot.GrowUpItem", Material.POTION.getId());
        config.addDefault("MyPet.Pets.Pig.GrowUpItem", Material.POTION.getId());
        config.addDefault("MyPet.Pets.Sheep.GrowUpItem", Material.POTION.getId());
        config.addDefault("MyPet.Pets.Villager.GrowUpItem", Material.POTION.getId());
        config.addDefault("MyPet.Pets.Wolf.GrowUpItem", Material.POTION.getId());
        config.addDefault("MyPet.Pets.Zombie.GrowUpItem", Material.POTION.getId());
        config.addDefault("MyPet.Pets.PigZombie.GrowUpItem", Material.POTION.getId());

        config.addDefault("MyPet.Info.AdminOnly.PetName", PetInfoDisplay.Name.adminOnly);
        config.addDefault("MyPet.Info.AdminOnly.PetOwner", PetInfoDisplay.Owner.adminOnly);
        config.addDefault("MyPet.Info.AdminOnly.PetHP", PetInfoDisplay.HP.adminOnly);
        config.addDefault("MyPet.Info.AdminOnly.PetDamage", PetInfoDisplay.Damage.adminOnly);
        config.addDefault("MyPet.Info.AdminOnly.PetHunger", PetInfoDisplay.Hunger.adminOnly);
        config.addDefault("MyPet.Info.AdminOnly.PetLevel", PetInfoDisplay.Level.adminOnly);
        config.addDefault("MyPet.Info.AdminOnly.PetEXP", PetInfoDisplay.Exp.adminOnly);
        config.addDefault("MyPet.Info.AdminOnly.PetSkilltree", PetInfoDisplay.Skilltree.adminOnly);

        config.addDefault("MyPet.Info.OverHead.Name", PET_INFO_OVERHEAD_NAME);
        config.addDefault("MyPet.Info.OverHead.Prefix", PET_INFO_OVERHEAD_PREFIX);
        config.addDefault("MyPet.Info.OverHead.Suffix", PET_INFO_OVERHEAD_SUFFIX);

        for (MyPetType petType : MyPetType.values()) {
            MyPetInfo pi = petType.getMyPetClass().getAnnotation(MyPetInfo.class);
            if (pi == null) {
                continue;
            }

            config.addDefault("MyPet.Pets." + petType.getTypeName() + ".HP", pi.hp());
            config.addDefault("MyPet.Pets." + petType.getTypeName() + ".Speed", pi.walkSpeed());
            config.addDefault("MyPet.Pets." + petType.getTypeName() + ".Food", linkFood(pi.food()));
            config.addDefault("MyPet.Pets." + petType.getTypeName() + ".LeashFlags", linkLeashFlags(pi.leashFlags()));
            config.addDefault("MyPet.Pets." + petType.getTypeName() + ".CustomRespawnTimeFactor", 0);
            config.addDefault("MyPet.Pets." + petType.getTypeName() + ".CustomRespawnTimeFixed", 0);
            config.addDefault("MyPet.Pets." + petType.getTypeName() + ".LeashItem", Material.LEASH.getId());
        }

        for (EntityType entityType : MonsterExperience.mobExp.keySet()) {
            config.addDefault("MyPet.Exp.Active." + entityType.getName() + ".Min", MonsterExperience.getMonsterExperience(entityType).getMin());
            config.addDefault("MyPet.Exp.Active." + entityType.getName() + ".Max", MonsterExperience.getMonsterExperience(entityType).getMax());
        }

        config.options().copyDefaults(true);
        MyPetPlugin.getPlugin().saveConfig();
    }

    public static void loadConfiguration() {
        CONSUME_LEASH_ITEM = config.getBoolean("MyPet.Leash.Consume", false);
        ALWAYS_SHOW_LEASH_FOR_OWNER = config.getBoolean("MyPet.Leash.ShowAlwaysForOwner", false);

        Control.CONTROL_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Skill.Control.Item", "" + Material.LEASH.getId()));
        Ride.RIDE_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Skill.Ride.Item", "" + Material.LEASH.getId()));
        Beacon.HUNGER_DECREASE_TIME = config.getInt("MyPet.Skill.Beacon.HungerDecreaseTime", 100);
        Beacon.PARTY_SUPPORT = config.getBoolean("MyPet.Skill.Beacon.Party-Support", true);
        Inventory.OPEN_IN_CREATIVEMODE = config.getBoolean("MyPet.Skill.Inventory.Creative", true);
        Inventory.DROP_WHEN_OWNER_DIES = config.getBoolean("MyPet.Skill.Inventory.DropWhenOwnerDies", false);
        Behavior.BehaviorState.Aggressive.setActive(config.getBoolean("MyPet.Skill.Behavior.Aggro", true));
        Behavior.BehaviorState.Farm.setActive(config.getBoolean("MyPet.Skill.Behavior.Farm", true));
        Behavior.BehaviorState.Friendly.setActive(config.getBoolean("MyPet.Skill.Behavior.Friendly", true));
        Behavior.BehaviorState.Raid.setActive(config.getBoolean("MyPet.Skill.Behavior.Raid", true));
        Behavior.BehaviorState.Duel.setActive(config.getBoolean("MyPet.Skill.Behavior.Duel", true));

        Backup.MAKE_BACKUPS = config.getBoolean("MyPet.Backup.Active", Backup.MAKE_BACKUPS);
        Backup.SAVE_INTERVAL = config.getInt("MyPet.Backup.SaveInterval", Backup.SAVE_INTERVAL);
        Backup.DATE_FORMAT = config.getString("MyPet.Backup.DateFormat", Backup.DATE_FORMAT);

        SKILLTREE_SWITCH_PENALTY_FIXED = config.getDouble("MyPet.Skilltree.SwitchPenaltyFixed", 0.0);
        SKILLTREE_SWITCH_PENALTY_PERCENT = config.getInt("MyPet.Skilltree.SwitchPenaltyPercent", 5);
        SKILLTREE_SWITCH_PENALTY_ADMIN = config.getBoolean("MyPet.Skilltree.SwitchPenaltyAdmin", false);
        INHERIT_ALREADY_INHERITED_SKILLS = config.getBoolean("MyPet.Skilltree.InheritAlreadyInheritedSkills", false);
        RESPAWN_TIME_FACTOR = config.getInt("MyPet.Respawn.Time.Default.Factor", 5);
        RESPAWN_TIME_PLAYER_FACTOR = config.getInt("MyPet.Respawn.Time.Player.Factor", 5);
        RESPAWN_TIME_FIXED = config.getInt("MyPet.Respawn.Time.Default.Fixed", 0);
        RESPAWN_TIME_PLAYER_FIXED = config.getInt("MyPet.Respawn.Time.Player.Fixed", 0);
        RESPAWN_COSTS_FACTOR = config.getDouble("MyPet.Respawn.EconomyCost.Factor", 1.0);
        RESPAWN_COSTS_FIXED = config.getDouble("MyPet.Respawn.EconomyCost.Fixed", 0.0);
        AUTOMATIC_SKILLTREE_ASSIGNMENT = config.getBoolean("MyPet.Skilltree.AutomaticAssignment", false);
        CHOOSE_SKILLTREE_ONLY_ONCE = config.getBoolean("MyPet.Skilltree.ChooseOnce", true);
        PREVENT_LEVELLING_WITHOUT_SKILLTREE = config.getBoolean("MyPet.Skilltree.PreventLevellingWithout", true);
        OWNER_CAN_ATTACK_PET = config.getBoolean("MyPet.OwnerCanAttackPet", false);
        DISABLE_PET_VS_PLAYER = config.getBoolean("MyPet.DisablePetVersusPlayer", false);
        USE_HUNGER_SYSTEM = config.getBoolean("MyPet.HungerSystem.Active", true);
        HUNGER_SYSTEM_TIME = config.getInt("MyPet.HungerSystem.Time", 60);
        MAX_PET_NAME_LENGTH = config.getInt("MyPet.MaxPetNameLength", 64);
        HUNGER_SYSTEM_POINTS_PER_FEED = config.getInt("MyPet.HungerSystem.HungerPointsPerFeed", 6);
        RELEASE_PETS_ON_DEATH = config.getBoolean("MyPet.ReleasePetsOnDeath", false);
        REMOVE_PETS_AFTER_RELEASE = config.getBoolean("MyPet.RemovePetsAfterRelease", false);
        ADD_ZOMBIE_TARGET_GOAL = config.getBoolean("MyPet.ZombieVsMyPet", false);
        RETAIN_EQUIPMENT_ON_TAME = config.getBoolean("MyPet.RetainEquipmentOnTame", true);
        INVISIBLE_LIKE_OWNER = config.getBoolean("MyPet.Make-Pet-Invisible-When-Owner-Is-Invisible", true);
        MYPET_FOLLOW_START_DISTANCE = config.getDouble("MyPet.FollowStartDistance", 7.0D);
        LEVELUP_FIREWORK_COLOR = Integer.decode(config.getString("MyPet.LevelSystem.Firework.Color", "#00FF00"));

        AUTOSAVE_TIME = config.getInt("MyPet.PetStorage.AutoSaveTime", 60);
        STORE_PETS_ON_PLAYER_QUIT = config.getBoolean("MyPet.PetStorage.OnPlayerQuit", true);
        STORE_PETS_ON_PET_LEASH = config.getBoolean("MyPet.PetStorage.OnPetLeash", true);
        STORE_PETS_ON_PET_RELEASE = config.getBoolean("MyPet.PetStorage.OnPetRelease", true);

        PetInfoDisplay.Name.adminOnly = config.getBoolean("MyPet.Info.AdminOnly.PetName", false);
        PetInfoDisplay.HP.adminOnly = config.getBoolean("MyPet.Info.AdminOnly.PetHP", false);
        PetInfoDisplay.Damage.adminOnly = config.getBoolean("MyPet.Info.AdminOnly.PetDamage", false);
        PetInfoDisplay.Hunger.adminOnly = config.getBoolean("MyPet.Info.AdminOnly.PetHunger", false);
        PetInfoDisplay.Level.adminOnly = config.getBoolean("MyPet.Info.AdminOnly.PetLevel", true);
        PetInfoDisplay.Exp.adminOnly = config.getBoolean("MyPet.Info.AdminOnly.PetEXP", true);
        PetInfoDisplay.Owner.adminOnly = config.getBoolean("MyPet.Info.AdminOnly.PetOwner", true);
        PetInfoDisplay.Skilltree.adminOnly = config.getBoolean("MyPet.Info.AdminOnly.PetOwner", true);

        PET_INFO_OVERHEAD_NAME = config.getBoolean("MyPet.Info.OverHead.Name", true);
        PET_INFO_OVERHEAD_PREFIX = Colorizer.setColors(config.getString("MyPet.Info.OverHead.Prefix", "<aqua>"));
        PET_INFO_OVERHEAD_SUFFIX = Colorizer.setColors(config.getString("MyPet.Info.OverHead.Suffix", ""));

        Permissions.USE_EXTENDET_PERMISSIONS = config.getBoolean("MyPet.Permissions.UseExtendedPermissions", false);
        Permissions.ENABLED = config.getBoolean("MyPet.Permissions.Enabled", true);

        Economy.USE_ECONOMY = config.getBoolean("MyPet.Support.Vault.Economy", true);
        Minigames.DISABLE_PETS_IN_MINIGAMES = config.getBoolean("MyPet.Support.Minigames.DisablePetsInGames", true);
        PvPArena.DISABLE_PETS_IN_ARENA = config.getBoolean("MyPet.Support.PvPArena.DisablePetsInArena", true);
        SurvivalGames.DISABLE_PETS_IN_SURVIVAL_GAMES = config.getBoolean("MyPet.Support.SurvivalGames.DisablePetsInGames", true);
        MyHungerGames.DISABLE_PETS_IN_HUNGER_GAMES = config.getBoolean("MyPet.Support.MyHungerGames.DisablePetsInGames", true);
        MobArena.DISABLE_PETS_IN_ARENA = config.getBoolean("MyPet.Support.MobArena.DisablePetsInArena", false);
        PvPChecker.USE_PlayerDamageEntityEvent = config.getBoolean("MyPet.Support.PlayerDamageEntityEvent", false);
        PvPChecker.USE_PvPArena = config.getBoolean("MyPet.Support.PvPArena.PvP", true);
        PvPChecker.USE_Towny = config.getBoolean("MyPet.Support.Towny", true);
        PvPChecker.USE_Factions = config.getBoolean("MyPet.Support.Factions", true);
        PvPChecker.USE_WorldGuard = config.getBoolean("MyPet.Support.WorldGuard", true);
        PvPChecker.USE_Citizens = config.getBoolean("MyPet.Support.Citizens", true);
        PvPChecker.USE_Heroes = config.getBoolean("MyPet.Support.Heroes", true);
        PvPChecker.USE_McMMO = config.getBoolean("MyPet.Support.mcMMO", true);
        PvPChecker.USE_MobArena = config.getBoolean("MyPet.Support.MobArena.PvP", true);
        PvPChecker.USE_SurvivalGame = config.getBoolean("MyPet.Support.SurvivalGames.PvP", true);
        PvPChecker.USE_Regios = config.getBoolean("MyPet.Support.Regios", true);
        PvPChecker.USE_Residence = config.getBoolean("MyPet.Support.Residence", true);
        PvPChecker.USE_AncientRPG = config.getBoolean("MyPet.Support.AncientRPG", true);
        PvPChecker.USE_GriefPrevention = config.getBoolean("MyPet.Support.GriefPrevention", true);
        PvPChecker.USE_PvPManager = config.getBoolean("MyPet.Support.PvPManager", true);

        Experience.LOSS_PERCENT = config.getInt("MyPet.Exp.Loss.Percent", 0);
        Experience.LOSS_FIXED = config.getDouble("MyPet.Exp.Loss.Fixed", 0.0);
        Experience.DROP_LOST_EXP = config.getBoolean("MyPet.Exp.Loss.Drop", true);
        Experience.PASSIVE_PERCENT_PER_MONSTER = config.getInt("MyPet.Exp.Passive.PercentPerMonster", 25);
        Experience.ALWAYS_GRANT_PASSIVE_XP = config.getBoolean("MyPet.Exp.Passive.Always-Grant-Passive-XP", true);
        Experience.GAIN_EXP_FROM_MONSTER_SPAWNER_MOBS = config.getBoolean("MyPet.Exp.Gain.MonsterSpawner", true);
        Experience.CALCULATION_MODE = config.getString("MyPet.LevelSystem.CalculationMode", "Default");
        Experience.DAMAGE_WEIGHTED_EXPERIENCE_DISTRIBUTION = config.getBoolean("MyPet.Exp.DamageWeightedExperienceDistribution", true);
        Experience.FIREWORK_ON_LEVELUP = config.getBoolean("MyPet.LevelSystem.Firework", true);

        MyChicken.CAN_LAY_EGGS = config.getBoolean("MyPet.Pets.Chicken.CanLayEggs", true);
        MyCow.CAN_GIVE_MILK = config.getBoolean("MyPet.Pets.Cow.CanGiveMilk", true);
        MySheep.CAN_BE_SHEARED = config.getBoolean("MyPet.Pets.Sheep.CanBeSheared", true);
        MySheep.CAN_REGROW_WOOL = config.getBoolean("MyPet.Pets.Sheep.CanRegrowWool", true);
        MyIronGolem.CAN_THROW_UP = config.getBoolean("MyPet.Pets.IronGolem.CanThrowUp", true);
        MySnowman.FIX_SNOW_TRACK = config.getBoolean("MyPet.Pets.Snowman.FixSnowTrack", true);
        MyChicken.GROW_UP_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Pets.Chicken.GrowUpItem", "" + Material.POTION.getId()));
        MyCow.GROW_UP_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Pets.Cow.GrowUpItem", "" + Material.POTION.getId()));
        MyHorse.GROW_UP_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Pets.Horse.GrowUpItem", "" + Material.BREAD.getId()));
        MyMooshroom.GROW_UP_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Pets.Mooshroom.GrowUpItem", "" + Material.POTION.getId()));
        MyMooshroom.CAN_GIVE_SOUP = config.getBoolean("MyPet.Pets.Mooshroom.CanGiveStew", false);
        MyOcelot.GROW_UP_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Pets.Ocelot.GrowUpItem", "" + Material.POTION.getId()));
        MyPig.GROW_UP_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Pets.Pig.GrowUpItem", "" + Material.POTION.getId()));
        MySheep.GROW_UP_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Pets.Sheep.GrowUpItem", "" + Material.POTION.getId()));
        MyVillager.GROW_UP_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Pets.Villager.GrowUpItem", "" + Material.POTION.getId()));
        MyWolf.GROW_UP_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Pets.Wolf.GrowUpItem", "" + Material.POTION.getId()));
        MyZombie.GROW_UP_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Pets.Zombie.GrowUpItem", "" + Material.POTION.getId()));
        MyPigZombie.GROW_UP_ITEM = ConfigItem.createConfigItem(config.getString("MyPet.Pets.PigZombie.GrowUpItem", "" + Material.POTION.getId()));

        MyPet.resetOptions();
        for (MyPetType petType : MyPetType.values()) {
            MyPetInfo pi = petType.getMyPetClass().getAnnotation(MyPetInfo.class);

            MyPet.setStartHP(petType.getMyPetClass(), config.getDouble("MyPet.Pets." + petType.getTypeName() + ".HP", pi.hp()));
            MyPet.setStartSpeed(petType.getMyPetClass(), config.getDouble("MyPet.Pets." + petType.getTypeName() + ".Speed", pi.walkSpeed()));
            seperateFood(petType.getMyPetClass(), config.getString("MyPet.Pets." + petType.getTypeName() + ".Food", linkFood(pi.food())));
            seperateLeashFlags(petType.getMyPetClass(), config.getString("MyPet.Pets." + petType.getTypeName() + ".LeashFlags", linkLeashFlags(pi.leashFlags())));
            MyPet.setCustomRespawnTimeFactor(petType.getMyPetClass(), config.getInt("MyPet.Pets." + petType.getTypeName() + ".CustomRespawnTimeFactor", 0));
            MyPet.setCustomRespawnTimeFixed(petType.getMyPetClass(), config.getInt("MyPet.Pets." + petType.getTypeName() + ".CustomRespawnTimeFixed", 0));
            MyPet.setLeashItem(petType.getMyPetClass(), ConfigItem.createConfigItem(config.getString("MyPet.Pets." + petType.getTypeName() + ".LeashItem", "" + Material.LEASH.getId())));
        }

        if (config.getStringList("MyPet.exp.active") != null) {
            double min;
            double max;
            for (EntityType entityType : MonsterExperience.mobExp.keySet()) {
                max = config.getDouble("MyPet.Exp.Active." + entityType.getName() + ".Max", 0.);
                min = config.getDouble("MyPet.Exp.Active." + entityType.getName() + ".Min", 0.);
                if (min == max) {
                    MonsterExperience.getMonsterExperience(entityType).setExp(max);
                } else {
                    MonsterExperience.getMonsterExperience(entityType).setMin(min);
                    MonsterExperience.getMonsterExperience(entityType).setMax(max);
                }
            }
        }
    }

    private static String linkFood(Material[] foodTypes) {
        String linkedFood = "";
        for (Material foodType : foodTypes) {
            if (!linkedFood.equalsIgnoreCase("")) {
                linkedFood += ";";
            }
            linkedFood += foodType.getId();
        }
        return linkedFood;
    }

    private static void seperateFood(Class<? extends MyPet> myPetClass, String foodString) {
        foodString = foodString.trim();
        while (true) {
            if (foodString.endsWith("\\;")) {
                foodString = foodString.substring(0, foodString.length() - 2);
                continue;
            }
            if (foodString.endsWith(";")) {
                foodString = foodString.substring(0, foodString.length() - 1);
                continue;
            }
            break;
        }
        if (foodString.contains(";")) {
            for (String foodIDString : foodString.split("(?<!\\\\);")) {
                MyPet.setFood(myPetClass, ConfigItem.createConfigItem(foodIDString.replace("\\;", ";")));
            }
        } else {
            MyPet.setFood(myPetClass, ConfigItem.createConfigItem(foodString));
        }
    }

    private static String linkLeashFlags(LeashFlag[] leashFlags) {
        String linkedLeashFlags = "";
        for (LeashFlag leashFlag : leashFlags) {
            if (!linkedLeashFlags.equalsIgnoreCase("")) {
                linkedLeashFlags += ",";
            }
            linkedLeashFlags += leashFlag.name();
        }
        return linkedLeashFlags;
    }

    private static void seperateLeashFlags(Class<? extends MyPet> myPetClass, String leashFlagString) {
        leashFlagString = leashFlagString.replaceAll("\\s", "");
        if (leashFlagString.contains(",")) {
            for (String leashFlagSplit : leashFlagString.split(",")) {
                if (LeashFlag.getLeashFlagByName(leashFlagSplit) != null) {
                    MyPet.setLeashFlags(myPetClass, LeashFlag.getLeashFlagByName(leashFlagSplit));
                } else {
                    MyPetLogger.write(ChatColor.RED + leashFlagString + " is not a valid LeashFlag!");
                }
            }
        } else {
            if (LeashFlag.getLeashFlagByName(leashFlagString) != null) {
                MyPet.setLeashFlags(myPetClass, LeashFlag.getLeashFlagByName(leashFlagString));
            } else {
                MyPetLogger.write(ChatColor.RED + leashFlagString + " is not a valid LeashFlag!");
            }
        }
    }
}