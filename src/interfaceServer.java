
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.JScrollPane;

public class interfaceServer extends JFrame {

	private static JPanel contentPane;

	public static ArrayList<String> titulos = new ArrayList<String>();
	public static ArrayList<ArrayList<String>> listaCadastros = new ArrayList<ArrayList<String>>();
	private static ArrayList<String> teste = new ArrayList();

	ArrayList<String[]> rowList = new ArrayList<String[]>();
	public static JTable table;

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

		// aqui vai ficar a inserção dos usuários na tabela

		addLinhas(titulos, "Status", "Nome", "Ip", "Porta");
		addLinhas(teste, "online", "Mateus", "10.10.10", "2020");
		listaCadastros.add(teste);
		System.out.println(titulos.toString());

		Object[] tempTitulos = titulos.toArray();
		String[][] tempTabela = new String[listaCadastros.size()][];
		int i = 0;
		for (List<String> next : listaCadastros) {
			tempTabela[i++] = next.toArray(new String[next.size()]);
		}

		criarTabela(tempTabela, tempTitulos);
		Timer t = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update(tempTabela, tempTitulos);
				
			}
		});
		t.start();

	}

	public static void addLinhas(ArrayList<String> titulos, String linha1, String linha2, String linha3,
			String linha4) {

		titulos.add(linha1);
		titulos.add(linha2);
		titulos.add(linha3);
		titulos.add(linha4);
	}

	public static void criarTabela(Object[][] objeto1, Object[] objeto2) {
		table = new JTable(objeto1, objeto2);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 65, 548, 265);
		contentPane.add(scrollPane_1);
		scrollPane_1.setViewportView(table);
		table.setForeground(Color.DARK_GRAY);
		table.setBackground(Color.WHITE);
	}

	public static void update(Object[][] objeto1, Object[] objeto2) {
		Object[] tempTitulos = titulos.toArray();
		String[][] tempTabela = new String[listaCadastros.size()][];
		int i = 0;
		for (List<String> next : listaCadastros) {
			tempTabela[i++] = next.toArray(new String[next.size()]);
		}
		criarTabela(tempTabela, tempTitulos);

	}

}

