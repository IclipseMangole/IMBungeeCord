package de.Iclipse.IMBungee.Util.Command;

import de.Iclipse.IMBungee.Util.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BungeeCommand extends Command implements TabExecutor {
    private final Object function;
    private final IMCommand command;
    private final GlobalCommand<CommandSender> processor;

    public BungeeCommand(Object function, Method method, IMCommand command) {
        super(command.name(), null, command.aliases().length > 0 ? command.aliases() : new String[]{command.name()});
        this.function = function;
        this.command = command;
        this.processor = new GlobalCommand<CommandSender>(command, function, method, ProxiedPlayer.class) {
            @Override
            public boolean checkPermission(CommandSender sender, String permission) {
                return sender.hasPermission(permission);
            }
        };
    }

    @Override
    public void execute(final CommandSender sender, String[] args) {
        processor.process(sender, args);
    }

    public GlobalCommand<CommandSender> getProcessor() {
        return processor;
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender commandSender, final String[] strings) {
        if (commandSender instanceof ProxiedPlayer && commandSender.hasPermission("aw.tabcomplete")) {
            final ServerInfo server = ((ProxiedPlayer) commandSender).getServer().getInfo();
            final String search = strings[strings.length - 1];
            return ProxyServer.getInstance().getPlayers().stream().map(ProxiedPlayer::getName).filter(name -> name.toLowerCase().startsWith(search.toLowerCase())).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
