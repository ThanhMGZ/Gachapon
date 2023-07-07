package org.thanhmagics.gachapon.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.thanhmagics.gachapon.commands.SetupCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TextComponentBuilder {

    private String text;

    private boolean underLine = false;

    private List<TextComponent> description = new ArrayList<>();

    private ClickEvent clickEvent;

    public TextComponentBuilder(String text) {
        this.text = Utils.applyColor(text);
    }

    public TextComponentBuilder setUnderLine(boolean b) {
        this.underLine =b;
        return this;
    }

    public TextComponentBuilder addDescription(String... strings) {
        for (String s : strings) {
            TextComponent textComponent = new TextComponent(Utils.applyColor(s));
            this.description.add(textComponent);
        }
        return this;
    }

    public TextComponentBuilder setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }


//    TextComponent text = new TextComponent("test");
//    TextComponent hoverText = new TextComponent(":)");
//    HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{hoverText});
//    TextComponent t2 = new TextComponent("test2");
//                text.setHoverEvent(hoverEvent);
//    TextComponent t3 = new TextComponent("test3");
//                t3.setUnderlined(false);
//                t2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/say a"));
//                t3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/say b"));
//
//                player.spigot().sendMessage(text);
//                player.spigot().sendMessage(t2,t3);
    public TextComponent build() {
        TextComponent textComponent = new TextComponent(text);
        if (description.size() > 0) {
          //  textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (BaseComponent[]) description.toArray()));
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {description.get(0)}));
        }
        if (clickEvent != null)
            textComponent.setClickEvent(clickEvent);
        textComponent.setUnderlined(underLine);
        return textComponent;
    }

}
