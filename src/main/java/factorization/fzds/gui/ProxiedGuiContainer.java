package factorization.fzds.gui;

import factorization.fzds.Hammer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class ProxiedGuiContainer extends GuiContainer {
    final GuiContainer sub;

    public ProxiedGuiContainer(Container container, GuiContainer sub) {
        super(container);
        this.sub = sub;
        if (sub instanceof ProxiedGuiContainer) {
            throw new IllegalArgumentException("No nesting! Not allowed!");
        }
    }

    private boolean enter() {
        if (!Hammer.proxy.isInShadowWorld()) {
            Hammer.proxy.setShadowWorld();
            return true;
        }
        return false;
    }

    private void exit(boolean do_it) {
        if (!do_it) return;
        Hammer.proxy.restoreRealWorld();
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partial, int mouseX, int mouseY) {
        // No-op; sub.drawScreen()'ll make this happen
    }

    public void initGui() {
        boolean switched = enter();
        try {
            sub.initGui();
        } finally {
            exit(switched);
        }

        this.width = sub.width;
        this.height = sub.height;

        super.initGui();
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        boolean switched = enter();
        try {
            sub.setWorldAndResolution(mc, width, height);
        } finally {
            exit(switched);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partial) {
        boolean switched = enter();
        try {
            sub.drawScreen(mouseX, mouseY, partial);
        } finally {
            exit(switched);
        }
    }

    @Override
    public void handleInput() {
        boolean switched = enter();
        try {
            sub.handleInput();
        } finally {
            exit(switched);
        }
    }

    public void onGuiClosed() {
        boolean switched = enter();
        try {
            sub.onGuiClosed();
        } finally {
            exit(switched);
        }
        super.onGuiClosed();
    }

    public boolean doesGuiPauseGame() {
        return sub.doesGuiPauseGame();
    }

    public void updateScreen() {
        boolean switched = enter();
        try {
            sub.updateScreen();
        } finally {
            exit(switched);
        }
    }
}
