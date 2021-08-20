/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NLPTranslation;

import com.google.api.client.util.StringUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.SyncTree.CompletionListener;
import com.google.firebase.database.core.view.Event;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Mouad AITALI
 */
public class mainui extends javax.swing.JFrame {

    /**
     * Creates new form mainui
     */
    private DatabaseReference mDatabase;
    private static final String CMD_ID = "command";
    private String outputStr;
    private static final String CONNECTING = "Connecting...";
    private static final String LISTENING = "Listening...";
    private static final String EXECUTING_CMD = "Executing python script...";

    public mainui() {
        try {
            setTitle("Translation Helper");
            Common.initializeFirebase();
            getOutputCommand();
        } catch (IOException ex) {
            Logger.getLogger(mainui.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
    }

    private void getOutputCommand() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Commands");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String input = dsp.child("input").getValue().toString();
                    if (input.equalsIgnoreCase("none")) {
                        state.setText(LISTENING);
                        System.out.println(LISTENING);
                    } else {
                        state.setText(EXECUTING_CMD);
                        System.out.println(EXECUTING_CMD);
                        try {
                            File pythonFile = new File("C://scripts/script.py");
                            FileWriter fileWriter = new FileWriter(pythonFile);
                            fileWriter.write(generateInput(input));

                            fileWriter.close();
                            System.out.println("Successfully wrote to the file.");

                            Process process = Runtime.getRuntime().exec("python3 " + pythonFile.getAbsolutePath());
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(process.getInputStream()));
                            String line;
                            outputStr = "";
                            while ((line = reader.readLine()) != null) {
                                outputStr = outputStr + line;
                                System.out.println("==> " + outputStr);
                            }
                            System.out.println(LISTENING);
                            reader.close();
                            dsp.getRef().setValue(new CommandLine("none", outputStr), (DatabaseError de, DatabaseReference dr) -> {
                            });
                        } catch (IOException e) {
                            System.out.println("ERROR ==> " + e.getMessage());
                        }
                    }
                    break;
                }
            }

            @Override
            public void onCancelled(DatabaseError de) {
                System.out.print("OUTPUT ==> " + "ERROR");
                throw new UnsupportedOperationException("Not supported yet.");
            }

        });

    }

    private String generateInput(String input) {
        String[] inputs = input.split("\\$");

        String isoInputLang = inputs[0];
        String textInput = inputs[1];

        String isoOutputLang = inputs[2];
        String mname = "Helsinki-NLP/opus-mt-" + isoInputLang + "-" + isoOutputLang;
        String fullScript = "# -*- coding: utf-8 -*-\n"
                + "import sys\n"
                + "from transformers import AutoModelWithLMHead, AutoTokenizer\n"
                + "from transformers import pipeline\n"
                + "\n"
                + "\n"
                + "mname = " + "\"" + mname + "\""
                + "\n"
                + "model = AutoModelWithLMHead.from_pretrained(mname)\n"
                + "tokenizer = AutoTokenizer.from_pretrained(mname)\n"
                + "translation = pipeline(\"translation_" + isoInputLang + "_to_" + isoOutputLang + "\", model=model, tokenizer=tokenizer)\n"
                + "text = " + "\"" + textInput + "\""
                + "\n"
                + "translated_text = translation(text, max_length=40)[0]['translation_text']\n"
                + "\n"
                + "sys.stdout.buffer.write(translated_text.encode(\"utf-8\"))";
        System.out.println("==> " + fullScript);
        return fullScript;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        state = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        state.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        state.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        state.setText("Connecting...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(state, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(state, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(120, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainui().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel state;
    // End of variables declaration//GEN-END:variables
}
