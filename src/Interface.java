
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Interface {

	private static ArrayList<JFrame> listaJanelas = new ArrayList<JFrame>();
	private static ArrayList<String> listaIps = new ArrayList<String>();
	//lista encadeada com as jframes das conversas
	
	public static JFrame frame;
	public static JTextField ipServidor;
	public static JTextField ipDestinatario;
	public static JTextField portaDestinario;

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
		frame.setBounds(100, 100, 604, 528);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblDigiteAquiO = new JLabel("Digite aqui o IP do servidor que voc\u00EA quer se conectar ");
		lblDigiteAquiO.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDigiteAquiO.setBounds(44, 73, 570, 57);
		frame.getContentPane().add(lblDigiteAquiO);

		JLabel label = new JLabel("Digite aqui o IP do amigo que quer conversar ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label.setBounds(44, 186, 570, 57);
		frame.getContentPane().add(label);

		ipServidor = new JTextField();
		ipServidor.setBounds(44, 137, 234, 37);
		frame.getContentPane().add(ipServidor);
		ipServidor.setColumns(10);

		ipDestinatario = new JTextField();
		ipDestinatario.setColumns(10);
		ipDestinatario.setBounds(44, 247, 234, 37);
		frame.getContentPane().add(ipDestinatario);

		portaDestinario = new JTextField();
		portaDestinario.setColumns(10);
		portaDestinario.setBounds(44, 356, 234, 37);
		frame.getContentPane().add(portaDestinario);

		JButton btnConversar = new JButton("CONVERSAR");
		btnConversar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Chat novo = new Chat(ipDestinatario.getText(), Integer.parseInt(portaDestinario.getText()) , ipServidor.getText());
				novo.NewScreen();
			}
		});
		btnConversar.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnConversar.setBounds(201, 423, 192, 43);
		frame.getContentPane().add(btnConversar);

		JLabel lblNewLabel = new JLabel("Zapizapi");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 25));
		lblNewLabel.setBounds(224, 18, 120, 43);
		frame.getContentPane().add(lblNewLabel);


		JLabel label_1 = new JLabel("Digite aqui a porta de acesso do seu amigo");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_1.setBounds(44, 295, 570, 57);
		frame.getContentPane().add(label_1);
	}
}