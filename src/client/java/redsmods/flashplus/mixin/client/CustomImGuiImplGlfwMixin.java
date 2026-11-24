package redsmods.flashplus.mixin.client;

import com.moulberry.flashback.editor.ui.CustomImGuiWindowerSdl;
import com.moulberry.flashback.editor.ui.ReplayUI;
import com.moulberry.flashback.state.EditorState;
import com.moulberry.flashback.state.EditorStateManager;
import imgui.moulberry90.ImGuiIO;
import org.lwjgl.sdl.SDLKeycode;
import org.lwjgl.sdl.SDL_KeyboardEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CustomImGuiWindowerSdl.class)
public class CustomImGuiImplGlfwMixin {

    @Inject(method = "shouldPassToMinecraft", at = @At("HEAD"), cancellable = true, remap = false)
    private void suppressRollKeys(ImGuiIO io, SDL_KeyboardEvent event, int key, boolean down, boolean repeat, boolean isImGuiDown, boolean isGameDown, CallbackInfoReturnable<Boolean> cir) {
        if (key != SDLKeycode.SDLK_Q && key != SDLKeycode.SDLK_E) return;
        if (!down) return; // always let releases through to avoid stuck keys

        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState == null || !editorState.replayVisuals.overrideRoll) return;

        if (!ReplayUI.isActive()) return;
        if (ReplayUI.isMovingCamera()) return; // grabbed state — game should handle it
        if (io.getWantCaptureKeyboard()) return; // imgui text field active, don't intercept

        cir.setReturnValue(false);
    }
}