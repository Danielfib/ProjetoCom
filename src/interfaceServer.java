import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.JScrollPane;

public class interfaceServer extends JFrame {

	private JPanel contentPane;
	public static ArrayList<String> listaCadastros = new ArrayList<String>();
	public static ArrayList<String> listaStatus = new ArrayList<String>();
	public JTable table;
	public static Object[][] data = new Object[10][2];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					interfaceServer frame = new interfaceServer();
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
	public interfaceServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 602, 454);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton Refresh = new JButton("Refresh");
		Refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nova = "nova";
				listaCadastros.add(nova);

			}
		});
		Refresh.setBounds(487, 17, 89, 23);
		contentPane.add(Refresh);

		JLabel lblEssesSoOs = new JLabel("Esses s\u00E3o os usuarios cadastrados e as condi\u00E7\u00F5es deles:");
		lblEssesSoOs.setBounds(10, 21, 313, 14);
		contentPane.add(lblEssesSoOs);

		JButton btnCadastrarUsurio = new JButton("Cadastrar Usu\u00E1rio");
		btnCadastrarUsurio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cadastrar cadastrar = new Cadastrar();
				cadastrar.NewScreen();
			}
		});
		btnCadastrarUsurio.setBounds(347, 17, 130, 23);
		contentPane.add(btnCadastrarUsurio);

		String[] legenda = { "Status","Nome", "IP","Porta" };
		//ArrayList<>
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 65, 548, 265);
		contentPane.add(scrollPane_1);
		//table = new JTable(dados, legenda);
		scrollPane_1.setViewportView(table);
		table.setForeground(Color.DARK_GRAY);
		table.setBackground(Color.WHITE);

	}
}
