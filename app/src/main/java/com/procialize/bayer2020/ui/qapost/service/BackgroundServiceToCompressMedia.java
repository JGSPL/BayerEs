package com.procialize.bayer2020.ui.qapost.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.procialize.bayer2020.Constants.Constant;
import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.ui.qapost.roomDB.UploadMultimedia;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

/*import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor;
import com.abedelazizshe.lightcompressorlibrary.VideoQuality;*/
/*import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;*/
/*import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;*/

public class BackgroundServiceToCompressMedia extends IntentService {
    String TAG = "BackgroundServiceToCompressMedia";
    private List<UploadMultimedia> mediaList;
   // private FFmpeg ffmpeg;
   int startMsForVideoCutting = 0;//Start time for cutting video
    int endMsForVideoCutting = 120000;//End TIme for cutting video

    public BackgroundServiceToCompressMedia() {
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("BackgroundService", "Service Started");
        //Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        loadFFMpegBinary();
        EventAppDB eventAppDB = EventAppDB.getDatabase(this);
        mediaList = eventAppDB.uploadMultimediaQaDao().getNonCompressesMultimediaBg();
        //mediaList = (ArrayList<UploadMultimedia>) intent.getSerializableExtra("MediaList");

        if (mediaList.size() > 0) {

               /* if (mediaList.get(0).getMedia_type().equalsIgnoreCase("video")) {
                    String strPath = mediaList.get(0).getMedia_file();
                    executeCutVideoCommand(Uri.parse(strPath), 0);
                } else {

                    String strPath = mediaList.get(0).getMedia_file();
                    compressImage(strPath, 0);
                }*/

            if (mediaList.get(0).getMedia_type().equalsIgnoreCase("video")) {
                executeCutVideoCommand(Uri.parse(mediaList.get(0).getMedia_file()), 0);
            } else {
                if (mediaList.get(0).getMedia_file().contains("gif")) {
                    compressGif(0);
                } else {
                    compressImage(mediaList.get(0).getMedia_file(), 0);
                }

            }

        } else {
            Intent broadcastIntent = new Intent(Constant.BROADCAST_UPLOAD_MULTIMEDIA_ACTION);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
        }
        Log.d("countOfMedia", String.valueOf(mediaList.size()));
    }

    /**
     * Load FFmpeg binary
     */
    private void loadFFMpegBinary() {
       /* try {
            if (ffmpeg == null) {
                Log.d(TAG, "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    //showUnsupportedExceptionDialog();
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            //showUnsupportedExceptionDialog();
        } catch (Exception e) {
            Log.d(TAG, "EXception no controlada : " + e);
        }*/
    }

    /**
     * Executing ffmpeg binary
     */
    private String execFFmpegBinary(final String[] command, final String inputPath, final String outputPath, final int mediaListposition) {
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){
            String originalPath = mediaList.get(mediaListposition).getMedia_file();
            EventAppDB.getDatabase(getApplicationContext()).uploadMultimediaDao().updateCompressedPath(outputPath, originalPath);
            compressMedia(mediaListposition);
        } else{*/
        /*try {

            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.d(TAG, "FAILED with output : " + s);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        Log.d(TAG, "SUCCESS with output : " + s);
                        String thumbPath = mediaList.get(mediaListposition).getMedia_file_thumb();
                        String originalPath = mediaList.get(mediaListposition).getMedia_file();
                        EventAppDB.getDatabase(getApplicationContext()).uploadMultimediaDao().updateCompressedPath(outputPath, originalPath);
                        compressMedia(mediaListposition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                    Log.d(TAG, "progress : " + s);
                }

                @Override
                public void onStart() {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg " + command);
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }*/
        FFmpeg.executeAsync(command, new ExecuteCallback() {



            @Override
            public void apply(final long executionId, final int returnCode) {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                    String thumbPath = mediaList.get(mediaListposition).getMedia_file_thumb();
                    String originalPath = mediaList.get(mediaListposition).getMedia_file();
                    EventAppDB.getDatabase(getApplicationContext()).uploadMultimediaDao().updateCompressedPath(outputPath, String.valueOf(mediaList.get(mediaListposition).getMultimedia_id()));
                    compressMedia(mediaListposition);
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", returnCode));
                }
            }
        });
       /* VideoCompressor.start(inputPath,outputPath, new CompressionListener() {
            @Override
            public void onStart() {
                // Compression start
            }

            @Override
            public void onSuccess() {
                Log.i(TAG, "Async command execution completed successfully.");
                String thumbPath = mediaList.get(mediaListposition).getMedia_file_thumb();
                String originalPath = mediaList.get(mediaListposition).getMedia_file();
                EventAppDB.getDatabase(getApplicationContext()).uploadMultimediaDao().updateCompressedPath(outputPath, originalPath);
                compressMedia(mediaListposition);
            }

            @Override
            public void onFailure(String failureMessage) {
                // On Failure
            }

            @Override
            public void onProgress(final float v) {
                // Update UI with progress value
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("InProgress",v + "%");
                       *//* progress.setText(progressPercent + "%");
                        progressBar.setProgress((int) progressPercent);*//*
                    }
                });
            }

            @Override
            public void onCancelled() {
                // On Cancelled
            }
        }, VideoQuality.MEDIUM, false, false);*/
        //}

        return outputPath;
    }


    /**
     * Command for cutting video
     */
    private String executeCutVideoCommand(Uri selectedVideoUri, int mediaListposition) {
        String root = Environment.getExternalStorageDirectory().toString();
        File moviesDir = new File(root + Constant.FOLDER_DIRECTORY + Constant.VIDEO_DIRECTORY);//"/VideoCompressDemo/CompressedVideo");

        if (!moviesDir.exists()) {
            moviesDir.mkdirs();
        }
        String path = selectedVideoUri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            path = path.substring(cut + 1);
        }
        String fileExtn = ".mp4";
        String filePrefix = path.replace(".mp4", "");
        Calendar calendar = Calendar.getInstance();
        long timeStamp = calendar.getTimeInMillis();
        //File dest = new File(moviesDir, filePrefix + fileExtn);
        File dest = new File(moviesDir, timeStamp + fileExtn);


        int fileNo = 0;
        while (dest.exists()) {
            fileNo++;
            //dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
            dest = new File(moviesDir, timeStamp  + fileNo + fileExtn);
        }
        String filePath = dest.getAbsolutePath();


   /*     String[] complexCommand = new String[]{"-y", "-i", selectedVideoUri.toString(), "-s", "640x480", "-r", "25",
                //"-vcodec",
                "-c:v",
                "libx264",  "-maxrate", "1984k",
                "-bufsize", "3968k","-b:v", "3000k", "-b:a", "48000",
                "-movflags", "+faststart", "-profile:v", "baseline", "-level", "3.1" ,"-crf", "28","-preset", "ultrafast",  "-ac", "2", "-ar", "22050", filePath };*/

/*        String[] complexCommand = new String[]{"-y", "-i", selectedVideoUri.toString(), "-s", "640x480", "-r", "25",
                //"-vcodec",
                "-c:v",
                "libx264", "-minrate", "4000k", "-maxrate", "4000k","-b:v", "250k", "-b:a", "48000",
                "-profile:v", "baseline", "-level", "3.1" ,"-crf", "28","-preset", "veryfast",  "-ac", "2", "-ar", "22050", filePath };*/
        String[] complexCommand = new String[]{"-y", "-i", selectedVideoUri.toString(),  "-ss", "" + startMsForVideoCutting / 1000,
                "-t", "" + (endMsForVideoCutting - startMsForVideoCutting) / 1000/*,"-s", "640x480"*/, "-r", "25",
                //"-vcodec",
                "-c:v",
                "libx264", "-minrate", "4000k", "-maxrate", "4000k","-b:v", "250k", "-b:a", "48000",
                "-profile:v", "baseline", "-level", "3.1" ,"-crf", "28","-preset", "veryfast",
                "-ac", "2", "-ar", "22050", filePath };
        execFFmpegBinary(complexCommand,selectedVideoUri.toString(), filePath, mediaListposition);

        return filePath;
    }

    private void compressImage(String media_file, int mediaListposition) {
        try {
            Uri myUri = Uri.parse(media_file);
            Log.d("Bg Service compression", mediaListposition + "");

            File auxFile = new File(myUri.toString());
            ExifInterface exif = new ExifInterface(auxFile.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix mat = new Matrix();
            mat.postRotate(angle);

            String root = Environment.getExternalStorageDirectory().toString();
            File moviesDir = new File(root + Constant.FOLDER_DIRECTORY + Constant.IMAGE_DIRECTORY);
            String path = myUri.getPath();
            int cut = path.lastIndexOf('/');
            if (cut != -1) {
                path = path.substring(cut + 1);
            }
            String fileExtn = "";
            String filePrefix = "";
            if (path.contains("jpg")) {
                fileExtn = ".jpg";
                filePrefix = path.replace(".jpg", "");
            } else if (path.contains("png")) {
                fileExtn = ".png";
                filePrefix = path.replace(".png", "");
            } else if (path.contains("jpeg")) {
                fileExtn = ".jpeg";
                filePrefix = path.replace(".jpeg", "");
            }
            File dest = new File(moviesDir, filePrefix + fileExtn);


            int fileNo = 0;
            while (dest.exists()) {
                fileNo++;
                dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
            }
            //copyFile(media_file,moviesDir+auxFile.getName());
            try {
                Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(auxFile), null, null);
                Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                correctBmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
                File f1 = new File(media_file);
                f1.createNewFile();

                //copyFile(media_file, dest.getPath());
                FileOutputStream fo = new FileOutputStream(auxFile);
                fo.write(bytes.toByteArray());
                fo.close();
                EventAppDB.getDatabase(getApplicationContext()).uploadMultimediaDao().updateCompressedPath(f1.getPath(), String.valueOf(mediaList.get(mediaListposition).getMultimedia_id()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            compressMedia(mediaListposition);
        } catch (IOException e) {
            Log.w("TAG", "-- Error in setting image");
        } catch (OutOfMemoryError oom) {
            Log.w("TAG", "-- OOM Error in setting image");
        }
    }

    public void compressMedia(int mediaListposition) {
        if (mediaList.size() > mediaListposition + 1) {
            if (mediaList.get(mediaListposition + 1).getMedia_type().equalsIgnoreCase("video")) {
                executeCutVideoCommand(Uri.parse(mediaList.get(mediaListposition + 1).getMedia_file()), mediaListposition + 1);
            } else {
                if (mediaList.get(mediaListposition + 1).getMedia_file().contains("gif")) {
                    compressGif(mediaListposition + 1);
                } else {
                    compressImage(mediaList.get(mediaListposition + 1).getMedia_file(), mediaListposition + 1);
                }
            }
        } else {
            Intent broadcastIntent = new Intent(Constant.BROADCAST_UPLOAD_MULTIMEDIA_ACTION);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
        }
    }

    public void compressGif(int mediaListposition) {
        String originalPath = mediaList.get(mediaListposition).getMedia_file();
        EventAppDB.getDatabase(getApplicationContext()).uploadMultimediaDao().updateCompressedPath(originalPath, String.valueOf(mediaList.get(mediaListposition).getMultimedia_id()));//originalPath);
        if (mediaList.size() > mediaListposition + 1) {
            /*if(mediaList.get(mediaListposition).getMedia_type().contains("video")) {
                executeCutVideoCommand(Uri.parse(mediaList.get(mediaListposition + 1).getMedia_file()), mediaListposition + 1);
            }else
            { */
            compressMedia(mediaListposition + 1);

            /* }*/
        } else {
            Intent broadcastIntent = new Intent(Constant.BROADCAST_UPLOAD_MULTIMEDIA_ACTION);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
        }
    }
}
