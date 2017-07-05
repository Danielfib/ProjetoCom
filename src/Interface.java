
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class Interface {

	private static ArrayList<Chat> listaJanelas = new ArrayList<Chat>();
	private static ArrayList<String> listaIps = new ArrayList<String>();
	// lista encadeada com as jframes das conversas

	public static JFrame frame;
	public static JTextField ipDestinatario;
	private JTextField ipServidor;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		(new Thread(new ServidorUDP(listaJanelas, listaIps))).start();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface window = new Interface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Interface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 17));
		frame.setBounds(100, 100, 578, 443);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblIpDoDestino = new JLabel("IP do destino");
		lblIpDoDestino.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblIpDoDestino.setBounds(18, 80, 120, 49);
		frame.getContentPane().add(lblIpDoDestino);

		ipDestinatario = new JTextField();
		ipDestinatario.setColumns(10);
		ipDestinatario.setBounds(18, 140, 206, 37);
		frame.getContentPane().add(ipDestinatario);

		JButton btnConversar = new JButton("CONVERSAR");

		btnConversar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ipServidor.getText().equals("") || ipDestinatario.getText().equals("") || textField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Você não completou algum campo");
				} else {
					new ClienteUDP(ipDestinatario.getText(), Integer.parseInt(textField.getText()), listaJanelas, listaIps).start();
				}

			}
		});
		btnConversar.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnConversar.setBounds(158, 323, 156, 43);
		frame.getContentPane().add(btnConversar);

		JLabel lblNewLabel = new JLabel("Zapizapi");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 25));
		lblNewLabel.setBounds(224, 18, 120, 43);
		frame.getContentPane().add(lblNewLabel);

		JLabel label_2 = new JLabel("Digite o IP do servidor central ");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_2.setBounds(18, 188, 248, 57);
		frame.getContentPane().add(label_2);

		ipServidor = new JTextField();
		ipServidor.setColumns(10);
		ipServidor.setBounds(18, 241, 234, 37);
		frame.getContentPane().add(ipServidor);
		
		JLabel lblNewLabel_1 = new JLabel("Porta do destino");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(289, 80, 148, 34);
		frame.getContentPane().add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBounds(303, 140, 107, 37);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

	}
}