
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
		frame.setBounds(100, 100, 516, 437);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel label = new JLabel("Digite aqui o IP do amigo que quer conversar ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label.setBounds(18, 79, 570, 57);
		frame.getContentPane().add(label);

		ipDestinatario = new JTextField();
		ipDestinatario.setColumns(10);
		ipDestinatario.setBounds(18, 140, 234, 37);
		frame.getContentPane().add(ipDestinatario);

		JButton btnConversar = new JButton("CONVERSAR");

		btnConversar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ipServidor.getText().equals("") || ipDestinatario.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Você não completou algum campo");
				} else {
					/*
					 * Chat novo = new Chat(ipDestinatario.getText(), 2021 ,
					 * ipServidor.getText()); novo.NewScreen();
					 */
					ClienteUDP client = new ClienteUDP(ipDestinatario.getText(), 2021);
					client.startConection();
					client.start();
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
		label_2.setBounds(18, 188, 570, 57);
		frame.getContentPane().add(label_2);

		ipServidor = new JTextField();
		ipServidor.setColumns(10);
		ipServidor.setBounds(18, 241, 234, 37);
		frame.getContentPane().add(ipServidor);

	}
}