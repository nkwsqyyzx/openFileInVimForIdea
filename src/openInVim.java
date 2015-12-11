import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;

public class openInVim extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Document currentDoc = FileEditorManager.getInstance(e.getProject()).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        final String fileName = currentFile.getPath();
        CommandProcessor.getInstance().executeCommand(e.getProject(), new Runnable() {
            @Override
            public void run() {
                GeneralCommandLine line = new GeneralCommandLine("/usr/local/bin/gvim", "--servername", "idea_common", "--remote-tab-silent", fileName);
                try {
                    line.createProcess();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        }, "OpenInVim", null);
    }
}
