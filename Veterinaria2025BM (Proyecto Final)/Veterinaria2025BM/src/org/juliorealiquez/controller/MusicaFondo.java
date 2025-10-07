
// Realizado por: Julio Gabriel Realiquez Noriega - 2021549 - IN5BM
package org.juliorealiquez.controller;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicaFondo {

    private static MediaPlayer mediaPlayer;
    private static String cancionActual = "";

    public static void reproducirInicio() {
        reproducirCancion("org/juliorealiquez/resource/Title Screen - Super Mario Bros. Wonder OST.mp3");
    }
    
    public static void reproducirRegistro() {
        reproducirCancion("org/juliorealiquez/resource/Qumu - Athletic Theme (From _Super Mario Bros. 3_) (Cover Version).mp3");
    }
    
    public static void reproducirFondo() {
        reproducirCancion("org/juliorealiquez/resource/Qumu - Grassland Theme (From _Super Mario Bros. Wonder_) (Cover Version).mp3");
    }

    private static void reproducirCancion(String rutaRelativa) {
        try {
            // Solo cambiar la música si es distinta a la actual
            if (cancionActual.equals(rutaRelativa) && mediaPlayer != null) {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED ||
                    mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                    mediaPlayer.play();
                }
                return;
            }

            // Detener cualquier canción anterior
            detener();

            String ruta = MusicaFondo.class.getClassLoader().getResource(rutaRelativa).toExternalForm();
            if (ruta != null) {
                Media media = new Media(ruta);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Repetir
                mediaPlayer.setVolume(0.5);
                mediaPlayer.play();
                cancionActual = rutaRelativa;
            } else {
                System.err.println("No se encontró el archivo de música: " + rutaRelativa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pausar() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    public static void detener() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose(); // Libera recursos
            mediaPlayer = null;
            cancionActual = "";
        }
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}