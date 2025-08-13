package org.omegat.core.team2.gui;

import org.omegat.util.OStrings;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class RepositoriesCredentialsPanel extends JPanel {

    public JButton btnRemove, btnExport, btnImport, btnEdit, btnAdd;
    public Box.Filler filler1;
    public JPanel jPanel2;
    public JScrollPane jScrollPane2;
    public JTable list;

    private RepoTableModel model;
    private boolean selectAllState = false;
    private String highlightedRepo = null;

    public RepositoriesCredentialsPanel() {
        initComponents();
    }

    private void initComponents() {
        jScrollPane2 = new JScrollPane();
        jPanel2 = new JPanel();
        btnRemove = new JButton();
        btnExport = new JButton();
        btnImport = new JButton();
        btnEdit = new JButton();
        btnAdd = new JButton();
        filler1 = new Box.Filler(new Dimension(0, 0), new Dimension(0,0), new Dimension(0, 32767));

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setMinimumSize(new Dimension(600, 300));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        model = new RepoTableModel(new ArrayList<>());
        list = new JTable(model);
        list.setFillsViewportHeight(true);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableRowSorter<RepoTableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortable(1, false);
        list.setRowSorter(sorter);

        TableColumn checkboxCol = list.getColumnModel().getColumn(1);
        checkboxCol.setHeaderValue("✓");
        checkboxCol.setMaxWidth(50);
        checkboxCol.setMinWidth(30);

        list.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (list.columnAtPoint(e.getPoint()) == 1) {
                    selectAllState = !selectAllState;
                    model.setAllSelected(selectAllState);
                }
            }
        });

        list.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            int row = list.getSelectedRow();
            highlightedRepo = (row >= 0) ? (String) model.getValueAt(list.convertRowIndexToModel(row), 0) : null;
        });

        jScrollPane2.setViewportView(list);
        add(jScrollPane2);

        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.PAGE_AXIS));
        org.openide.awt.Mnemonics.setLocalizedText(btnRemove, OStrings.getString("BUTTON_REMOVE"));
        org.openide.awt.Mnemonics.setLocalizedText(btnExport, OStrings.getString("PREFS_REPO_CREDS_EXPORT"));
        org.openide.awt.Mnemonics.setLocalizedText(btnImport, OStrings.getString("PREFS_REPO_CREDS_IMPORT"));
        org.openide.awt.Mnemonics.setLocalizedText(btnEdit,   OStrings.getString("PREFS_REPO_CREDS_EDIT"));
        org.openide.awt.Mnemonics.setLocalizedText(btnAdd,    OStrings.getString("PREFS_REPO_CREDS_ADD"));

        jPanel2.add(btnRemove);
        jPanel2.add(btnExport);
        jPanel2.add(btnImport);
        jPanel2.add(btnEdit);
        jPanel2.add(btnAdd);
        jPanel2.add(filler1);
        add(jPanel2);
    }

    public void setRepositories(List<String> repos) {
        model.setRepositories(repos);
    }

    public List<String> getCheckedRepositories() {
        return model.getCheckedRepositories();
    }

    public void removeRepositories(List<String> repos) {
        model.removeRepositories(repos);
    }

    public String getHighlightedRepo() {
        return highlightedRepo;
    }

    private static class RepoTableModel extends AbstractTableModel {
        private final String[] colNames = {"Repository", "✓"};
        private List<String> repos;
        private List<Boolean> selected;

        RepoTableModel(List<String> repos) {
            setRepositories(repos);
        }

        void setRepositories(List<String> repos) {
            this.repos = new ArrayList<>(repos);
            this.selected = new ArrayList<>();
            for (int i = 0; i < repos.size(); i++) {
                selected.add(Boolean.FALSE);
            }
            fireTableDataChanged();
        }

        void setAllSelected(boolean state) {
            for (int i = 0; i < selected.size(); i++) {
                selected.set(i, state);
            }
            fireTableDataChanged();
        }

        void removeRepositories(List<String> removeList) {
            repos.removeAll(removeList);
            setRepositories(repos);
        }

        List<String> getCheckedRepositories() {
            List<String> result = new ArrayList<>();
            for (int i = 0; i < repos.size(); i++) {
                if (Boolean.TRUE.equals(selected.get(i))) {
                    result.add(repos.get(i));
                }
            }
            return result;
        }

        @Override public int getRowCount() { return repos.size(); }
        @Override public int getColumnCount() { return 2; }
        @Override public String getColumnName(int col) { return colNames[col]; }
        @Override public Class<?> getColumnClass(int col) { return (col == 0) ? String.class : Boolean.class; }
        @Override public boolean isCellEditable(int row, int col) { return col == 1; }
        @Override public Object getValueAt(int row, int col) { return (col == 0) ? repos.get(row) : selected.get(row); }
        @Override public void setValueAt(Object value, int row, int col) {
            if (col == 1 && value instanceof Boolean) {
                selected.set(row, (Boolean) value);
                fireTableCellUpdated(row, col);
            }
        }
    }
}
