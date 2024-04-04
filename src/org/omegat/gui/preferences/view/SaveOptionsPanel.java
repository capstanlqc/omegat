/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2012 Didier Briel, Aaron Madlon-Kay
               2015 Aaron Madlon-Kay
               2016 Aaron Madlon-Kay
               Home page: http://www.omegat.org/
               Support center: https://omegat.org/support

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package org.omegat.gui.preferences.view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.omegat.util.OStrings;

/**
 * @author Maxym Mykhalchuk
 * @author Didier Briel
 * @author Aaron Madlon-Kay
 */
@SuppressWarnings("serial")
public class SaveOptionsPanel extends JPanel {

    /** Creates new form SaveOptionsPanel */
    public SaveOptionsPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        intervalDescriptionTextArea = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        minutesLabel = new javax.swing.JLabel();
        minutesSpinner = new javax.swing.JSpinner();
        secondsLabel = new javax.swing.JLabel();
        secondsSpinner = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        externalCommandLabel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        externalCmdDescriptionTextArea = new javax.swing.JTextArea();
        externalCommandScrollPane = new javax.swing.JScrollPane();
        externalCommandTextArea = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        variablesLabel = new javax.swing.JLabel();
        variablesList = new javax.swing.JComboBox<>();
        insertButton = new javax.swing.JButton();
        allowProjectCmdCheckBox = new javax.swing.JCheckBox();
        compileCloseCheckbox = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        jPanel1.setLayout(new java.awt.BorderLayout());

        intervalDescriptionTextArea.setEditable(false);
        intervalDescriptionTextArea.setFont(minutesLabel.getFont());
        intervalDescriptionTextArea.setLineWrap(true);
        intervalDescriptionTextArea.setText(OStrings.getString("SAVE_DIALOG_DESCRIPTION")); // NOI18N
        intervalDescriptionTextArea.setWrapStyleWord(true);
        intervalDescriptionTextArea.setFocusable(false);
        intervalDescriptionTextArea.setOpaque(false);
        jPanel1.add(intervalDescriptionTextArea, java.awt.BorderLayout.NORTH);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 0, 0));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel10.setLayout(new java.awt.GridBagLayout());

        minutesLabel.setLabelFor(minutesSpinner);
        org.openide.awt.Mnemonics.setLocalizedText(minutesLabel, OStrings.getString("SAVE_DIALOG_MINUTES")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(minutesLabel, gridBagConstraints);

        minutesSpinner.setValue(90);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 50;
        jPanel10.add(minutesSpinner, gridBagConstraints);

        secondsLabel.setLabelFor(secondsSpinner);
        org.openide.awt.Mnemonics.setLocalizedText(secondsLabel, OStrings.getString("SAVE_DIALOG_SECONDS")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel10.add(secondsLabel, gridBagConstraints);

        secondsSpinner.setValue(90);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 50;
        jPanel10.add(secondsSpinner, gridBagConstraints);

        jPanel5.add(jPanel10, java.awt.BorderLayout.WEST);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        externalCommandLabel.setLabelFor(externalCommandTextArea);
        org.openide.awt.Mnemonics.setLocalizedText(externalCommandLabel, OStrings.getString("EXTERNAL_COMMAND_LABEL")); // NOI18N
        jPanel2.add(externalCommandLabel, java.awt.BorderLayout.NORTH);

        jPanel8.setLayout(new java.awt.BorderLayout());

        externalCmdDescriptionTextArea.setEditable(false);
        externalCmdDescriptionTextArea.setFont(minutesLabel.getFont());
        externalCmdDescriptionTextArea.setLineWrap(true);
        externalCmdDescriptionTextArea.setText(OStrings.getString("EXTERNAL_COMMAND_DESCRIPTION")); // NOI18N
        externalCmdDescriptionTextArea.setWrapStyleWord(true);
        externalCmdDescriptionTextArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        externalCmdDescriptionTextArea.setOpaque(false);
        jPanel8.add(externalCmdDescriptionTextArea, java.awt.BorderLayout.NORTH);

        externalCommandTextArea.setColumns(20);
        externalCommandTextArea.setLineWrap(true);
        externalCommandTextArea.setRows(5);
        externalCommandScrollPane.setViewportView(externalCommandTextArea);

        jPanel8.add(externalCommandScrollPane, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(variablesLabel, OStrings.getString("EXT_TMX_MATCHES_TEMPLATE_VARIABLES")); // NOI18N
        jPanel4.add(variablesLabel, java.awt.BorderLayout.WEST);
        jPanel4.add(variablesList, java.awt.BorderLayout.CENTER);

        org.openide.awt.Mnemonics.setLocalizedText(insertButton, OStrings.getString("BUTTON_INSERT")); // NOI18N
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });
        jPanel4.add(insertButton, java.awt.BorderLayout.EAST);

        jPanel8.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanel2.add(jPanel8, java.awt.BorderLayout.CENTER);

        allowProjectCmdCheckBox.setFont(new JLabel().getFont());
        org.openide.awt.Mnemonics.setLocalizedText(allowProjectCmdCheckBox, OStrings.getString("ALLOW_PROJECT_EXTERN_CMD")); // NOI18N
        jPanel2.add(allowProjectCmdCheckBox, java.awt.BorderLayout.SOUTH);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        org.openide.awt.Mnemonics.setLocalizedText(compileCloseCheckbox, OStrings.getString("TF_COMPILE_ON_CLOSE")); // NOI18N
        add(compileCloseCheckbox, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed

    }//GEN-LAST:event_insertButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JCheckBox allowProjectCmdCheckBox;
    javax.swing.JCheckBox compileCloseCheckbox;
    private javax.swing.JTextArea externalCmdDescriptionTextArea;
    private javax.swing.JLabel externalCommandLabel;
    private javax.swing.JScrollPane externalCommandScrollPane;
    javax.swing.JTextArea externalCommandTextArea;
    javax.swing.JButton insertButton;
    private javax.swing.JTextArea intervalDescriptionTextArea;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel minutesLabel;
    javax.swing.JSpinner minutesSpinner;
    private javax.swing.JLabel secondsLabel;
    javax.swing.JSpinner secondsSpinner;
    private javax.swing.JLabel variablesLabel;
    javax.swing.JComboBox<String> variablesList;
    // End of variables declaration//GEN-END:variables
}
