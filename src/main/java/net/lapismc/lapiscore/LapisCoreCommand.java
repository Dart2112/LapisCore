/*
 * Copyright 2018 Benjamin Martin
 *
 * MICROSOFT REFERENCE SOURCE LICENSE (MS-RSL)
 *
 * This license governs use of the accompanying software. If you use the software, you accept this license. If you do not accept the license, do not use the software.
 * 1. Definitions
 *
 * The terms "reproduce," "reproduction" and "distribution" have the same meaning here as under U.S. copyright law.
 *
 * "You" means the licensee of the software.
 *
 * "Your company" means the company you worked for when you downloaded the software.
 *
 * "Reference use" means use of the software within your company as a reference, in read only form, for the sole purposes of debugging your products, maintaining your products, or enhancing the interoperability of your products with the software, and specifically excludes the right to distribute the software outside of your company.
 *
 * "Licensed patents" means any Licensor patent claims which read directly on the software as distributed by the Licensor under this license.
 * 2. Grant of Rights
 *
 * (A) Copyright Grant- Subject to the terms of this license, the Licensor grants you a non-transferable, non-exclusive, worldwide, royalty-free copyright license to reproduce the software for reference use.
 *
 * (B) Patent Grant- Subject to the terms of this license, the Licensor grants you a non-transferable, non-exclusive, worldwide, royalty-free patent license under licensed patents for reference use.
 * 3. Limitations
 *
 * (A) No Trademark License- This license does not grant you any rights to use the Licensor's name, logo, or trademarks.
 *
 * (B) If you begin patent litigation against the Licensor over patents that you think may apply to the software (including a cross-claim or counterclaim in a lawsuit), your license to the software ends automatically.
 *
 * (C) The software is licensed "as-is." You bear the risk of using it. The Licensor gives no express warranties, guarantees or conditions. You may have additional consumer rights under your local laws which this license cannot change. To the extent permitted under your local laws, the Licensor excludes the implied warranties of merchantability, fitness for a particular purpose and non-infringement.
 */

package net.lapismc.lapiscore;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class LapisCoreCommand extends BukkitCommand {

    private final LapisCorePlugin core;

    protected LapisCoreCommand(LapisCorePlugin core, String name, String desc, ArrayList<String> aliases) {
        super(name);
        this.core = core;
        setDescription(desc);
        setAliases(aliases);
        registerCommand(name);
    }

    private void registerCommand(String name) {
        try {
            final Field serverCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            serverCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) serverCommandMap.get(Bukkit.getServer());
            commandMap.register(name, this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        onCommand(sender, args);
        return true;
    }

    protected abstract void onCommand(CommandSender sender, String[] args);

    protected void sendMessage(CommandSender sender, String key) {
        sender.sendMessage(core.config.getMessage(key));
    }

    protected boolean isNotPlayer(CommandSender sender, String key) {
        if (!(sender instanceof Player)) {
            sendMessage(sender, key);
            return true;
        }
        return false;
    }

}
