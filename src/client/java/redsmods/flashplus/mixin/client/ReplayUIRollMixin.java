package redsmods.flashplus.mixin.client;

import com.moulberry.flashback.editor.ui.ReplayUI;
import com.moulberry.flashback.state.EditorState;
import com.moulberry.flashback.state.EditorStateManager;
import imgui.moulberry90.ImGui;
import imgui.moulberry90.flag.ImGuiKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static redsmods.flashplus.FlashplusClient.lockRoll;

@Mixin(ReplayUI.class)
public class ReplayUIRollMixin {
    @Inject(method = "drawOverlayInternal", at = @At("HEAD"))
    private static void onDrawOverlay(CallbackInfo ci) {
        EditorState editorState = EditorStateManager.getCurrent();
        if (editorState == null || !editorState.replayVisuals.overrideRoll || lockRoll) {
            return;
        }

        if (ReplayUI.isMovingCamera() || !ReplayUI.getIO().getWantCaptureKeyboard()) {
            float rollSpeed = 1.0f; // Degrees per frame

            // Apply speed multiplier if holding 'Shift' (Flashback uses isMoveQuickDown)
            if (ReplayUI.isMoveQuickDown()) {
                rollSpeed *= 2.0f;
            }

            if (ImGui.isKeyDown(ImGuiKey.Q)) {
                editorState.replayVisuals.overrideRollAmount -= rollSpeed;
                ImGui.getIO().setWantCaptureKeyboard(true);
            }
            if (ImGui.isKeyDown(ImGuiKey.E)) {
                editorState.replayVisuals.overrideRollAmount += rollSpeed;
                ImGui.getIO().setWantCaptureKeyboard(true);
            }
        }
    }
}
