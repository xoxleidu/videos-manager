package com.dulei.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FFMPEG 的相关操作
 *
 * @author Administrator
 */
public class FFMpegUtil {

    //Windows下 ffmpeg.exe的路径
    private static String ffmpegEXE = "C:\\ffmpeg\\bin\\ffmpeg.exe";

    //Linux与mac下  ffmpeg的路径
    //private static String ffmpegEXE = "/developer/ffmpeg-4.0/bin/ffmpeg";


    /**
     * @param videoInputPath 视频的输入路径
     * @param videoOutPath   视频的输出路径
     * @throws Exception
     */
    // 拷贝视频，并指定新的视频的名字以及格式
    // ffmpeg.exe -i old.mp4 new.avi
    public static void convetor(String videoInputPath, String videoOutPath) throws Exception {

        List<String> command = new ArrayList<String>();
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoInputPath);
        command.add(videoOutPath);
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 使用这种方式会在瞬间大量消耗CPU和内存等系统资源，所以这里我们需要对流进行处理
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(inputStreamReader);

        String line = "";
        while ((line = br.readLine()) != null) {
        }
        if (br != null) {
            br.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }

    }

    /**
     * @param videoInputPath 原视频的路径
     * @param audioInputPath 音频的路径
     * @param videoOutPath   视频与音频结合之后的视频的路径
     * @param time           视频的长度 ,单位为 s
     * @throws Exception
     */
    // 将视频和音频结合，并指定视频的长度，同时生成结合之后的视频文件
    // ffmpeg.exe -i f:/UploadFileServer/File/user/101/upload/2018/09/01/1535816288685741.wmv -i f:/UploadFileServer/File/bgm/eluosi.mp3 -t 10 -y f:/UploadFileServer/File/user/101/videos/2018/09/01/1535816288685741.mp4
    public static void convetor(String videoInputPath, String audioInputPath, String videoOutPath, double time)
            throws Exception {

        List<String> command = new ArrayList<String>();
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoInputPath);
        command.add("-i");
        command.add(audioInputPath);
        command.add("-t");
        command.add(String.valueOf(time));
        command.add("-y");
        command.add(videoOutPath);
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 使用这种方式会在瞬间大量消耗CPU和内存等系统资源，所以这里我们需要对流进行处理
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(inputStreamReader);

        String line = "";
        while ((line = br.readLine()) != null) {
        }
        if (br != null) {
            br.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }

    }


    /**
     * @param time_coverimg   视频的第几秒作为封面图
     * @param videoInputPath  视频的路径
     * @param frame           帧数
     * @param coverOutputPath 视频的封面图的路径
     * @throws Exception
     */
    // ffmpeg.exe -ss 00:00:01 -y -i 视频.mp4 -vframes 1 new.jpg
    public static void convetor(String time_coverimg, String videoInputPath, int frame, String coverOutputPath)
            throws Exception {

        List<String> command = new ArrayList<String>();
        command.add(ffmpegEXE);
        command.add("-ss");
        command.add(time_coverimg);
        command.add("-y");
        command.add("-i");
        command.add(videoInputPath);
        command.add("-vframes");
        command.add(String.valueOf(frame));
        command.add(coverOutputPath);
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 使用这种方式会在瞬间大量消耗CPU和内存等系统资源，所以这里我们需要对流进行处理
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(inputStreamReader);

        String line = "";
        while ((line = br.readLine()) != null) {
        }
        if (br != null) {
            br.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }

    }

// public static void main(String[] args) {
//    String videoInputPath = "G:/videos-resources/180525DFH9X09GR4/video/2018052920010792217.mp4";
//    String coverOutputPath = "G:/videos-resources/180525DFH9X09GR4/video/2018052920014289695.jpg";
//    try {
//       convetor("00:00:01", videoInputPath, 1, coverOutputPath);
//    } catch (Exception e) {
//       e.printStackTrace();
//    }
// }

/*    public static void main(String[] args) {
        String coverOutputPath = "G:/videos-resources/180525DFH9X09GR4/video/2018052920014289695.jpg";
        int start = coverOutputPath.lastIndexOf("/");
        int end = coverOutputPath.lastIndexOf(".");
        System.out.println(coverOutputPath.substring(start));
        if (start != -1 && end != -1) {
            System.out.println(coverOutputPath.substring(start + 1, end));
        } else {
            System.out.println("NO");
        }

    }*/

}
