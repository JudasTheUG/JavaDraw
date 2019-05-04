package paint;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;


public class Paint extends Application {

    final static int CANVAS_WIDTH = 1100;
    final static int CANVAS_HEIGHT = 570;

    ColorPicker renkSecici;
    
     public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(final Stage primaryStage) {

        final Canvas Kanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        final GraphicsContext graphicsContext = Kanvas.getGraphicsContext2D();
       

        Draw(graphicsContext);



        Kanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    graphicsContext.beginPath(); //Çizimin başlangıcı
                    graphicsContext.moveTo(event.getX(), event.getY());
                    graphicsContext.setStroke(renkSecici.getValue());
                    graphicsContext.stroke(); //Fırça darbesi
                });

        Kanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                event -> {
                    graphicsContext.lineTo(event.getX(), event.getY());
                    graphicsContext.setStroke(renkSecici.getValue());
                    graphicsContext.stroke();
                });

        Kanvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                event -> {

                });

        Group kok = new Group();

        Button btnKaydet = new Button("Kaydet");
        btnKaydet.setOnAction(t -> {
            FileChooser fileChooser = new FileChooser();

            
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png"); //kayıt türünü sınırlandırıyor.
            fileChooser.getExtensionFilters().add(extFilter);

           
            File file = fileChooser.showSaveDialog(primaryStage);

            if(file != null){
                try {
                    WritableImage resim = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT); // Resmin boyutunu belirle
                    Kanvas.snapshot(null, resim); // KAnvasın görüntüsünü al
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(resim, null); //Resmi işle
                    ImageIO.write(renderedImage, "png", file); // Resimi .png uzantısıyla kaydet
                } catch (IOException ex) {


                }
            }
        });

        Button btnTemizle = new Button("Ekranı Temizle");
        btnTemizle.setOnAction(t-> {
            graphicsContext.clearRect(0,0,1100,600);
            
        });


        Slider kaydırak = new Slider();
        kaydırak.setMin(0);
        kaydırak.setMax(100);
        kaydırak.setValue(1);
        kaydırak.setMinorTickCount(5);
        kaydırak.setBlockIncrement(10);

        kaydırak.valueProperty().addListener((ov, old_val, new_val) -> {
            kaydırak.setValue(new_val.intValue());
            graphicsContext.setLineWidth(kaydırak.getValue()/10);
        });
        
        HBox hb = new HBox();   //Yatay Sıralama
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(renkSecici, btnKaydet,btnTemizle,kaydırak);

        VBox vBox = new VBox();   //Dikey Sıralama
        vBox.getChildren().addAll(Kanvas,hb);
        kok.getChildren().add(vBox);
        Scene scene = new Scene(kok, 1000, 600);

        primaryStage.setTitle("Çizim Programı");
        primaryStage.setResizable(false);// Yeniden boyutlandırmayı kapatıyoruz
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void Draw(GraphicsContext gc){

        renkSecici = new ColorPicker(Color.BLACK);
        gc.setFill(renkSecici.getValue());
        gc.setStroke(renkSecici.getValue());
        gc.setLineWidth(1);
    }
}