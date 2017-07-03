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
import javax.swing.JScrollPane;

public class Chat extends JFrame {

	private JPanel contentPane;
	private JTextField Escrito;
	String ipDestino;

	JTextArea textArea = new JTextArea();

	private String msgTela = "";

	// criando variaveis importantes para a aplicação

	String mensagem1, Outras;
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy 'uma' EE 'as' hh:mm:ss ' Voce diz: ' ");

	/**
	 * Launch the application.
	 */
	public void NewScreen() {
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
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 697, 489);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Escrito = new JTextField();
		Escrito.setBounds(6, 397, 459, 45);
		contentPane.add(Escrito);
		Escrito.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 56, 655, 323);
		contentPane.add(scrollPane);

		// JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		JButton BtnEnviar = new JButton("ENVIAR");
		BtnEnviar.addActionListener(new ActionListener() {
			int contador = 0;

			public void actionPerformed(ActionEvent arg0) {

				if (contador == 0) {
					contador++;
					mensagem1 = Escrito.getText();
					msgTela = sdf.format(cal.getTime()) + " " + mensagem1;
					textArea.setText(msgTela);
				} else {
					msgTela = msgTela + '\n' + sdf.format(cal.getTime()) + " " + Escrito.getText();
					textArea.setText(msgTela);
				}
			}
		});

		BtnEnviar.setFont(new Font("Tahoma", Font.BOLD, 17));
		BtnEnviar.setBounds(500, 401, 161, 34);
		contentPane.add(BtnEnviar);

		JButton btnAnexo = new JButton("ANEXO");
		btnAnexo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Anexo anexo = new Anexo();
				anexo.NewScreen();
			}
		});
		btnAnexo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAnexo.setBounds(570, 19, 97, 25);
		contentPane.add(btnAnexo);

		JLabel lblNewLabel = new JLabel("Voc� esta conversando com: " + ipDestino);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 19, 355, 25);
		contentPane.add(lblNewLabel);

	}
	

	public void addText(String msg) {
		msgTela = msgTela + '\n' + sdf.format(cal.getTime()) + " " + msg;
		textArea.setText(msgTela);
	}
}