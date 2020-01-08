// 
// Decompiled by Procyon v0.5.36
// 

package me.zeroeightsix.kami.gui.rgui.component.container;

import me.zeroeightsix.kami.gui.rgui.component.Component;
import me.zeroeightsix.kami.gui.rgui.render.theme.Theme;
import me.zeroeightsix.kami.gui.rgui.layout.Layout;
import me.zeroeightsix.kami.gui.rgui.util.Docking;

public class OrganisedContainer extends AbstractContainer
{
    Layout layout;
    
    public OrganisedContainer(final Theme theme, final Layout layout) {
        super(theme);
        this.layout = layout;
    }
    
    public Layout getLayout() {
        return this.layout;
    }
    
    public void setLayout(final Layout layout) {
        this.layout = layout;
    }
    
    @Override
    public Container addChild(final Component... component) {
        super.addChild(component);
        this.layout.organiseContainer(this);
        return this;
    }
    
    @Override
    public void setOriginOffsetX(final int originoffsetX) {
        super.setOriginOffsetX(originoffsetX);
        this.layout.organiseContainer(this);
    }
    
    @Override
    public void setOriginOffsetY(final int originoffsetY) {
        super.setOriginOffsetY(originoffsetY);
        this.layout.organiseContainer(this);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean isPinned() {
        return false;
    }

    @Override
    public boolean isMinimized() {
        return false;
    }

    @Override
    public Docking getDocking() {
        return null;
    }
}
