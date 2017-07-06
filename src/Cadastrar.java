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
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Cadastrar extends JFrame {

	private JPanel contentPane;
	private JTextField ipCadastrar;
	private JTextField portaUsuario;
	private JTextField nomeUsuario;

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
		setBounds(100, 100, 406, 391);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDigiteOIp = new JLabel("Digite o IP do usu\u00E1rio que voc\u00EA quer cadastrar:");
		lblDigiteOIp.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDigiteOIp.setBounds(10, 95, 369, 33);
		contentPane.add(lblDigiteOIp);
		
		ipCadastrar = new JTextField();
		ipCadastrar.setBounds(10, 124, 369, 33);
		contentPane.add(ipCadastrar);
		ipCadastrar.setColumns(10);
		
	
		JLabel label = new JLabel("Digite a porta do usu\u00E1rio que voc\u00EA quer cadastrar:");
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(10, 166, 369, 33);
		contentPane.add(label);
		
		portaUsuario = new JTextField();
		portaUsuario.setColumns(10);
		portaUsuario.setBounds(10, 210, 369, 33);
		contentPane.add(portaUsuario);
		
		JLabel label_1 = new JLabel("Digite o nome do novo usuário:");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_1.setBounds(10, 11, 369, 33);
		contentPane.add(label_1);
		
		nomeUsuario = new JTextField();
		nomeUsuario.setColumns(10);
		nomeUsuario.setBounds(10, 55, 369, 33);
		contentPane.add(nomeUsuario);
		
		
		
		
		JButton btnCadastrar = new JButton("CADASTRAR");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ip = ipCadastrar.getText();
				String porta = portaUsuario.getText();
				String nome = nomeUsuario.getText();
				
				if(  ip.equals("") ||  nome.equals("") ||  porta.equals("") ){
					System.out.println(ip +" " + nome + " "+ porta);
				    JOptionPane.showMessageDialog(null, "Você não completou algum campo");

				} else {
					ArrayList<String> dados = new ArrayList();
					InterfaceServer.addLinhas(dados, "offline", nome, ip, porta);
					InterfaceServer.listaCadastros.add(dados);
					System.out.println(InterfaceServer.listaCadastros.toString());
					fecharJanela();
					
				}
			}
		});
		btnCadastrar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnCadastrar.setBounds(135, 280, 121, 45);
		contentPane.add(btnCadastrar);
		
		
	}
}
