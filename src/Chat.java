import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class Chat extends JFrame {

	private JPanel contentPane;
	private JTextField Escrito;

	
	// criando variaveis importantes para a aplicação
	
	String mensagem1, Outras;
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy 'uma' EE 'às' mm:ss:zz ' Mateus diz: ' ");
    
	/**
	 * Launch the application.
	 */
	public static void NewScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat frame = new Chat();
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
	public Chat() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 697, 533);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		
		Escrito = new JTextField();
		Escrito.setBounds(12, 436, 459, 45);
		contentPane.add(Escrito);
		Escrito.setColumns(10);
		
		
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(12, 61, 655, 359);
		contentPane.add(textArea);
		
		JButton BtnEnviar = new JButton("ENVIAR");
		BtnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int contador = 0 ;
				if( contador == 0){
					contador++;
					mensagem1 = Escrito.getText();
					textArea.setText(mensagem1);	
				}else{
					 Outras = Escrito.getText();
	                 textArea.setText(textArea.getText() + '\n' + sdf.format(cal.getTime()) + " " + Outras);
				}
			}
		});
		
		BtnEnviar.setFont(new Font("Tahoma", Font.BOLD, 17));
		BtnEnviar.setBounds(498, 436, 169, 41);
		contentPane.add(BtnEnviar);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(12, 14, 325, 34);
		contentPane.add(lblNewLabel);
		
		JButton btnAnexo = new JButton("ANEXO");
		btnAnexo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAnexo.setBounds(570, 19, 97, 25);
		contentPane.add(btnAnexo);
	}
}
