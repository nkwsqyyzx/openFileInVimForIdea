package space.wsq.www.vim_action;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.psi.PsiFile;

public class openInVim extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        PsiFile file = (PsiFile) e.getData(DataKeys.PSI_FILE);
        if (file == null) {
            return;
        }
        final String fileName = file.getVirtualFile().getCanonicalPath();
        EditorImpl editor = (EditorImpl) e.getData(PlatformDataKeys.EDITOR);
        String cursor = null;
        if (editor != null) {
            int line = editor.getCaretModel().getLogicalPosition().line + 1;
            int column = editor.getCaretModel().getLogicalPosition().column;
            cursor = "+call cursor(" + line + ", " + column + ")";
        }
        final String xCursor = cursor;
        CommandProcessor.getInstance().executeCommand(e.getProject(), new Runnable() {
            public void run() {
                String[] args = new String[]{"/usr/local/bin/gvim", "--servername", "idea_common", "--remote-tab-silent", fileName};
                if (xCursor != null) {
                    args = new String[]{"/usr/local/bin/gvim", "--servername", "idea_common", "--remote-tab-silent", xCursor, fileName};
                }
                GeneralCommandLine line = new GeneralCommandLine(args);
                try {
                    line.createProcess();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        }, "OpenInVim", null);
    }
}
