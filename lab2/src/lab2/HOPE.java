package lab2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class HOPE extends JFrame{
    private DefaultTableModel tableModel;
    private JTable table1;

    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton add;
    private JButton remove;
    private JButton calc;
    private JButton clear;
    private JButton fill;

    private double E = 2.7182818284590452353602874713527;
    private ArrayList<RecIntegral> ne_chet = new ArrayList<>();
    private LinkedList<RecIntegral> chet = new LinkedList<>();

    Object[] columnNames = {"нижняя граница интегрирования",
"верхняя граница интегрирования",
"шаг интегрирования",
"результат вычисления"};

    public HOPE(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        CreateTextModel();
        CreateTextField();
        CreateButton();

        CreateForm();
    }

    private void CreateTextModel(){
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);

        table1 = new JTable(tableModel);
    }

    private void CreateTextField(){
        textField1 = new JTextField();
        textField1.setPreferredSize(new Dimension(200, 30));
        textField2 = new JTextField();
        textField2.setPreferredSize(new Dimension(200, 30));
        textField3 = new JTextField();
        textField3.setPreferredSize(new Dimension(200, 30));
    }

    private void CreateButton(){
        add = new JButton("Добавить");
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = table1.getSelectedRow();
                System.out.println(idx);

                if (ne_chet.size() >= chet.size()){
                    chet.add(new RecIntegral(textField1.getText(),
                            textField2.getText(), textField3.getText()));

                    tableModel.insertRow(tableModel.getRowCount(), new String[] {chet.getLast().start,
                            chet.getLast().end, chet.getLast().step, chet.getLast().result});
                }else{
                    ne_chet.add(new RecIntegral(textField1.getText(),
                            textField2.getText(), textField3.getText()));

                    tableModel.insertRow(tableModel.getRowCount(), new String[] {ne_chet.get(ne_chet.size() - 1).start,
                            ne_chet.get(ne_chet.size() - 1).end, ne_chet.get(ne_chet.size() - 1).step,
                            ne_chet.get(ne_chet.size() - 1).result});
                }
            }
        });

        remove = new JButton("Удалить");
        remove.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                int idx = table1.getSelectedRow();
                if(idx == -1) {
                    return;
                }
                tableModel.removeRow(idx);
                if(idx % 2 == 0){
                    chet.remove(idx / 2);
                }else{
                    ne_chet.remove(idx / 2);
                }
            }
        });

        calc = new JButton("Вычислить");
        calc.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                int idx = table1.getSelectedRow();
                if(idx == -1){
                    return;
                }
                double[] data;
                try {
                    data = new double[]{Double.valueOf((String) tableModel.getValueAt(idx, 0)),
                            Double.valueOf((String) tableModel.getValueAt(idx, 1)),
                            Double.valueOf((String) tableModel.getValueAt(idx, 2))};
                }catch(Throwable t){
                    tableModel.setValueAt("NULL", idx, 3);
                    return;
                }
                double start, end, step, result;
                System.out.println();
                start = data[0];
                end = data[1];
                step = data[2];
                result = 0.0;

                while(start < end){
                    if(start + step > end){
                        step = end - start;
                    }
                    result += 0.5 * (Math.pow(E, -start) + Math.pow(E, -(start + step))) * step;
                    start += step;
                }
                tableModel.setValueAt(result, idx, 3);
            }
        });

        clear = new JButton("Очистить");
        clear.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                while(tableModel.getRowCount() > 0){
                    tableModel.removeRow(0);
                }
            }
        });

        fill = new JButton("Заполнить");
        fill.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                int i = 0;
                while(i <= chet.size() + ne_chet.size()){
                    if(i % 2 == 0) {
                        if ((i / 2) < chet.size()) {
                            tableModel.insertRow(i, new String[]{chet.get(i / 2).start,
                                    chet.get(i / 2).end, chet.get(i / 2).step, chet.get(i / 2).result});
                        }
                    }else {
                        if((i / 2) < ne_chet.size() ) {
                            tableModel.insertRow(i, new String[]{ne_chet.get(i / 2).start,
                                    ne_chet.get(i / 2).end, ne_chet.get(i / 2).step,
                                    ne_chet.get(i / 2).result});
                        }
                    }
                    i++;
                }
            }
        });
    }

    private void CreateForm(){
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(table1));

        JPanel text = new JPanel();
        text.add(textField1);
        text.add(textField2);
        text.add(textField3);
        contents.add(text);

        JPanel button = new JPanel();
        button.add(add);
        button.add(remove);
        button.add(calc);
        button.add(clear);
        button.add(fill);
        contents.add(button);

        getContentPane().add(contents);

        setSize(800, 600);
        setVisible(true);
    }
}

