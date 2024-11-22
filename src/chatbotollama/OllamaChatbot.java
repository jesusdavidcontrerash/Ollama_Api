package chatbotollama;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;

public class OllamaChatbot extends javax.swing.JFrame {

    private int CantidadChats = 1, ChatSeleccionado = 0;
    private final ArrayList<JButton> BotonChat = new ArrayList<>();
    private ArrayList<String> MensajeChat = new ArrayList<>();

    public OllamaChatbot() {
        initComponents();

        setTitle("Ollama Chatbot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MensajeChat.add("");

        AreaTexto.setEditable(false);  
        AreaTexto.setLineWrap(true);   
        AreaTexto.setWrapStyleWord(true);

        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
        Chat1.setAlignmentX(Component.CENTER_ALIGNMENT);
        Chat1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChatSeleccionado = 0; // El índice del primer chat es 0
                ActualizarHistorial();
            }
        });
    }

    private String RespuestaOllama(String mensaje) {

        String urlString = "http://localhost:11434/api/chat";
        String jsonInputString = "{\"model\": \"llama3.2:1b\", \"stream\": false, \"messages\": [ { \"role\": \"user\", \"content\": \" Responde en español: " + mensaje + " \" } ] }"; // Solicitud

        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try ( OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;
            StringBuilder respuesta = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                respuesta.append(inputLine);
            }
            in.close();

            return CapturarRespuesta(respuesta.toString());

        } catch (Exception e) {
            System.out.println(e);
            return "Error al conectar con el chatbot.";
        }
    }

    private String CapturarRespuesta(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
            return "La respuesta del chatbot está vacía o es nula.";
        }

        try {
            System.out.println(jsonResponse);

            // Extraer el contenido de la respuesta usando la clave "content"
            String searchKey = "\"content\":\"";
            int startIndex = jsonResponse.indexOf(searchKey) + searchKey.length();
            int endIndex = jsonResponse.indexOf("\"},\"done_reason", startIndex);

            if (startIndex != -1 && endIndex != -1) {
                return jsonResponse.substring(startIndex, endIndex);
            } else {
                return "No se encontró el contenido en la respuesta.";
            }

        } catch (StringIndexOutOfBoundsException e) {
            // Manejo de errores de índices incorrectos
            System.err.println("Error de índices en la cadena: " + e.getMessage());
            return "Error al extraer datos de la respuesta del chatbot.";

        } catch (Exception e) {
            // Captura general para otros errores inesperados
            System.err.println("Error inesperado: " + e.getMessage());
            return "Error inesperado al procesar la respuesta.";
        }
    }

    private void EnvioPregunta() {
        String userText = AreaMensaje.getText();
        AreaTexto.append("Tú: " + userText + "\n");
        MensajeChat.set(ChatSeleccionado, MensajeChat.get(ChatSeleccionado) + "Tú: " + userText + "\n");

        AreaMensaje.setText("");

        String botResponse = RespuestaOllama(userText); // Subrutina de API
        AreaTexto.append("Bot: " + botResponse + "\n");
        MensajeChat.set(ChatSeleccionado, MensajeChat.get(ChatSeleccionado) + "Bot: " + botResponse + "\n");
    }

    private void ActualizarHistorial() {
        AreaTexto.setText(MensajeChat.get(ChatSeleccionado));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        AreaTexto = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        Chat1 = new javax.swing.JButton();
        Enviar = new javax.swing.JButton();
        Nuevo = new javax.swing.JButton();
        AreaMensaje = new javax.swing.JTextField();
        Vaciar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        AreaTexto.setColumns(20);
        AreaTexto.setRows(5);
        jScrollPane1.setViewportView(AreaTexto);

        Chat1.setText("Chat 1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Chat1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Chat1)
                .addContainerGap(299, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel1);

        Enviar.setText("Enviar");
        Enviar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EnviarMouseClicked(evt);
            }
        });

        Nuevo.setText("Nuevo chat");
        Nuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NuevoMouseClicked(evt);
            }
        });

        Vaciar.setText("Vaciar chat");
        Vaciar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VaciarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Vaciar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(AreaMensaje)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Nuevo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Enviar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Vaciar)
                    .addComponent(Nuevo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(AreaMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Enviar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NuevoMouseClicked
        JButton nuevoChatButton = new JButton("Chat " + (CantidadChats + 1));
        MensajeChat.add("");
        jPanel1.add(Box.createRigidArea(new Dimension(0, 5)));

        final int newChatIndex = CantidadChats;

        nuevoChatButton.setFocusPainted(false);
        nuevoChatButton.setOpaque(true);

        Dimension buttonSize = Chat1.getPreferredSize();
        nuevoChatButton.setMinimumSize(buttonSize);
        nuevoChatButton.setPreferredSize(buttonSize);
        nuevoChatButton.setMaximumSize(buttonSize);

        nuevoChatButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nuevoChatButton.setPreferredSize(Chat1.getPreferredSize());

        nuevoChatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChatSeleccionado = newChatIndex;
                AreaTexto.setText(MensajeChat.get(ChatSeleccionado));
            }
        });
        CantidadChats++;

        BotonChat.add(nuevoChatButton);
        jPanel1.add(nuevoChatButton, jPanel1.getComponentCount());
        jPanel1.add(Box.createRigidArea(new Dimension(0, 5)));

        jPanel1.revalidate();
        jPanel1.repaint();
    }//GEN-LAST:event_NuevoMouseClicked

    private void EnviarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EnviarMouseClicked
        EnvioPregunta();
    }//GEN-LAST:event_EnviarMouseClicked

    private void VaciarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VaciarMouseClicked
        AreaTexto.setText("");
        MensajeChat.set(ChatSeleccionado, "");
    }//GEN-LAST:event_VaciarMouseClicked

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OllamaChatbot().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AreaMensaje;
    private javax.swing.JTextArea AreaTexto;
    private javax.swing.JButton Chat1;
    private javax.swing.JButton Enviar;
    private javax.swing.JButton Nuevo;
    private javax.swing.JButton Vaciar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
