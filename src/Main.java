import ui.JanelaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Classe principal que inicializa a aplicação gráfica na Event Dispatch Thread
 * (EDT).
 */
public class Main {
    /**
     * Método principal de inicialização do software.
     * 
     * @param args Argumentos de linha de comando.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            JanelaPrincipal janela = new JanelaPrincipal();
            janela.setVisible(true);
        });
    }
}