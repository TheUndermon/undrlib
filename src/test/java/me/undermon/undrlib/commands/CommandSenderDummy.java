package me.undermon.undrlib.commands;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

class CommandSenderDummy implements CommandSender {

	@Override
	public boolean isPermissionSet(String name) {
		throw new UnsupportedOperationException("Unimplemented method 'isPermissionSet'");
	}

	@Override
	public boolean isPermissionSet(Permission perm) {
		throw new UnsupportedOperationException("Unimplemented method 'isPermissionSet'");
	}

	@Override
	public boolean hasPermission(String name) {
		throw new UnsupportedOperationException("Unimplemented method 'hasPermission'");
	}

	@Override
	public boolean hasPermission(Permission perm) {
		throw new UnsupportedOperationException("Unimplemented method 'hasPermission'");
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		throw new UnsupportedOperationException("Unimplemented method 'addAttachment'");
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		throw new UnsupportedOperationException("Unimplemented method 'addAttachment'");
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		throw new UnsupportedOperationException("Unimplemented method 'addAttachment'");
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		throw new UnsupportedOperationException("Unimplemented method 'addAttachment'");
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		throw new UnsupportedOperationException("Unimplemented method 'removeAttachment'");
	}

	@Override
	public void recalculatePermissions() {
		throw new UnsupportedOperationException("Unimplemented method 'recalculatePermissions'");
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		throw new UnsupportedOperationException("Unimplemented method 'getEffectivePermissions'");
	}

	@Override
	public boolean isOp() {
		throw new UnsupportedOperationException("Unimplemented method 'isOp'");
	}

	@Override
	public void setOp(boolean value) {
		throw new UnsupportedOperationException("Unimplemented method 'setOp'");
	}

	@Override
	public void sendMessage(String message) {
		throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
	}

	@Override
	public void sendMessage(String... messages) {
		throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
	}

	@Override
	public void sendMessage(UUID sender, String message) {
		throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
	}

	@Override
	public void sendMessage(UUID sender, String... messages) {
		throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
	}

	@Override
	public Server getServer() {
		throw new UnsupportedOperationException("Unimplemented method 'getServer'");
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException("Unimplemented method 'getName'");
	}

	@Override
	public Spigot spigot() {
		throw new UnsupportedOperationException("Unimplemented method 'spigot'");
	}

	@Override
	public @NotNull Component name() {
		throw new UnsupportedOperationException("Unimplemented method 'name'");
	}
}