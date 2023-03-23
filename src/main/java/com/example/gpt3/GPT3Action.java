package com.example.gpt3;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import javax.swing.JTextField;

public class GPT3Action extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();

        // Create the dialog box
        DialogBuilder dialogBuilder = new DialogBuilder(project);
        dialogBuilder.setTitle("Generate Text");
        dialogBuilder.addT("Prompt:");
        dialogBuilder.addTextField("Max Tokens:");

        dialogBuilder.addAction(new AbstractAction("Generate") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String prompt = dialogBuilder.getTextField().getText();
                int maxTokens = Integer.parseInt(dialogBuilder.getNorthTextField().getText());

                GPT3Service gpt3Service = new GPT3Service(project);
                String generatedText = gpt3Service.generateText(prompt, maxTokens);

                if (generatedText != null) {
                    dialogBuilder.getDialogWrapper().close(DialogWrapper.OK_EXIT_CODE);

                    ApplicationManager.getApplication().invokeLater(() -> {
                        Messages.showMessageDialog(project, generatedText, "Generated Text", Messages.getInformationIcon());
                    });
                }
            }
        });

        dialogBuilder.show();
    }
}
