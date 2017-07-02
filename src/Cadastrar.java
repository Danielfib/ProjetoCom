import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Cadastrar extends JFrame {

	private JPanel contentPane;
	private JTextField ipCadastrar;

	/**
	 * Launch the application.
	 */
	public static void NewScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cadastrar frame = new Cadastrar();
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
	public void fecharJanela(){
	    super.dispose();
	}
	
	public Cadastrar() {
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 407, 210);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDigiteOIp = new JLabel("Digite o IP do usu\u00E1rio que voc\u00EA quer cadastrar");
		lblDigiteOIp.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDigiteOIp.setBounds(10, 11, 369, 33);
		contentPane.add(lblDigiteOIp);
		
		ipCadastrar = new JTextField();
		ipCadastrar.setBounds(10, 55, 369, 33);
		contentPane.add(ipCadastrar);
		ipCadastrar.setColumns(10);
		
		
		JButton btnCadastrar = new JButton("CADASTRAR");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ip = ipCadastrar.getText();
				if( ! ip.equals("") ){
					interfaceServer.listaCadastros.add(ip);
					fecharJanela();
				} else {
					JOptionPane.showMessageDialog(null, "Você não completou algum campo");
				}
			}
		});
		btnCadastrar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnCadastrar.setBounds(116, 116, 121, 45);
		contentPane.add(btnCadastrar);
	}

}
