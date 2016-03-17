package com.zhihucrawler.utils;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import com.zhihucrawler.config.Const;

public class ImageUtil {

	/**
	 * @description 从网络上下载图片
	 * @param urlstr 图片的网络地址
	 * @param savepath 本地的保存路径
	 * @return String 本地图片保存全路径
	 */
	public static String getImageFromUrl(String urlstr, String savepath) {
		if (urlstr == null || "".equals(urlstr)) {
			return null;
		}
		if(savepath == null || "".equals(savepath)) {
			savepath = Const.IMAGE_SAVE_PATH;
		}
		int hostNum = urlstr.indexOf('/', 8);
		int num = urlstr.lastIndexOf('/');
		int extnum = urlstr.lastIndexOf('.');

		String host = urlstr.substring(0, hostNum);//主机名
		String name = urlstr.substring(num + 1, extnum);//文件名
		String ext = urlstr.substring(extnum + 1, urlstr.length());//文件拓展名
		String fileName = System.currentTimeMillis() + "_" + name + "." + ext;
		try {
			URL url = new URL(urlstr);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("referer", host); //通过这个http头的伪装来反盗链
			BufferedImage image = ImageIO.read(connection.getInputStream());
			FileOutputStream fout = new FileOutputStream(savepath + fileName);
//			if ("gif".equals(ext) || "png".equals(ext)) {
				ImageIO.write(image, ext, fout);
//			}
//			ImageIO.write(image, "jpg", fout);
			fout.flush();
			fout.close();
			return savepath + fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String urlstr = "https://pic4.zhimg.com/f54eec97e9b4bcf34ff80852b81f3f23_l.jpg";
		System.out.println(ImageUtil.getImageFromUrl(urlstr, null));

	}

}
