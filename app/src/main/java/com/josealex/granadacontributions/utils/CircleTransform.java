package com.josealex.granadacontributions.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;


/**
 * Gracias a julianshen por compartir esta clase, gracias a ella pude realizar el redondeado de la imagen
 * del usuario en la StartActivity.
 *
 * para que vea que no copio sin saber que copio, le he comentado toda la clase :)
 *
 * PD: no tiene nada que ver con el juego, era solo para darle un diseño al menu principal
 *
 * Author: julianshen
 * Github: https://gist.github.com/julianshen
 * Recurso: https://gist.github.com/julianshen/5829333
 * TODO(decidir que hacer con cicleTransform)
 */
public class CircleTransform implements Transformation
{
    //TODO(@Override)
    public Bitmap transform(Bitmap source) {
        /*
         * lo que se consigue con esta clase es "recoger" el bitmap y "cortarle" los bordes,
         * dejandolo circular, pero realmente no se "cortan" los bordes, sino que se crea un nuevo
         * bitmap, se le da forma circular, y luego pintamos encima el bitmap pincipal.
         */

        //recogemos la anchura minima de la imagen (ya sea el ancho o el alto)
        int size = Math.min(source.getWidth(), source.getHeight());

        /* Ejemplo:
         * una imagen de :
         *  width = 1000 px
         *  height = 800 px
         * por lo tanto :
         *  size = 800 px
         *
         * operacion :
         *  x = 200/2 = 100 px
         *  y = 0/2   = 0 px
         */
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // Ahora que hemos calculado la x e y de nuestra imagen con tamano 800x800
        // creamos el bitmap de la imagen SOURCE pero recortandola para recorger el 800x800 central
        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);

        // A este if solo entrara cuando resulta que la imagen NO tenia el mismo ancho que alto,
        // por ejemplo:
        //     1000x1000px = false
        //     1000x800px = true
        if (squaredBitmap != source) {
            //en el caso de ser una imagen NO CUADRADA, reciclamos el anterior bitmap, porque
            //no nos vale, al no ser cuadrado.
            source.recycle();
        }

        // Creamos un bitmap con la configuracion del recurso anterior, en caso de
        // ser una imagen NO CUADRADA, la configuracion de ve afectada
        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        //creamos el canvas con el que cambiaremos nuestro bitmap
        Canvas canvas = new Canvas(bitmap);
        //la pintura/brocha que usaremos.
        Paint paint = new Paint();
        /*
         * Este es el paso mas importante, ahora nos crearemos el shader, que lo que hara sera copiar
         * el bitmap y convertirlo en algo asi como la pintura, de manera que podamos pintar todos los objetos
         * que queramos con la misma textura que el bitmap.
         */
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        //agregamos a la pintura/brocha el shader y el antiAlias
        paint.setShader(shader);
        paint.setAntiAlias(true);

        //dibujamos el CIRCULO, al cual le aplicamos nuestra pintura/brocha con nuestro SHADER
        //y con esto se termina, ya creamos nuestro bitmap circular
        float r = size/2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    //TODO(@Override)
    public String key() {
        return "circle";
    }
}