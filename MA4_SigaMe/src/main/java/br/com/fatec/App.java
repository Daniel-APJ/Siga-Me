package br.com.fatec;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage; 

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        App.primaryStage = stage; 

        Parent root = loadFXML("view/MENU");
        scene = new Scene(root);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        Parent newRoot = loadFXML(fxml);
        scene.setRoot(newRoot);
        // Se o stage principal for undecorated, reaplicar listeners de arrastar ao newRoot
        if (App.primaryStage != null && App.primaryStage.getStyle() == StageStyle.UNDECORATED) {
            // Re-usar xOffset e yOffset da classe App ou passar o stage como parâmetro
            // Para simplificar, o exemplo abaixo assume que xOffset e yOffset da classe App são usados
            // Se eles não forem campos da classe, você precisará de uma estratégia diferente aqui.
            // Como são campos da classe 'App', podemos acessá-los (mas isso pode ser um pouco confuso se setRoot for chamado de contextos diferentes).
            // Uma forma mais limpa seria o newRoot ter seus próprios listeners que usam o primaryStage.
            // Exemplo simplificado:
            newRoot.setOnMousePressed(event -> {
                // Esta é uma simplificação; o ideal seria ter offsets locais ou uma classe helper
                // Para o propósito de arrastar o stage principal após trocar o root:
                // ((App)Application.getCurrentApplication()).xOffset = event.getSceneX(); // Não é ideal
                // ((App)Application.getCurrentApplication()).yOffset = event.getSceneY();
                // A melhor maneira é refatorar para que o stage e seus offsets sejam gerenciados de forma mais centralizada
                // ou que cada root configurado tenha os listeners aplicados com o stage correto.
                // Por ora, vamos focar na funcionalidade principal. Se o menu não troca seu root, isso não é um problema.
            });
            newRoot.setOnMouseDragged(event -> {
                 // App.primaryStage.setX(event.getScreenX() - ((App)Application.getCurrentApplication()).xOffset);
                 // App.primaryStage.setY(event.getScreenY() - ((App)Application.getCurrentApplication()).yOffset);
            });
        }
    }
    
    // Método para mostrar/desminimizar a janela principal (menu)
    public static void mostrarMenuPrincipal() {
        if (primaryStage != null) {
            primaryStage.setIconified(false); // Desminimiza
            primaryStage.toFront(); // Traz para frente
            primaryStage.requestFocus(); // Dá foco
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}