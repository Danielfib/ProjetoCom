import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JProgressBar;

public class Chat extends JFrame {

	private JPanel contentPane;
	private JTextField Escrito;
	private String ipDestino;
	private int portDestino;
	private int numSeq;
	private static JProgressBar progressBar = new JProgressBar();
	private static JLabel rtt = new JLabel("RTT (ms): ");

	JTextArea textArea = new JTextArea();

	private String msgTela = "";

	String mensagem1, Outras;
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy 'uma' EE 'as' hh:mm:ss, ");
	GDPClient gdp;

	/**
	 * Launch the application.
	 */
	public void NewScreen(Chat frame) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
					gdp = new GDPClient(ipDestino, portDestino);
					RttSend Sender = new RttSend(ipDestino);
					RttReceive Receiver = new RttReceive("localhost");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Chat(String ipDestino, int portDestino) {
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

		this.ipDestino = ipDestino;
		this.portDestino = portDestino;
		this.numSeq = 0;

		// JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);

		JButton BtnEnviar = new JButton("ENVIAR");
		BtnEnviar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				Pacote p = new Pacote(0, portDestino, numSeq, 0, false, false, false, false, false, 0, 0,
						Escrito.getText().getBytes());
				numSeq += 1000;
				gdp.listPacotes.add(p);

				msgTela += sdf.format(cal.getTime()) + " " + "Você disse: " + Escrito.getText() + "\n";
				textArea.setText(msgTela);

				Escrito.setText("");
				Escrito.requestFocus();

			}
		});

		BtnEnviar.setFont(new Font("Tahoma", Font.BOLD, 17));
		BtnEnviar.setBounds(500, 410, 161, 25);
		contentPane.add(BtnEnviar);

		JButton btnAnexo = new JButton("ANEXO");
		btnAnexo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser buscar = new JFileChooser();
				buscar.showOpenDialog(btnAnexo);
				File file = buscar.getSelectedFile();
				Escrito.setText(file.getAbsolutePath());
			}
		});
		btnAnexo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAnexo.setBounds(570, 19, 97, 25);
		contentPane.add(btnAnexo);

		JLabel lblNewLabel = new JLabel("Você esta conversando com: " + ipDestino);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 19, 355, 25);
		contentPane.add(lblNewLabel);
		
		progressBar.setBounds(500, 390, 161, 14);
		contentPane.add(progressBar);
		
		rtt.setBounds(380, 26, 119, 14);
		contentPane.add(rtt);

	}

	public static void setRTT(long rttN){
		rtt.setText("RTT (ms): " + rttN);
	}
	
	public static void setProgress(int n){
		progressBar.setValue(n);
	}
	
	public void addText(String msg) {
		msgTela += sdf.format(cal.getTime()) + " " + ipDestino + " disse: " + msg;
		textArea.setText(msgTela);
	}
}