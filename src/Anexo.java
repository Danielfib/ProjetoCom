import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class Anexo extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnENV;
	private JProgressBar progressBar;
	private JLabel lblRtt;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_1;

	/**
	 * Launch the application.
	 */
	public void NewScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Anexo frame = new Anexo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Anexo() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 452, 356);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblEscolhaOEndereo = new JLabel("Escolha o endereço para anexar");
		lblEscolhaOEndereo.setBounds(16, 17, 222, 21);
		contentPane.add(lblEscolhaOEndereo);
		
		JButton btnEscolher = new JButton("Escolher...");
		btnEscolher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 JFileChooser buscar = new JFileChooser();
	             buscar.showOpenDialog(btnEscolher);
	             File arquivo = buscar.getSelectedFile();
	             textField.setText(arquivo.getAbsolutePath());

			}
		});
		btnEscolher.setBounds(335, 57, 109, 41);
		contentPane.add(btnEscolher);
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setBounds(16, 59, 307, 35);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel label = new JLabel("Escreva o nome do arquivo com extensão:");
		label.setBounds(16, 106, 307, 21);
		contentPane.add(label);
		
		textField_1 = new JTextField();
		textField_1.setBounds(16, 139, 247, 35);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		btnENV = new JButton("E N V I A R");
		btnENV.setBounds(159, 191, 117, 41);
		contentPane.add(btnENV);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(28, 244, 392, 20);
		contentPane.add(progressBar);
		
		lblRtt = new JLabel("RTT");
		lblRtt.setBounds(27, 276, 61, 16);
		contentPane.add(lblRtt);
		
		label_2 = new JLabel("Número");
		label_2.setForeground(new Color(119, 136, 153));
		label_2.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		label_2.setBounds(28, 298, 61, 16);
		contentPane.add(label_2);
		
		label_3 = new JLabel("Tempo Restante");
		label_3.setBounds(177, 276, 104, 16);
		contentPane.add(label_3);
		
		label_1 = new JLabel("Número Dois");
		label_1.setForeground(new Color(119, 136, 153));
		label_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		label_1.setBounds(177, 298, 92, 16);
		contentPane.add(label_1);
	}
}
