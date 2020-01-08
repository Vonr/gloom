// 
// Decompiled by Procyon v0.5.36
// 

package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.kami.KamiGUI;
import org.lwjgl.opengl.GL11;
import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
import java.awt.Font;
import java.awt.Color;
import me.zeroeightsix.kami.gui.font.CFontRenderer;
import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
import me.zeroeightsix.kami.gui.rgui.component.use.CheckButton;

public class RootCheckButtonUI<T extends CheckButton> extends AbstractComponentUI<CheckButton>
{
    CFontRenderer cFontRenderer;
    protected Color backgroundColour;
    protected Color backgroundColourHover;
    protected Color idleColourNormal;
    protected Color downColourNormal;
    protected Color idleColourToggle;
    protected Color downColourToggle;
    
    public RootCheckButtonUI() {
        this.cFontRenderer = new CFontRenderer(new Font("Verdana", 0, 18), true, false);
        this.backgroundColour = new Color(200, 56, 56);
        this.backgroundColourHover = new Color(255, 66, 66);
        this.idleColourNormal = new Color(200, 200, 200);
        this.downColourNormal = new Color(190, 190, 190);
        this.idleColourToggle = new Color(250, 120, 120);
        this.downColourToggle = this.idleColourToggle.brighter();
    }
    
    @Override
    public void renderComponent(final CheckButton component, final FontRenderer ff) {
        if (component.isToggled()) {}
        if (component.isHovered() || component.isPressed()) {}
        final String text = component.getName();
        int c = component.isPressed() ? 11184810 : (component.isToggled() ? 16724787 : 14540253);
        if (component.isHovered()) {
            c = (c & 0x7F7F7F) << 1;
        }
        GL11.glEnable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        KamiGUI.cFontRenderer.drawString(text, (float)(component.getWidth() / 2 - KamiGUI.fontRenderer.getStringWidth(text) / 2), (float)(KamiGUI.fontRenderer.getFontHeight() / 2 - 2), c);
        GL11.glDisable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2884);
    }
    
    @Override
    public void handleAddComponent(final CheckButton component, final Container container) {
        component.setWidth(KamiGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(KamiGUI.fontRenderer.getFontHeight() + 2);
    }
}
