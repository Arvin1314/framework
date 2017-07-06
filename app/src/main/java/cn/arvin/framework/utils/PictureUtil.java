package cn.arvin.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.opengl.GLES10;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;


/**
 * 处理图片相关工具类
 *
 * @author WWN
 */
public class PictureUtil {

    private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;

    private PictureUtil() {
    }

    /**
     * 获取系统允许绘制的Bitmap最大尺寸
     *
     * @return
     */
    public static int getMaxTextureSize() {
        int[] maxTextureSize = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
        return Math.max(maxTextureSize[0], DEFAULT_MAX_BITMAP_DIMENSION);
    }

    /**
     * 截取应用程序界面
     *
     * @param activity 界面Activity
     * @return Bitmap对象
     */
    public static Bitmap takeFullScreenShot(Activity activity) {

        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);

        Bitmap bmp = view.getDrawingCache();

        // Bitmap bmp2 = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);

        // view.draw(new Canvas(bmp2));

        // bmp就是截取的图片了，可通过bmp.compress(CompressFormat.PNG, 100, new
        // FileOutputStream(file));把图片保存为文件。

        // 1、得到状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        // 2、得到标题栏高度
        int wintop = activity.getWindow().findViewById(android.R.id.content).getTop();
        int titleBarHeight = wintop - statusBarHeight;

        // //把两个bitmap合到一起
        // Bitmap bmpall=Biatmap.createBitmap(width,height,Config.ARGB_8888);
        // Canvas canvas=new Canvas(bmpall);
        // canvas.drawBitmap(bmp1,x,y,paint);
        // canvas.drawBitmap(bmp2,x,y,paint);

        view.destroyDrawingCache();
        return bmp;
    }

    /**
     * 将byte数组转成 bitmap
     *
     * @param bytes
     * @return
     */
    public static Bitmap bytesToBitmap(byte[] bytes) {
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    /**
     * 将bitmap转成 byte数组
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 把bitmap转换成String(Base64加密)
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {

        // Bitmap bm = getSmallBitmap(filePath);
        byte[] bytes = bitmapToByteArray(bitmap);

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * Drawable转成Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                    : Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    /**
     * 根据图片的字节数据，在指定的路径下生成图片(可以将拍照后返回的字节数据或者压缩后的图片字节保存)
     *
     * @param bytes
     * @param filepath
     */
    public static boolean saveImageBytes(byte[] bytes, String filepath) {
        BufferedOutputStream stream = null;
        FileOutputStream fStream = null;
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            fStream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fStream);
            stream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fStream != null) {
                try {
                    fStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    /**
     * 压缩bitmap到指定的尺寸(bitmap格式是byte数组，适合用于拍照返回的数据)
     *
     * @param data 需要缩放的图片bytes
     * @param newW 需要缩放成的图片宽度
     * @param newH 需要缩放成的图片高度
     * @return 缩放后的图片
     */
    public static Bitmap makeSmallerBitmap(byte[] data, int newW, int newH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, newW, newH);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }


    /**
     * 根据路径获得图片,并压缩返回bitmap用于显示,该方法不会影响原图片的大小，只是压缩了图片
     * 将图片从本地读到内存时,进行压缩 ,即图片从File形式变为Bitmap形式
     * 特点: 通过设置采样率, 减少图片的像素, 达到对内存中的Bitmap进行压缩
     *
     * @param filePath
     * @return
     */
    public static Bitmap makeSmallerBitmap(String filePath, int newW, int newH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, newW, newH);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        LogUtil.d("inSampleSize---->" + options.inSampleSize);

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            reqWidth = reqWidth == 0 ? 1 : reqWidth;
            reqHeight = reqHeight == 0 ? 1 : reqHeight;
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 将所给的路径下的图片压缩到指定大小以下,该压缩一般用于图片的上传
     * <p/>
     * 该方法是压缩图片的质量, 注意它不会减少图片的像素,比方说, 你的图片是300K的, 1280*700像素的,
     * 经过该方法压缩后, File形式的图片是在100以下, 以方便上传服务器, 但是你BitmapFactory.decodeFile到内存中,
     * 变成Bitmap时,它的像素仍然是1280*700。
     * <p/>
     * 它会让图片重新构造, 但是有可能图像的位深(即色深)和每个像素的透明度会变化,JPEG onlysupports opaque(不透明),
     * 也就是说以jpeg格式压缩后, 原来图片中透明的元素将消失.所以这种格式很可能造成失真。
     * <p/>
     * 既然它是改变了图片的显示质量, 达到了对File形式的图片进行压缩, 图片的像素没有改变的话,
     * 那重新读取经过压缩的file为Bitmap时, 它占用的内存并不会少.
     *
     * @param filePath   图片路径
     * @param desSize_KB 目标大小，单位KB
     */
    public static byte[] compressBitmap(String filePath, int desSize_KB) {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        return compressBitmap(bitmap, desSize_KB, filePath);
    }


    /**
     * 压缩bitmap
     *
     * @param bitmap
     * @param size   压缩target 单位 kb
     * @return 返回不超过size大小的bitmap
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int size) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            while (baos.toByteArray().length / 1024 > size && options >= 10) {
                baos.reset();
                options -= 5;
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }
        }
        return bitmap;
    }

    /**
     * 把bitmap进行质量压缩后保存
     *
     * @param bitmap
     * @param desSize_KB
     * @param filePath
     */
    public static byte[] compressBitmap(Bitmap bitmap, int desSize_KB, String filePath) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        while (baos.toByteArray().length / 1024 > desSize_KB && quality >= 10) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();
            quality -= 5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        byte[] imgBytes = baos.toByteArray();

        //saveImageBytes(imgBytes, filePath);

        return imgBytes;
    }


    // ---------------------------图型变换相关操作-----------------------//

    //-----------------------1、缩放------------------------//

    /**
     * 缩放bitmap到指定的尺寸,使的控件可以完全放得下该图片，并且图片不变形
     *
     * @param bmp  需要缩放的图片源
     * @param newW 需要缩放成的图片宽度
     * @param newH 需要缩放成的图片高度
     * @return 缩放后的图片
     */
    public static Bitmap scaleBitmap(Bitmap bmp, int newW, int newH) {
        // 获得图片的宽高
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        // 计算缩放比例
        float scaleWidth = ((float) newW) / width;
        float scaleHeight = ((float) newH) / height;

        //图片宽高均小于控件，不需要压缩
        if (scaleHeight >= 1 && scaleWidth >= 1) {
            return bmp;
        }

        float scale = 0;
        //按照宽的压缩比例压缩
        if (scaleWidth <= 1 && scaleHeight >= 1) {
            scale = scaleWidth;
        } else if (scaleWidth >= 1 && scaleHeight <= 1) {//按照高的压缩比例压缩
            scale = scaleHeight;
        } else if (scaleWidth <= 1 && scaleHeight <= 1) {//按照宽，高的压缩比例大的压缩
            if (scaleWidth < scaleHeight) {
                scale = scaleWidth;
            } else {
                scale = scaleHeight;
            }
        }

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();

        matrix.postScale(scale, scale);

        //width = (int) (width * scale);
        //height = (int) (height * scale);

        // 得到新的图片
        Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

        return newBmp;
    }

    /**
     * 缩放bitmap到指定的尺寸，不管图片是否会变形
     *
     * @param bmp  需要缩放的图片源
     * @param newW 需要缩放成的图片宽度
     * @param newH 需要缩放成的图片高度
     * @return 缩放后的图片
     */
    public static Bitmap scaleBitmap2(Bitmap bmp, int newW, int newH) {
        // 获得图片的宽高
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        // 计算缩放比例
        float scaleWidth = ((float) newW) / width;
        float scaleHeight = ((float) newH) / height;

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        // 得到新的图片
        Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

        return bitmap;
    }

    //-----------------------2、旋转------------------------//

    /**
     * 旋转图片
     *
     * @param angle  角度
     * @param bitmap 源bitmap
     * @return Bitmap 旋转角度之后的bitmap
     */
    public static Bitmap rotatingBitmap(Bitmap bitmap, int angle) {
        //旋转图片 动作   
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        //重新构建Bitmap
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }


    /**
     * 获得倒影的图片
     *
     * @param bitmap 原始图片
     * @return 带倒影的图片
     */
    public static Bitmap makeReflectionImage(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

        Paint defaultPaint = new Paint();
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }


    /**
     * 在图片右下角添加水印
     *
     * @param srcBMP  原图
     * @param markBMP 水印图片
     * @return 合成水印后的图片
     */
    public static Bitmap composeWatermark(Bitmap srcBMP, Bitmap markBMP) {
        if (srcBMP == null) {
            return null;
        }

        // 创建一个新的和SRC长度宽度一样的位图
        Bitmap bitmap = Bitmap.createBitmap(srcBMP.getWidth(), srcBMP.getHeight(), Config.ARGB_8888);
        Canvas cv = new Canvas(bitmap);
        // 在 0，0坐标开始画入原图
        cv.drawBitmap(srcBMP, 0, 0, null);
        // 在原图的右下角画入水印
        cv.drawBitmap(markBMP, srcBMP.getWidth() - markBMP.getWidth() + 5, srcBMP.getHeight() - markBMP.getHeight() + 5, null);
        // 保存
        cv.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        cv.restore();

        return bitmap;
    }

    /**
     * 将图片转成指定弧度（角度）的图片
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        //根据图片创建画布
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    /**
     * 获取验证码图片
     *
     * @param width  验证码宽度
     * @param height 验证码高度
     * @return 验证码Bitmap对象
     */
    public static synchronized Bitmap makeValidateCode(int width, int height) {
        return ValidateCodeGenerator.createBitmap(width, height);
    }

    /**
     * 获取验证码值
     *
     * @return 验证码字符串
     */
    public static synchronized String gainValidateCodeValue() {
        return ValidateCodeGenerator.getCode();
    }

    /**
     * 随机生成验证码内部类
     */
    final static class ValidateCodeGenerator {
        private static final char[] CHARS = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };

        //default settings
        private static final int DEFAULT_CODE_LENGTH = 4;
        private static final int DEFAULT_FONT_SIZE = 20;
        private static final int DEFAULT_LINE_NUMBER = 3;
        private static final int BASE_PADDING_LEFT = 5, RANGE_PADDING_LEFT = 10, BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 10;
        private static final int DEFAULT_WIDTH = 60, DEFAULT_HEIGHT = 30;

        //variables
        private static String value;
        private static int padding_left, padding_top;
        private static Random random = new Random();

        public static Bitmap createBitmap(int width, int height) {
            padding_left = 0;
            //创建画布
            Bitmap bp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas c = new Canvas(bp);

            //随机生成验证码字符
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
                buffer.append(CHARS[random.nextInt(CHARS.length)]);
            }
            value = buffer.toString();

            //设置颜色
            c.drawColor(Color.WHITE);

            //设置画笔大小
            Paint paint = new Paint();
            paint.setTextSize(DEFAULT_FONT_SIZE);
            for (int i = 0; i < value.length(); i++) {
                //随机样式
                randomTextStyle(paint);
                padding_left += BASE_PADDING_LEFT + random.nextInt(RANGE_PADDING_LEFT);
                padding_top = BASE_PADDING_TOP + random.nextInt(RANGE_PADDING_TOP);
                c.drawText(value.charAt(i) + "", padding_left, padding_top, paint);
            }
            for (int i = 0; i < DEFAULT_LINE_NUMBER; i++) {
                drawLine(c, paint);
            }
            //保存
            c.save(Canvas.ALL_SAVE_FLAG);
            c.restore();

            return bp;
        }

        public static String getCode() {
            return value;
        }

        private static void randomTextStyle(Paint paint) {
            int color = randomColor(1);
            paint.setColor(color);
            paint.setFakeBoldText(random.nextBoolean());//true为粗体，false为非粗体
            float skewX = random.nextInt(11) / 10;
            skewX = random.nextBoolean() ? skewX : -skewX;
            paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
            paint.setUnderlineText(true); //true为下划线，false为非下划线
            paint.setStrikeThruText(true); //true为删除线，false为非删除线
        }

        private static void drawLine(Canvas canvas, Paint paint) {
            int color = randomColor(1);
            int startX = random.nextInt(DEFAULT_WIDTH);
            int startY = random.nextInt(DEFAULT_HEIGHT);
            int stopX = random.nextInt(DEFAULT_WIDTH);
            int stopY = random.nextInt(DEFAULT_HEIGHT);
            paint.setStrokeWidth(1);
            paint.setColor(color);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        private static int randomColor(int rate) {
            int red = random.nextInt(256) / rate;
            int green = random.nextInt(256) / rate;
            int blue = random.nextInt(256) / rate;
            return Color.rgb(red, green, blue);
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * Bitmap对象保存为图片文件
     *
     * @param bitmap
     */
    public static File bitmapToFile(Context context, Bitmap bitmap) {

        try {
            File file = StorageUtil.createFileByName(context, FileUtil.createNewImageName());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
