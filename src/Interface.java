import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Interface {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		frame.setBounds(100, 100, 632, 562);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblDigiteAquiO = new JLabel("Digite aqui o IP do servidor que voc\u00EA quer se conectar ");
		lblDigiteAquiO.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDigiteAquiO.setBounds(44, 73, 570, 57);
		frame.getContentPane().add(lblDigiteAquiO);
		
		JLabel label = new JLabel("Digite aqui o IP do amigo que quer conversar ");
		label.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label.setBounds(44, 198, 570, 57);
		frame.getContentPane().add(label);
		
		textField = new JTextField();
		textField.setBounds(44, 148, 234, 37);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(44, 255, 234, 37);
		frame.getContentPane().add(textField_1);
		
		JButton btnConversar = new JButton("CONVERSAR");
		btnConversar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Chat novo = new Chat();
				novo.NewScreen();
			}
		});
		btnConversar.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnConversar.setBounds(229, 413, 192, 43);
		frame.getContentPane().add(btnConversar);
		
		JLabel lblNewLabel = new JLabel("Zapizapi");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 27));
		lblNewLabel.setBounds(263, 17, 120, 43);
		frame.getContentPane().add(lblNewLabel);
	}
}
